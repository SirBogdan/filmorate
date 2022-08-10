package ru.yandex.practicum.filmorate.storage.DAO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.RowMappers.FilmRowMapper;

import java.util.Collection;

@Repository
@Qualifier
@Slf4j
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        if (!isLikeExists(filmId, userId)) {
            jdbcTemplate.update("INSERT INTO LIKES VALUES (?, ?)", filmId, userId);
            jdbcTemplate.update("UPDATE FILMS SET RATE = RATE + 1 WHERE FILM_ID = ?", filmId);
        }
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        if (!isLikeExists(filmId, userId)) {
            throw new ObjectNotFoundException(String.format("Лайк от пользователя с id %d отсутствует", userId));
        }
        jdbcTemplate.update("DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?", filmId, userId);
        jdbcTemplate.update("UPDATE FILMS SET RATE = RATE - 1 WHERE FILM_ID = ?", filmId);
    }

    @Override
    public Collection<Film> findMostLikedFilms(Integer count) {
        return jdbcTemplate.query("SELECT * FROM FILMS " +
                "JOIN MPA M on M.MPA_ID = FILMS.MPA_ID " +
                "ORDER BY RATE DESC " +
                "LIMIT(?)", new FilmRowMapper(), count);
    }

    private boolean isLikeExists(Long filmId, Long userId) {
        String sql = "SELECT COUNT(FILM_ID) FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        Byte countLike = jdbcTemplate.queryForObject(sql, Byte.class, filmId, userId);
        return countLike > 0;
    }
}
