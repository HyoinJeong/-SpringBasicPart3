package org.prgms.kdt.customer.repository;

import org.prgms.kdt.JdbcCustomerRepository;
import org.prgms.kdt.customer.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.*;

@Repository
@Primary
public class CustomerNamedJdbcRepository implements CustomerRepository {
    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;


    private static RowMapper<Customer> customerRowMapper = (resultSet, i) -> {
        var customerName = resultSet.getString("name");
        var email = resultSet.getString("email");
        var customerId = toUUID(resultSet.getBytes("customer_id")); // UUID라서 getBytes
        var lastLoginAt = resultSet.getTimestamp("last_login_at") != null ?
                resultSet.getTimestamp("last_login_at").toLocalDateTime() : null;
        var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        return new Customer(customerId, customerName, email, lastLoginAt, createdAt);
    };
    ;

    public CustomerNamedJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Map<String,Object> toParamMap(Customer customer){
        return new HashMap<String, Object>() {{
            put("customerId", customer.getCustomerId().toString().getBytes());
            put("name", customer.getName());
            put("email", customer.getEmail());
            put("createdAt", Timestamp.valueOf(customer.getCreatedAt()));
            put("lastLoginAt", customer.getLastLoginAt() != null ? Timestamp.valueOf(customer.getLastLoginAt()) : null);
        }};
    }

    @Override
    public Customer insert(Customer customer) {
        var update=jdbcTemplate.update("INSERT INTO order_mgmt.customers(customer_id, name, email, created_at) VALUES (UNHEX(REPLACE(:customerId, '-', '')), :name, :email, :createdAt)",
                toParamMap(customer));
        if (update != 1) {
            throw new RuntimeException("Noting was iserted");
        }
        return customer;
    }

    @Override
    public Customer update(Customer customer) {
        var update = jdbcTemplate.update("UPDATE order_mgmt.customers SET name = :name, email = :email, last_login_at = :lastLoginAt  WHERE customer_id = UNHEX(REPLACE(:customerId, '-', ''))",
                toParamMap(customer));
        if (update != 1) {
            throw new RuntimeException("Noting was updated");
        }
        return customer;
    }

    @Override
    public int count() {
        return jdbcTemplate.queryForObject("select count(*) from order_mgmt.customers", Collections.emptyMap() ,Integer.class);
    }

    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query("select * from order_mgmt.customers", customerRowMapper);
    }


    @Override
    public Optional<Customer> findById(UUID customerId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from order_mgmt.customers WHERE customer_id = UNHEX(REPLACE(:customerId, '-', ''))",
                    Collections.singletonMap("customerId", customerId.toString().getBytes()),
                    customerRowMapper));
        } catch (EmptyResultDataAccessException e) {
            logger.error("Got empty result", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from order_mgmt.customers WHERE name=:name",
                    Collections.singletonMap("name", name),
                    customerRowMapper));
        } catch (EmptyResultDataAccessException e) {
            logger.error("Got empty result", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from order_mgmt.customers WHERE email=:email",
                    Collections.singletonMap("email", email),
                    customerRowMapper));
        } catch (EmptyResultDataAccessException e) {
            logger.error("Got empty result", e);
            return Optional.empty();
        }
    }

//    public void testTransaction(Customer customer){
//        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
//            @Override
//            protected void doInTransactionWithoutResult(TransactionStatus status) {
//                jdbcTemplate.update("update order_mgmt.customers set name =:name where customer_id=UNHEX(REPLACE(:customerId, '-', ''))",toParamMap(customer));
//                jdbcTemplate.update("update order_mgmt.customers set email =:email where customer_id=UNHEX(REPLACE(:customerId, '-', ''))",toParamMap(customer));
//            }
//        });
//    }

    @Override
    public void deleteAll() {
//        jdbcTemplate.getJdbcTemplate().update("delete from order_mgmt.customers");
        jdbcTemplate.update("delete from order_mgmt.customers",Collections.emptyMap());
    }

    static UUID toUUID(byte[] bytes) {
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }
}
