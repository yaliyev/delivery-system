package de.yagub.deliverysystem.msreportconsumer.repository;

import de.yagub.deliverysystem.msreportconsumer.model.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RequestRepositoryImpl implements RequestRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Request> requestRowMapper = (rs, rowNum) ->
            new Request(
                    rs.getLong("id"),
                    rs.getTimestamp("request_date").toLocalDateTime()
            );

    @Override
    public Request save(Request request) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO requests (request_date) VALUES (?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setTimestamp(1, Timestamp.valueOf(request.getRequestDate()));
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();

        return new Request(
                generatedId,
                request.getRequestDate()
        );
    }

    @Override
    public Optional<Request> findById(Long id) {
        String sql = "SELECT id, request_date FROM requests WHERE id = ?";
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, requestRowMapper, id)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
