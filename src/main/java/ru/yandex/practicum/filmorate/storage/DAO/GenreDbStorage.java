package ru.yandex.practicum.filmorate.storage.DAO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.RowMappers.GenreRowMapper;

import java.util.Collection;
import java.util.List;

@Repository
@Qualifier
@Slf4j
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre getGenreById(long id) {
        List<Genre> genre = jdbcTemplate.query("SELECT * FROM GENRES WHERE GENRE_ID = ?", new GenreRowMapper(), id);
        if (genre.size() == 0) {
            throw new ObjectNotFoundException("Ошибка: жанра с запрашиваемым id не существует");
        }
        return genre.get(0);
    }

    public Collection<Genre> getAllGenres() {
        return jdbcTemplate.query("SELECT * FROM GENRES", new GenreRowMapper());
    }
}
