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
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.jdbc.ContainerDatabaseDriver;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@SuppressWarnings("resource")
public class SimpleTest {

    @Test
    void test() {
        extracted("demo_ds_10");
        extractedWithoutSeata("demo_ds_1");
        extractedWithoutSeata("demo_ds_2");
        extractedWithoutSeata("demo_ds_3");
        extracted("demo_ds_11");
        extractedWithoutSeata("demo_ds_4");
        extractedWithoutSeata("demo_ds_5");
    }

    private static void extracted(String databaseName) {
        assertThat(System.getProperty("service.default.grouplist"), is(nullValue()));
        try (GenericContainer<?> seataContainer = new GenericContainer<>("apache/seata-server:2.3.0")
                .withExposedPorts(7091, 8091)
                .waitingFor(Wait.forHttp("/health").forPort(7091).forStatusCode(200).forResponsePredicate("ok"::equals))
        ) {
            seataContainer.start();
            System.setProperty("service.default.grouplist", "127.0.0.1:" + seataContainer.getMappedPort(8091));
            TMClient.init("test-first", "default_tx_group");
            RMClient.init("test-first", "default_tx_group");
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("org.testcontainers.jdbc.ContainerDatabaseDriver");
            config.setJdbcUrl("jdbc:tc:postgresql:17.5-bookworm:///" + databaseName + "?TC_INITSCRIPT=init.sql");
            HikariDataSource hikariDataSource = new HikariDataSource(config);
            DataSourceProxy seataDataSource = new DataSourceProxy(hikariDataSource);
            Awaitility.await().atMost(Duration.ofSeconds(15L)).ignoreExceptions().until(() -> {
                seataDataSource.getConnection().close();
                return true;
            });
            System.clearProperty("service.default.grouplist");
            TmNettyRemotingClient.getInstance().destroy();
            RmNettyRemotingClient.getInstance().destroy();
            ConfigurationFactory.reload();
        }
        ContainerDatabaseDriver.killContainers();
        Awaitility.await().timeout(3L, TimeUnit.MINUTES).pollDelay(2L, TimeUnit.MINUTES).until(() -> true);
    }

    private static void extractedWithoutSeata(String databaseName) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.testcontainers.jdbc.ContainerDatabaseDriver");
        config.setJdbcUrl("jdbc:tc:postgresql:17.5-bookworm:///" + databaseName + "?TC_INITSCRIPT=init.sql");
        HikariDataSource hikariDataSource = new HikariDataSource(config);
        Awaitility.await().atMost(Duration.ofSeconds(15L)).ignoreExceptions().until(() -> {
            hikariDataSource.getConnection().close();
            return true;
        });
        ContainerDatabaseDriver.killContainers();
        Awaitility.await().timeout(3L, TimeUnit.MINUTES).pollDelay(2L, TimeUnit.MINUTES).until(() -> true);
    }
}