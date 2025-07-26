package de.yagub.deliverysystem.msreportconsumer.repository;

import de.yagub.deliverysystem.msreportconsumer.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@RequiredArgsConstructor
@Repository
public class ResponseRepositoryImpl implements ResponseRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Response save(Response response) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO responses (request_id) VALUES (?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, response.getRequestId());
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();

        return new Response(
                generatedId,
                response.getRequestId()
        );
    }
}
