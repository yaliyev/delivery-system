package de.yagub.deliverysystem.msorder.repository;

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
                .withTableName("orders")
                .usingColumns("id", "customer_id", "total_amount", "status", "created_at", "updated_at");
    }

    private final RowMapper<Order> orderRowMapper = (rs, rowNum) -> {
        Order o = new Order();
        o.setId(rs.getString("id"));
        o.setCustomerId(rs.getString("customer_id"));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setStatus(OrderStatus.valueOf(rs.getString("status")));
        o.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        o.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return o;
    };

    private final RowMapper<OrderItem> itemRowMapper = (rs, rowNum) -> {
        OrderItem i = new OrderItem();
        i.setOrderId(rs.getString("order_id"));
        i.setProductId(rs.getString("product_id"));
        i.setQuantity(rs.getInt("quantity"));
        i.setPricePerUnit(rs.getBigDecimal("price_per_unit"));
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
            params.put("id", newId);
            params.put("customer_id", order.getCustomerId());
            params.put("total_amount", order.getTotalAmount());
            params.put("status", order.getStatus().name());
            params.put("created_at", Timestamp.valueOf(order.getCreatedAt()));
            params.put("updated_at", Timestamp.valueOf(order.getUpdatedAt()));

            orderInserter.execute(params);
        } else {
            // Update existing order
            jdbc.update(
                    "UPDATE orders SET customer_id=?, total_amount=?, status=?, updated_at=? WHERE id=?",
                    order.getCustomerId(),
                    order.getTotalAmount(),
                    order.getStatus().name(),
                    Timestamp.valueOf(order.getUpdatedAt()),
                    order.getId()
            );
            jdbc.update("DELETE FROM order_items WHERE order_id=?", order.getId());
        }

        // Insert order items
        if (!order.getItems().isEmpty()) {
            jdbc.batchUpdate(
                    "INSERT INTO order_items(order_id, product_id, quantity, price_per_unit) VALUES (?,?,?,?)",
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
            Order order = jdbc.queryForObject("SELECT * FROM orders WHERE id = ?", orderRowMapper, id);
            if (order != null) {
                order.setItems(getOrderItems(id));
            }
            return Optional.ofNullable(order);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Order> findByCustomerId(String customerId) {
        List<Order> orders = jdbc.query(
                "SELECT * FROM orders WHERE customer_id = ?",
                orderRowMapper,
                customerId
        );
        orders.forEach(order -> order.setItems(getOrderItems(order.getId())));
        return orders;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = jdbc.query("SELECT * FROM orders", orderRowMapper);
        orders.forEach(order -> order.setItems(getOrderItems(order.getId())));
        return orders;
    }

    private List<OrderItem> getOrderItems(String orderId) {
        return jdbc.query(
                "SELECT * FROM order_items WHERE order_id = ?",
                itemRowMapper,
                orderId
        );
    }
}
