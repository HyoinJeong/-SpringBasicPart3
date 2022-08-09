package org.prgms.kdt;

import org.prgms.kdt.customer.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JdbcCustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);
    private final String SELECT_BY_NAME_SQL = "select * from customers WHERE name=?";
    private final String SELECT_ALL_SQL = "select * from customers";
    private final String INSERT_SQL = "insert into customers(customer_id, name, email) VALUES (UUID_TO_BIN(?), ?, ?)";
    private final String DELETE_ALL_SQL = "delete from customers";
    private final String UPDATE_BY_ID_SQL = "update customers set name = ? where customer_id=UUID_TO_BIN(?)";


    public List<String> findNames(String name) {
        List<String> names = new ArrayList<>();

        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "hi01071104");
                var statement = connection.prepareStatement(SELECT_BY_NAME_SQL);
        ) {
            statement.setString(1, name);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var customerName = resultSet.getString("name");
                    var customerId = toUUID(resultSet.getBytes("customer_id")); // UUID라서 getBytes
                    var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
//                    logger.info("customer Id -> {}, name -> {}, createdAt -> {} ", customerId, customerName, createdAt);
                    names.add(customerName);
                }
            }
        } catch (SQLException throwables) {
            logger.error("Got error while closing connection", throwables);
        }
        return names;
    }

    public List<String> findAllNames() {
        List<String> names = new ArrayList<>();
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "hi01071104");
                var statement = connection.prepareStatement(SELECT_ALL_SQL);
                var resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
                var customerName = resultSet.getString("name");
                var customerId = toUUID(resultSet.getBytes("customer_id")); // UUID라서 getBytes
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
//                logger.info("customer Id -> {}, name -> {}, createdAt -> {} ", customerId, customerName, createdAt);
                names.add(customerName);
            }

        } catch (SQLException throwables) {
            logger.error("Got error while closing connection", throwables);
        }
        return names;
    }

    public List<UUID> findAllIds() {
        List<UUID> uuids = new ArrayList<>();
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "hi01071104");
                var statement = connection.prepareStatement(SELECT_ALL_SQL);
                var resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
                var customerName = resultSet.getString("name");
                var customerId = toUUID(resultSet.getBytes("customer_id"));// UUID라서 getBytes
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
//                logger.info("customer Id -> {}, name -> {}, createdAt -> {} ", customerId, customerName, createdAt);
                uuids.add(customerId);
            }

        } catch (SQLException throwables) {
            logger.error("Got error while closing connection", throwables);
        }
        return uuids;
    }

    public int insertCustomer(UUID customerId, String name, String email) {
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "hi01071104");
                var statement = connection.prepareStatement(INSERT_SQL);
        ) {
            statement.setBytes(1, customerId.toString().getBytes());
            statement.setString(2, name);
            statement.setString(3, email);
            return statement.executeUpdate();
        } catch (SQLException throwables) {
            logger.error("Got error while closing connection", throwables);
        }
        return 0;
    }

    public int updateCustomerName(UUID customerId, String name) {
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "hi01071104");
                var statement = connection.prepareStatement(UPDATE_BY_ID_SQL);
        ) {
            statement.setString(1, name);
            statement.setBytes(2, customerId.toString().getBytes());
            return statement.executeUpdate();
        } catch (SQLException throwables) {
            logger.error("Got error while closing connection", throwables);
        }
        return 0;
    }

    public int deleteAllCustomers() {
        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "hi01071104");
                var statement = connection.prepareStatement(DELETE_ALL_SQL);
        ) {
            return statement.executeUpdate();
        } catch (SQLException throwables) {
            logger.error("Got error while closing connection", throwables);
        }
        return 0;
    }

    public void transactionTest(Customer customer) {
        String updateNameSql = "update customers set name = ? where customer_id=UUID_TO_BIN(?)";
        String updateEmailSql = "update customers set email = ? where customer_id=UUID_TO_BIN(?)";

        Connection connection=null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "hi01071104");
            connection.setAutoCommit(false);
            try (
                    var updateNameStatement = connection.prepareStatement(updateNameSql);
                    var updateEmailStatement = connection.prepareStatement(updateEmailSql);
            ) {
                updateNameStatement.setString(1, customer.getName());
                updateNameStatement.setBytes(2, customer.getCustomerId().toString().getBytes());
                updateNameStatement.executeUpdate();

                updateEmailStatement.setString(1, customer.getEmail());
                updateEmailStatement.setBytes(2, customer.getCustomerId().toString().getBytes());
                updateEmailStatement.executeUpdate();
                connection.setAutoCommit(true);

            }
        } catch (SQLException e) {
            if(connection!=null){
                try {
                    connection.rollback();
                    connection.close();
                } catch (SQLException throwables) {
                    logger.error("Got error while closing connection", throwables);
                    throw new RuntimeException(e);
                }
            }
            logger.error("Got error while closing connection", e);
            throw new RuntimeException(e);
        }
    }

    static UUID toUUID(byte[] bytes) {
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }

    public static void main(String[] args) {
        var customerRepository = new JdbcCustomerRepository();

        customerRepository.transactionTest(new Customer(UUID.fromString("43ac12e9-5012-4939-b607-2a7aa05d40d1"), "update-user", "new-user2@gmail.com", LocalDateTime.now()));

//        var count=customerRepository.deleteAllCustomers();
//        logger.info("delete count -> {}", count);

//        customerRepository.insertCustomer(UUID.randomUUID(),"new-user","new-user@gmail.com");
//        var customer2 = UUID.randomUUID();
//        customerRepository.insertCustomer(customer2,"new-user2","new-user2@gmail.com");
//        customerRepository.findAllNames().forEach(v -> logger.info("Found name : {}", v));
//
//        customerRepository.updateCustomerName(customer2,"updated-user2");
//        customerRepository.findAllNames().forEach(v -> logger.info("Found name : {}", v));

//        var customerId=UUID.randomUUID();
//        logger.info("created customerId -> {}",customerId);
//        logger.info("created UUID Version -> {}",customerId.version());
//
//        customerRepository.insertCustomer(customerId,"new-user2","new-user2@gmail.com");
//        customerRepository.findAllIds().forEach(v -> logger.info("Found Id : {} and version {}", v, v.version()));
    }
}
