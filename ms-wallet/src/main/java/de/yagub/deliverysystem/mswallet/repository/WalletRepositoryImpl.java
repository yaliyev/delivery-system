package de.yagub.deliverysystem.mswallet.repository;

import de.yagub.deliverysystem.mswallet.error.UserNotFoundException;
import de.yagub.deliverysystem.mswallet.model.Wallet;
import de.yagub.deliverysystem.mswallet.model.WalletStatus;
import oracle.jdbc.OracleDatabaseException;
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
public class WalletRepositoryImpl implements WalletRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertWallet;

    public WalletRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertWallet = new SimpleJdbcInsert(dataSource)
                .withTableName("WALLETS")
                .usingGeneratedKeyColumns("ID")
                .usingColumns("USER_ID", "BALANCE", "CURRENCY", "STATUS",
                        "CREATED_AT", "UPDATED_AT", "VERSION");
    }

    @Override
    public Wallet create(Wallet wallet) {
        Map<String, Object> params = new HashMap<>();
        params.put("USER_ID", wallet.getUserId());
        params.put("BALANCE", wallet.getBalance());
        params.put("CURRENCY", wallet.getCurrency());
        params.put("STATUS", wallet.getStatus().name());
        params.put("CREATED_AT", LocalDateTime.now());
        params.put("UPDATED_AT", LocalDateTime.now());
        params.put("VERSION", 0L);

        Number id = null;
        try {
            id = insertWallet.executeAndReturnKey(params);
        }catch (Exception exception){

            if(exception.getMessage().contains("ORA-02291: integrity constraint (DELIVERY_SYSTEM.FK_WALLETS_USER_ID)")){
                 throw new UserNotFoundException("User with id: "+wallet.getUserId()+" doesn't exists.");
            }else{
               exception.getStackTrace();
            }

        }

        wallet.setId(id.longValue());
        return wallet;
    }

    @Override
    public List<Wallet> findAll(int limit, int offset) {
        String sql = """
            SELECT * FROM (
                SELECT a.*, ROWNUM rnum FROM (
                    SELECT ID, USER_ID, BALANCE, CURRENCY, 
                           CREATED_AT, UPDATED_AT, STATUS, VERSION
                    FROM WALLETS
                    ORDER BY ID
                ) a WHERE ROWNUM <= ?
            ) WHERE rnum > ?""";

        return jdbcTemplate.query(sql, new WalletRowMapper(), offset + limit, offset);
    }

    @Override
    @Transactional
    public Optional<Wallet> update(Wallet wallet) {
        int updatedRows = jdbcTemplate.update(
                """
                UPDATE WALLETS
                SET USER_ID = ?,
                    BALANCE = ?,
                    CURRENCY = ?,
                    STATUS = ?,
                    UPDATED_AT = ?,
                    VERSION = VERSION + 1
                WHERE ID = ? AND VERSION = ?
                """,
                wallet.getUserId(),
                wallet.getBalance(),
                wallet.getCurrency(),
                wallet.getStatus().toString(),
                LocalDateTime.now(),
                wallet.getId(),
                wallet.getVersion()
        );

        if (updatedRows > 0) {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM WALLETS WHERE ID = ?",
                            new WalletRowMapper(),
                            wallet.getId()
                    )
            );
        }
        return Optional.empty();
    }

    @Override
    public Optional<Wallet> findById(Long id) {
        String sql = "SELECT * FROM WALLETS WHERE ID = ?";
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
            SELECT ID, USER_ID, BALANCE, CURRENCY, 
                   CREATED_AT, UPDATED_AT, STATUS, VERSION
            FROM WALLETS
            WHERE USER_ID = ?""";

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
            UPDATE WALLETS 
            SET BALANCE = BALANCE + ?, 
                UPDATED_AT = SYSTIMESTAMP,
                VERSION = VERSION + 1
            WHERE ID = ?""";
        return jdbcTemplate.update(sql, amount, walletId);
    }

    private static class WalletRowMapper implements RowMapper<Wallet> {
        @Override
        public Wallet mapRow(ResultSet rs, int rowNum) throws SQLException {
            Wallet wallet = new Wallet();
            wallet.setId(rs.getLong("ID"));
            wallet.setUserId(rs.getLong("USER_ID"));
            wallet.setBalance(rs.getBigDecimal("BALANCE"));
            wallet.setCurrency(rs.getString("CURRENCY"));
            wallet.setCreatedAt(rs.getTimestamp("CREATED_AT").toLocalDateTime());
            wallet.setUpdatedAt(rs.getTimestamp("UPDATED_AT").toLocalDateTime());
            wallet.setStatus(WalletStatus.valueOf(rs.getString("STATUS")));
            wallet.setVersion(rs.getLong("VERSION"));
            return wallet;
        }
    }
}