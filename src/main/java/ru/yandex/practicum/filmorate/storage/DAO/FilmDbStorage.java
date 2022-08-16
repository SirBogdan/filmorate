package ru.yandex.practicum.filmorate.storage.DAO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.RowMappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.RowMappers.GenreRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.*;

@Repository
@Qualifier
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film getFilmById(long id) {
        checkFilmExists(id);
        Film film = jdbcTemplate.queryForObject(
                "SELECT * FROM FILMS " +
                        "JOIN MPA M on M.MPA_ID = FILMS.MPA_ID" +
                        " WHERE FILM_ID = ?", new FilmRowMapper(), id);
        loadFilmGenre(film);
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        List<Film> filmList = jdbcTemplate.query(
                "SELECT * FROM FILMS " +
                        "JOIN MPA M on M.MPA_ID = FILMS.MPA_ID", new FilmRowMapper());
        loadFilmGenre(filmList);
        return filmList;
    }

    @Override
    public Film createFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO FILMS (FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA_ID) " +
                            "values (?,?,?,?,?,?)", new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(releaseDate));
            }
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            stmt.setLong(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        film.setId(keyHolder.getKey().longValue());
        setFilmGenre(film);
        return film;
    }

    @Override
    public Film putFilm(Film film) {
        checkFilmExists(film.getId());
        jdbcTemplate.update("UPDATE FILMS " +
                        "SET FILM_NAME = ?, FILM_DESCRIPTION = ?, RELEASE_DATE = ?, " +
                        "DURATION = ?, RATE = ?, MPA_ID = ? " +
                        "WHERE FILM_ID = ?",
                film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getRate(), film.getMpa().getId(), film.getId());
        setFilmGenre(film);
        return film;
    }

    private void checkFilmExists(long filmId) {
        Byte countFilm = jdbcTemplate.queryForObject("SELECT COUNT (FILM_ID) FROM FILMS WHERE FILM_ID = ?",
                Byte.class, filmId);
        if (countFilm == 0) {
            log.debug("Ошибка обновления фильма: фильм не найден");
            throw new ObjectNotFoundException("Ошибка обновления фильма: фильм не найден");
        }
    }

    private void setFilmGenre(Film film) {
        jdbcTemplate.update("DELETE FROM FILM_GENRES WHERE  FILM_ID = ?", film.getId());
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO FILM_GENRES VALUES ( ?,? )", film.getId(), genre.getId());
            }
        }
    }

    private void loadFilmGenre(Film film) {
        List<Genre> genreList = jdbcTemplate.query("SELECT * FROM GENRES " +
                "JOIN FILM_GENRES FG on GENRES.GENRE_ID = FG.GENRE_ID " +
                "JOIN FILMS F on F.FILM_ID = FG.FILM_ID " +
                "WHERE F.FILM_ID = ?", new GenreRowMapper(), film.getId());
        film.setGenres(new HashSet<>(genreList));
    }

    private void loadFilmGenre(Collection<Film> films) {
        for (Film film : films) {
            loadFilmGenre(film);
        }
    }
}
