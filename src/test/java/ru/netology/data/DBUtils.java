package ru.netology.data;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    private static String urlMySql = "jdbc:mysql://localhost:3306/app";
    private static String user = System.getProperty("db.user");
    private static String password = System.getProperty("db.password");


    @SneakyThrows
    public static void clearTables() {
        val deletePaymentEntity = "DELETE FROM payment_entity";
        val deleteCreditEntity = "DELETE FROM credit_request_entity";
        val deleteOrderEntity = "DELETE FROM order_entity";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(
                urlMySql, user, password)
        ) {
            runner.update(conn, deletePaymentEntity);
            runner.update(conn, deleteCreditEntity);
            runner.update(conn, deleteOrderEntity);
        }
    }

    @SneakyThrows
    public static String getPaymentStatus() {
        String status = "SELECT status FROM payment_entity";
        return getStatus(status);
    }

    @SneakyThrows
    public static String getCreditStatus() {
        String status = "SELECT status FROM credit_request_entity";
        return getStatus(status);
    }

    @SneakyThrows
    public static String getStatus(String status) {
        String result = "";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(
                urlMySql, user, password)
        ) {
            result = runner.query(conn, status, new ScalarHandler<String>());
            System.out.println(result);
            return result;
        }
    }
}
