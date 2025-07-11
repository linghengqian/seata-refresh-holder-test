package com.github.linghengqian;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.seata.config.ConfigurationFactory;
import org.apache.seata.core.rpc.netty.RmNettyRemotingClient;
import org.apache.seata.core.rpc.netty.TmNettyRemotingClient;
import org.apache.seata.rm.RMClient;
import org.apache.seata.rm.datasource.DataSourceProxy;
import org.apache.seata.tm.TMClient;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@SuppressWarnings("SqlNoDataSourceInspection")
@Testcontainers
class SimpleTest {

    @SuppressWarnings("resource")
    @Container
    private final GenericContainer<?> seataContainer = new GenericContainer<>("apache/seata-server:2.4.0")
            .withExposedPorts(7091, 8091)
            .waitingFor(Wait.forHttp("/health").forPort(7091).forStatusCode(200).forResponsePredicate("ok"::equals));

    @SuppressWarnings("resource")
    @Container
    private final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:17.2-bookworm")
            .withCopyFileToContainer(
                    MountableFile.forHostPath(Paths.get("src/test/resources/postgres.sh").toAbsolutePath()),
                    "/docker-entrypoint-initdb.d/postgres.sh");

    @Test
    void test() throws SQLException {
        testPostgresWithSeata();
    }

    private void testPostgresWithSeata() throws SQLException {
        assertThat(System.getProperty("service.default.grouplist"), is(nullValue()));
        System.setProperty("service.default.grouplist", "127.0.0.1:" + seataContainer.getMappedPort(8091));
        TMClient.init("test-first", "default_tx_group");
        RMClient.init("test-first", "default_tx_group");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://127.0.0.1:" + postgresContainer.getMappedPort(5432) + "/demo_ds_0");
        config.setUsername("test");
        config.setPassword("test");
        HikariDataSource hikariDataSource = new HikariDataSource(config);
        DataSourceProxy seataDataSource = new DataSourceProxy(hikariDataSource);
        Awaitility.await().atMost(Duration.ofSeconds(15L)).ignoreExceptions().until(() -> {
            seataDataSource.getConnection().close();
            return true;
        });
        DbUtil.executePostgresSQL(seataDataSource);
        System.clearProperty("service.default.grouplist");
        TmNettyRemotingClient.getInstance().destroy();
        RmNettyRemotingClient.getInstance().destroy();
        ConfigurationFactory.reload();
    }
}
