package ru.yandex.practicum.filmorate.storage.DAO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.RowMappers.GenreRowMapper;

import java.util.Collection;

@Repository
@Qualifier
@Slf4j
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre getGenreById(long id) {
        if (id <= 0 || id > 6) {
            throw new ObjectNotFoundException("Ошибка: жанра с запрашиваемым id не существует");
        }
        return jdbcTemplate.queryForObject("SELECT * FROM GENRES WHERE GENRE_ID = ?", new GenreRowMapper(), id);
    }

    public Collection<Genre> getAllGenres() {
        return jdbcTemplate.query("SELECT * FROM GENRES", new GenreRowMapper());
    }
}
