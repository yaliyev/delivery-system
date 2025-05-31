package de.yagub.deliverysystem.msorder.repository;

import de.yagub.deliverysystem.msorder.model.Promocode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class PromocodeRepositoryImpl implements PromocodeRepository {

    private final JdbcTemplate jdbc;

    private final RowMapper<Promocode> promocodeRowMapper = (rs, rowNum) ->
            Promocode.builder()
                    .promocode(rs.getString("PROMOCODE"))  // UPPERCASE
                    .isUsed(rs.getBoolean("IS_USED"))      // UPPERCASE
                    .amount(rs.getBigDecimal("AMOUNT"))   // UPPERCASE
                    .build();

    @Override
    public Optional<Promocode> findByCode(String promocode) {
        try {
            Promocode code = jdbc.queryForObject(
                    "SELECT PROMOCODE, IS_USED, AMOUNT FROM PROMOCODES WHERE PROMOCODE = ?",  // UPPERCASE
                    promocodeRowMapper,
                    promocode
            );
            return Optional.ofNullable(code);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }



    @Override
    public boolean markAsUsed(String promocode) {
        int updated = jdbc.update(
                "UPDATE PROMOCODES SET IS_USED = 1 WHERE PROMOCODE = ? AND IS_USED = 0",  // UPPERCASE
                promocode
        );
        return updated == 1;
    }
}