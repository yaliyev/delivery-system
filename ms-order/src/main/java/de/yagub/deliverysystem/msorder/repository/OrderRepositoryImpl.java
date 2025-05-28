package de.yagub.deliverysystem.msorder.repository;

import de.yagub.deliverysystem.msorder.error.CustomerNotFoundException;
import de.yagub.deliverysystem.msorder.model.Order;
import de.yagub.deliverysystem.msorder.model.OrderItem;
import de.yagub.deliverysystem.msorder.model.OrderStatus;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert orderInserter;

    public OrderRepositoryImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.orderInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("ORDERS")
                .usingColumns("ID", "CUSTOMER_ID", "TOTAL_AMOUNT", "STATUS", "CREATED_AT", "UPDATED_AT");
    }

    private final RowMapper<Order> orderRowMapper = (rs, rowNum) -> {
        Order o = new Order();
        o.setId(rs.getString("ID"));
        o.setCustomerId(rs.getLong("CUSTOMER_ID"));
        o.setTotalAmount(rs.getBigDecimal("TOTAL_AMOUNT"));
        o.setStatus(OrderStatus.valueOf(rs.getString("STATUS")));
        o.setCreatedAt(rs.getTimestamp("CREATED_AT").toLocalDateTime());
        o.setUpdatedAt(rs.getTimestamp("UPDATED_AT").toLocalDateTime());
        return o;
    };

    private final RowMapper<OrderItem> itemRowMapper = (rs, rowNum) -> {
        OrderItem i = new OrderItem();
        i.setOrderId(rs.getString("ORDER_ID"));
        i.setProductId(rs.getString("PRODUCT_ID"));
        i.setQuantity(rs.getInt("QUANTITY"));
        i.setPricePerUnit(rs.getBigDecimal("PRICE_PER_UNIT"));
        return i;
    };

    @Override
    @Transactional
    public Order save(Order order) {
        if (order.getId() == null) {
            // Generate UUID for new order
            String newId = UUID.randomUUID().toString();
            order.setId(newId);

            // Insert new order with explicit ID
            Map<String, Object> params = new HashMap<>();
            params.put("ID", newId);
            params.put("CUSTOMER_ID", order.getCustomerId());
            params.put("TOTAL_AMOUNT", order.getTotalAmount());
            params.put("STATUS", order.getStatus().name());
            params.put("CREATED_AT", Timestamp.valueOf(order.getCreatedAt()));
            params.put("UPDATED_AT", Timestamp.valueOf(order.getUpdatedAt()));

            try {
                orderInserter.execute(params);
            }catch (Exception ex){
                if(ex.getMessage().contains("ORA-02291: integrity constraint (DELIVERY_SYSTEM.ORDERS_CUSTOMER_FK) violated")){
                    throw new CustomerNotFoundException("Customer with id: "+order.getCustomerId()+" doesn't exists.");
                }else{
                    ex.printStackTrace();
                }
            }

        } else {
            // Update existing order
            jdbc.update(Query.UPDATE.getQuery()
                    ,
                    order.getCustomerId(),
                    order.getTotalAmount(),
                    order.getStatus().name(),
                    Timestamp.valueOf(order.getUpdatedAt()),
                    order.getId()
            );
            jdbc.update(Query.DELETE_ORDER_ITEM.getQuery(), order.getId());
        }

        // Insert order items
        if (!order.getItems().isEmpty()) {
            jdbc.batchUpdate(
                    Query.INSERT_ORDER_ITEM.getQuery(),
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws java.sql.SQLException {
                            OrderItem item = order.getItems().get(i);
                            ps.setString(1, order.getId());
                            ps.setString(2, item.getProductId());
                            ps.setInt(3, item.getQuantity());
                            ps.setBigDecimal(4, item.getPricePerUnit());
                        }

                        @Override
                        public int getBatchSize() {
                            return order.getItems().size();
                        }
                    }
            );
        }

        return order;
    }

    @Override
    public Optional<Order> findById(String id) {
        try {
            Order order = jdbc.queryForObject(Query.FIND_BY_ID.getQuery(), orderRowMapper, id);
            if (order != null) {
                order.setItems(getOrderItems(id));
            }
            return Optional.ofNullable(order);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Order> findByCustomerId(Long customerId) {
        List<Order> orders = jdbc.query(
                Query.FIND_BY_CUSTOMER_ID.getQuery(),
                orderRowMapper,
                customerId
        );
        orders.forEach(order -> order.setItems(getOrderItems(order.getId())));
        return orders;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = jdbc.query(Query.FIND_ALL.getQuery(), orderRowMapper);
        orders.forEach(order -> order.setItems(getOrderItems(order.getId())));
        return orders;
    }

    private List<OrderItem> getOrderItems(String orderId) {
        return jdbc.query(
                Query.FIND_ALL_ORDER_ITEMS.getQuery(),
                itemRowMapper,
                orderId
        );
    }
}
