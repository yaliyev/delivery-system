package de.yagub.deliverysystem.mswallet.repository;

import de.yagub.deliverysystem.mswallet.model.Wallet;
import de.yagub.deliverysystem.mswallet.model.WalletStatus;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class WalletRepositoryImpl implements WalletRepository{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertWallet;

    public WalletRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertWallet = new SimpleJdbcInsert(dataSource)
                .withTableName("wallets")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Wallet create(Wallet wallet) {
        Map<String, Object> params = Map.of(
                "user_id", wallet.getUserId(),
                "balance", wallet.getBalance(),
                "currency", wallet.getCurrency(),
                "status", wallet.getStatus().name(),
                "created_at", LocalDateTime.now(),
                "updated_at", LocalDateTime.now(),
                "version", 0L
        );

        Number id = insertWallet.executeAndReturnKey(params);
        wallet.setId(id.longValue());
        return wallet;
    }

    @Override
    public List<Wallet> findAll(int limit, int offset) {
        String sql = """
        SELECT id, user_id, balance, currency, 
               created_at, updated_at, status, version
        FROM wallets
        ORDER BY id
        LIMIT ? OFFSET ?""";

        return jdbcTemplate.query(sql, new WalletRowMapper(), limit, offset);
    }

    @Override
    @Transactional
    public Optional<Wallet> update(Wallet wallet) {
        // First execute the update with version check
        int updatedRows = jdbcTemplate.update(
                """
                UPDATE wallets
                SET user_id = ?,
                    balance = ?,
                    currency = ?,
                    status = ?,
                    updated_at = ?,
                    version = version + 1
                WHERE id = ? AND version = ?
                """,
                wallet.getUserId(),
                wallet.getBalance(),
                wallet.getCurrency(),
                wallet.getStatus().toString(),
                LocalDateTime.now(),
                wallet.getId(),
                wallet.getVersion()
        );

        // If update was successful, fetch and return the updated wallet
        if (updatedRows > 0) {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM wallets WHERE id = ?",
                            new WalletRowMapper(),
                            wallet.getId()
                    )
            );
        }
        return Optional.empty();
    }

    @Override
    public Optional<Wallet> findById(Long id) {
        String sql = "SELECT * FROM wallets WHERE id = ?";
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, new WalletRowMapper(), id)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Wallet> findByUserId(Long userId) {
        String sql = """
            SELECT id, user_id, balance, currency, 
                   created_at, updated_at, status, version
            FROM wallets
            WHERE user_id = ?""";

        try {
            Wallet wallet = jdbcTemplate.queryForObject(sql, new WalletRowMapper(), userId);
            return Optional.ofNullable(wallet);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public int updateBalance(Long walletId, BigDecimal amount) {
        String sql = """
        UPDATE wallets 
        SET balance = balance + ?, 
            updated_at = CURRENT_TIMESTAMP,
            version = version + 1
        WHERE id = ?""";
        return jdbcTemplate.update(sql, amount, walletId);
    }

    private static class WalletRowMapper implements RowMapper<Wallet> {
        @Override
        public Wallet mapRow(ResultSet rs, int rowNum) throws SQLException {
            Wallet wallet = new Wallet();
            wallet.setId(rs.getLong("id"));
            wallet.setUserId(rs.getLong("user_id"));
            wallet.setBalance(rs.getBigDecimal("balance"));
            wallet.setCurrency(rs.getString("currency"));
            wallet.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            wallet.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

            wallet.setStatus(WalletStatus.valueOf(rs.getString("status")));

            wallet.setVersion(rs.getLong("version"));
            return wallet;
        }
    }
}
