package com.github.linghengqian;

import org.hamcrest.CoreMatchers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("SqlNoDataSourceInspection")
public class DbUtil {
    public static void executePostgresSQL(DataSource dataSource) throws SQLException {
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

    public static Connection openConnection(final String username, final String password, final String jdbcUrl) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        return DriverManager.getConnection(jdbcUrl, props);
    }
}
