package com.github.linghengqian;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import javax.sql.DataSource;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@SuppressWarnings("SqlNoDataSourceInspection")
@Testcontainers
public class ShardingSphereTest {
    @SuppressWarnings("resource")
    @Container
    private final GenericContainer<?> seataContainer = new GenericContainer<>("apache/seata-server:2.4.0")
            .withExposedPorts(7091, 8091)
            .waitingFor(Wait.forHttp("/health").forPort(7091).forStatusCode(200).forResponsePredicate("ok"::equals));

    @SuppressWarnings("resource")
    @Container
    private final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:17.2-bookworm")
            .withCopyFileToContainer(
                    MountableFile.forHostPath(Paths.get("src/test/resources/sh/postgres.sh").toAbsolutePath()),
                    "/docker-entrypoint-initdb.d/postgres.sh");

    private ProxyTestingServer proxyTestingServer;

    @Test
    void test() throws SQLException {
        assertThat(System.getProperty("service.default.grouplist"), is(nullValue()));
        System.setProperty("service.default.grouplist", "127.0.0.1:" + seataContainer.getMappedPort(8091));
        Awaitility.await().atMost(Duration.ofSeconds(30L)).ignoreExceptions().until(() -> {
            DbUtil.openConnection("test", "test", "jdbc:postgresql://127.0.0.1:" + postgresContainer.getMappedPort(5432) + "/")
                    .close();
            return true;
        });
        String absolutePath = Paths.get("src/test/resources/base").toAbsolutePath().toString();
        proxyTestingServer = new ProxyTestingServer(absolutePath);
        Awaitility.await().atMost(Duration.ofSeconds(30L)).ignoreExceptions().until(() -> {
            DbUtil.openConnection("root", "root", "jdbc:postgresql://127.0.0.1:" + proxyTestingServer.getProxyPort() + "/postgres").close();
            return true;
        });
        testPostgresWithSeata();
        Awaitility.await().pollDelay(5L, TimeUnit.SECONDS).until(() -> true); // TODO Apparently there is a real connection leak on Seata Client 2.4.0.
        System.clearProperty("service.default.grouplist");
        proxyTestingServer.close(Collections.singletonList("sharding_db"));
    }

    private void testPostgresWithSeata() throws SQLException {
        try (Connection connection = DbUtil.openConnection("root", "root", "jdbc:postgresql://127.0.0.1:" + proxyTestingServer.getProxyPort() + "/postgres")) {
            connection.createStatement().execute("CREATE DATABASE sharding_db");
        }
        try (Connection connection = DbUtil.openConnection("root", "root", "jdbc:postgresql://127.0.0.1:" + proxyTestingServer.getProxyPort() + "/sharding_db");
             Statement statement = connection.createStatement()) {
            statement.execute("""
                    REGISTER STORAGE UNIT ds_0 (
                      URL='jdbc:postgresql://127.0.0.1:%d/demo_ds_0',
                      USER='test',
                      PASSWORD='test'
                    ),ds_1 (
                      URL='jdbc:postgresql://127.0.0.1:%d/demo_ds_1',
                      USER='test',
                      PASSWORD='test'
                    ),ds_2 (
                      URL='jdbc:postgresql://127.0.0.1:%d/demo_ds_2',
                      USER='test',
                      PASSWORD='test'
                    )
                    """.formatted(postgresContainer.getMappedPort(5432), postgresContainer.getMappedPort(5432), postgresContainer.getMappedPort(5432)));
            statement.execute("""
                    CREATE DEFAULT SHARDING DATABASE STRATEGY (
                      TYPE='standard',
                      SHARDING_COLUMN=user_id,
                      SHARDING_ALGORITHM(
                        TYPE(
                          NAME=INLINE,
                          PROPERTIES(
                            'algorithm-expression'='ds_${user_id % 2}',
                            'allow-range-query-with-inline-sharding'='false'
                          )
                        )
                      )
                    )""");
            statement.execute("""
                    CREATE SHARDING TABLE RULE t_order (
                      DATANODES('<LITERAL>ds_0.t_order, ds_1.t_order, ds_2.t_order'),
                      KEY_GENERATE_STRATEGY(COLUMN=order_id,TYPE(NAME='SNOWFLAKE'))
                    )""");
        }
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl("jdbc:postgresql://127.0.0.1:" + proxyTestingServer.getProxyPort() + "/sharding_db");
        config.setUsername("root");
        config.setPassword("root");
        DataSource dataSource = new HikariDataSource(config);
        DbUtil.executePostgresSQL(dataSource);
    }
}
