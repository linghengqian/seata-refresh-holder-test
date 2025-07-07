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
import org.firebirdsql.management.FBManager;
import org.firebirdsql.management.PageSizeConstants;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.MountableFile;

import javax.sql.DataSource;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@SuppressWarnings({"resource", "SqlNoDataSourceInspection"})
class SimpleTest {

    @Test
    void test() throws Exception {
        testPostgresWithSeata();
        testPostgresWithoutSeata();
        testFirebirdWithoutSeata();
    }

    private void testPostgresWithSeata() {
        assertThat(System.getProperty("service.default.grouplist"), is(nullValue()));
        try (GenericContainer<?> seataContainer = new GenericContainer<>("apache/seata-server:2.4.0")
                .withExposedPorts(7091, 8091)
                .waitingFor(Wait.forHttp("/health").forPort(7091).forStatusCode(200).forResponsePredicate("ok"::equals));
             PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:17.5-bookworm")
                     .withCopyFileToContainer(
                             MountableFile.forHostPath(Paths.get("src/test/resources/postgres.sh").toAbsolutePath()),
                             "/docker-entrypoint-initdb.d/postgres.sh")
        ) {
            seataContainer.start();
            postgresContainer.start();
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
            executePostgresSQL(seataDataSource);
            System.clearProperty("service.default.grouplist");
            TmNettyRemotingClient.getInstance().destroy();
            RmNettyRemotingClient.getInstance().destroy();
            ConfigurationFactory.reload();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void testPostgresWithoutSeata() throws SQLException {
        try (PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:17.5-bookworm")
                .withCopyFileToContainer(
                        MountableFile.forHostPath(Paths.get("src/test/resources/postgres.sh").toAbsolutePath()),
                        "/docker-entrypoint-initdb.d/postgres.sh")) {
            postgresContainer.start();
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:postgresql://127.0.0.1:" + postgresContainer.getMappedPort(5432) + "/demo_ds_0");
            config.setUsername("test");
            config.setPassword("test");
            HikariDataSource hikariDataSource = new HikariDataSource(config);
            Awaitility.await().atMost(Duration.ofSeconds(15L)).ignoreExceptions().until(() -> {
                hikariDataSource.getConnection().close();
                return true;
            });
            executePostgresSQL(hikariDataSource);
        }
    }

    private void testFirebirdWithoutSeata() throws Exception {
        try (GenericContainer<?> container = new GenericContainer<>("firebirdsql/firebird:5.0.2")
                .withEnv("FIREBIRD_ROOT_PASSWORD", "masterkey")
                .withEnv("FIREBIRD_USER", "alice")
                .withEnv("FIREBIRD_PASSWORD", "masterkey")
                .withEnv("FIREBIRD_DATABASE", "mirror.fdb")
                .withEnv("FIREBIRD_DATABASE_DEFAULT_CHARSET", "UTF8")
                .withExposedPorts(3050)) {
            container.start();
            Awaitility.await().atMost(Duration.ofMinutes(1L)).ignoreExceptions().until(() -> {
                Properties props = new Properties();
                props.setProperty("user", "alice");
                props.setProperty("password", "masterkey");
                DriverManager.getConnection(
                        "jdbc:firebird://localhost:" + container.getMappedPort(3050) + "//var/lib/firebird/data/" + "mirror.fdb",
                        props
                ).close();
                return true;
            });
            try (FBManager fbManager = new FBManager()) {
                fbManager.setServer("localhost");
                fbManager.setUserName("alice");
                fbManager.setPassword("masterkey");
                fbManager.setFileName("/var/lib/firebird/data/mirror.fdb");
                fbManager.setPageSize(PageSizeConstants.SIZE_16K);
                fbManager.setDefaultCharacterSet("UTF8");
                fbManager.setPort(container.getMappedPort(3050));
                fbManager.start();
                fbManager.createDatabase("/var/lib/firebird/data/demo_ds_0.fdb", "alice", "masterkey");
            }
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:firebird://localhost:" + container.getMappedPort(3050) + "//var/lib/firebird/data/" + "demo_ds_0.fdb");
            config.setUsername("alice");
            config.setPassword("masterkey");
            HikariDataSource hikariDataSource = new HikariDataSource(config);
            executeFirebird(hikariDataSource);
        }
    }

    private void executePostgresSQL(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().execute("""
                    CREATE TABLE IF NOT EXISTS t_order (
                        order_id BIGSERIAL NOT NULL PRIMARY KEY,
                        order_type INTEGER,
                        user_id INTEGER NOT NULL,
                        phone VARCHAR(50) NOT NULL,
                        status VARCHAR(50)
                    )
                    """);
            connection.createStatement().execute("TRUNCATE TABLE t_order");
        }
        try (Connection conn = dataSource.getConnection()) {
            try {
                conn.setAutoCommit(false);
                conn.createStatement().executeUpdate("INSERT INTO t_order (order_id, user_id, phone, status) VALUES (2024, 2024, '13800000001', 'INSERT_TEST')");
                conn.createStatement().executeUpdate("INSERT INTO t_order_does_not_exist (test_id_does_not_exist) VALUES (2024)");
                conn.commit();
            } catch (final SQLException ignored) {
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
            }
        }
        try (Connection conn = dataSource.getConnection()) {
            ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM t_order WHERE user_id = 2024");
            assertThat(resultSet.next(), CoreMatchers.is(false));
        }
        try (Connection conn = dataSource.getConnection()) {
            try {
                conn.setAutoCommit(false);
                conn.createStatement().executeUpdate("INSERT INTO t_order (order_id, user_id, phone, status) VALUES (2025, 2025, '13800000001', 'INSERT_TEST')");
                conn.commit();
            } catch (final SQLException ignored) {
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
            }
        }
        try (Connection conn = dataSource.getConnection()) {
            ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM t_order WHERE user_id = 2025");
            assertThat(resultSet.next(), CoreMatchers.is(true));
        }
        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().execute("DELETE FROM t_order WHERE user_id = 2025");
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM t_order WHERE user_id = 2025");
            assertThat(resultSet.next(), CoreMatchers.is(false));
            connection.createStatement().execute("DROP TABLE IF EXISTS t_order");
        }
    }

