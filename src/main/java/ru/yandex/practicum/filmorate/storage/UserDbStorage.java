package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
@Qualifier
public class UserDbStorage implements UserStorage{

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUserById(long id) {
        User user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE USER_ID = ?", new UserRowMapper(), id);
        System.out.println(user);
        System.out.println(user.getName());
        System.out.println(user.getId());
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE USER_ID = ?", new UserRowMapper(), id);

    }

    @Override
    public Collection<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", new UserRowMapper());
    }

    @Override
    public User createUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        System.out.println(user.getName());
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                              "INSERT INTO users (email, login, user_name, birthday) values (?,?,?,?)",
                    new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        return user;

/*        String sqlQuery = "insert into users (email, login, user_name, birthday) " +
                "values (?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setId(1L);
        return  user;*/

    }

    @Override
    public User putUser(User user) {
        jdbcTemplate.update("UPDATE users SET email = ?, login = ?, user_name = ?, birthday = ? WHERE user_id = ?",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getLong("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("BIRTHDAY").toLocalDate()
        );
    }
}
