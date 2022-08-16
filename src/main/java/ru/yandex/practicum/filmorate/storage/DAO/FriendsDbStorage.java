package ru.yandex.practicum.filmorate.storage.DAO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.RowMappers.UserRowMapper;

import java.util.Collection;

@Repository
@Slf4j
public class FriendsDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addFriend(Long userId, Long friendId) {
        checkUserId(userId);
        checkUserId(friendId);
        if (!isFriendExist(userId, friendId)) {
            jdbcTemplate.update("INSERT INTO FRIENDS VALUES ( ?, ? )", userId, friendId);
        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        checkUserId(userId);
        checkUserId(friendId);
        if (!isFriendExist(userId, friendId)) {
            throw new ObjectNotFoundException("Ошибка: отсутствие дружбы между указанными пользователями");
        }
        jdbcTemplate.update("DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?", userId, friendId);
    }

    public Collection<User> findFriendsIntersection(Long userId, Long friendId) {
        checkUserId(userId);
        checkUserId(friendId);
        return jdbcTemplate.query(
                "SELECT * FROM USERS " +
                "JOIN FRIENDS F on USERS.USER_ID = F.FRIEND_ID " +
                "JOIN FRIENDS F2 on F.FRIEND_ID = F2.FRIEND_ID " +
                "WHERE F.USER_ID = ? AND F2.USER_ID = ?",
                new UserRowMapper(), userId, friendId);
    }

    public Collection<User> getUserFriends(Long userId) {
        checkUserId(userId);
        return jdbcTemplate.query("SELECT * FROM USERS " +
                "join FRIENDS F on USERS.USER_ID = F.FRIEND_ID " +
                        "WHERE F.USER_ID = ?",
                new UserRowMapper(), userId);
    }

    private boolean isFriendExist(Long userId, Long friendId) {
        String sql = "SELECT COUNT(USER_ID) FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        Byte countLike = jdbcTemplate.queryForObject(sql, Byte.class, userId, friendId);
        return countLike > 0;
    }

    private void checkUserId(Long userId) {
        String sql = "SELECT COUNT(USER_ID) FROM USERS WHERE USER_ID = ?";
        Byte countLike = jdbcTemplate.queryForObject(sql, Byte.class, userId);
        if (countLike == 0) {
            throw new ObjectNotFoundException(String.format("Ошибка: пользователя с id %d не существует", userId));
        }
    }
}