    private void executeFirebird(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().execute("""
                    CREATE TABLE t_order(
                         order_id BIGINT generated by default as identity PRIMARY KEY,
                         order_type INT,
                         user_id INT NOT NULL,
                         phone VARCHAR(50) NOT NULL,
                         status VARCHAR(50)
                    )
                    """);
        }
        try (Connection conn = dataSource.getConnection()) {
            try {
                conn.setAutoCommit(false);
                conn.createStatement().executeUpdate("INSERT INTO t_order (order_id, user_id, phone, status) VALUES (2024, 2024, '13800000001', 'INSERT_TEST')");
                conn.createStatement().executeUpdate("INSERT INTO t_order_does_not_exist (test_id_does_not_exist) VALUES (2024)");
                conn.commit();
            } catch (final SQLException ignored) {
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
            }
        }
        try (Connection conn = dataSource.getConnection()) {
            ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM t_order WHERE user_id = 2024");
            assertThat(resultSet.next(), CoreMatchers.is(false));
        }
        try (Connection conn = dataSource.getConnection()) {
            try {
                conn.setAutoCommit(false);
                conn.createStatement().executeUpdate("INSERT INTO t_order (order_id, user_id, phone, status) VALUES (2025, 2025, '13800000001', 'INSERT_TEST')");
                conn.commit();
            } catch (final SQLException ignored) {
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
            }
        }
        try (Connection conn = dataSource.getConnection()) {
            ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM t_order WHERE user_id = 2025");
            assertThat(resultSet.next(), CoreMatchers.is(true));
        }
        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().execute("DELETE FROM t_order WHERE user_id = 2025");
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM t_order WHERE user_id = 2025");
            assertThat(resultSet.next(), CoreMatchers.is(false));
        }
        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().execute("DROP TABLE t_order");
        }
    }
}