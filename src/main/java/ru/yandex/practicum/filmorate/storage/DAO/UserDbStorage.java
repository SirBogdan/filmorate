package ru.yandex.practicum.filmorate.storage.DAO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.RowMappers.UserRowMapper;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;

@Repository
@Qualifier
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUserById(long id) {
        checkUserExist(id);
        return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE USER_ID = ?", new UserRowMapper(), id);
    }

    @Override
    public Collection<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", new UserRowMapper());
    }

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO USERS (USER_EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY) values (?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"USER_ID"});
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
    }

    @Override
    public User putUser(User user) {
        checkUserExist(user.getId());
        String sql = "UPDATE users SET USER_EMAIL = ?, USER_LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    private void checkUserExist(Long userId) {
        Byte countUser = jdbcTemplate.queryForObject("SELECT COUNT (USER_ID) FROM USERS WHERE USER_ID = ?",
                Byte.class, userId);
        if (countUser == 0) {
            log.debug("Ошибка обновления пользователя: пользователь не найден");
            throw new ObjectNotFoundException("Ошибка обновления пользователя: пользователь не найден");
        }
    }
}
