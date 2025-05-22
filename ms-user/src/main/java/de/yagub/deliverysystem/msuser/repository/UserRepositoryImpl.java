package de.yagub.deliverysystem.msuser.repository;

import de.yagub.deliverysystem.msuser.model.User;
import de.yagub.deliverysystem.msuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (rs, rowNum) ->
            new User(
                    rs.getLong("id"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getBoolean("enabled")
            );

    @Override
    public User save(User user) {
        // For Oracle using sequence
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO users (id, username, password_hash, enabled) " +
                "VALUES (users_seq.NEXTVAL, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setBoolean(3, user.isEnabled());
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();

        return new User(
                generatedId,
                user.getUsername(),
                user.getPasswordHash(),
                user.isEnabled()
        );
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, userRowMapper, username)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }
}


