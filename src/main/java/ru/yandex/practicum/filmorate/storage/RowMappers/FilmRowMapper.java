package ru.yandex.practicum.filmorate.storage.RowMappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Film(
                rs.getLong("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("FILM_DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                rs.getInt("RATE"),
                new MPA(rs.getLong("MPA.MPA_ID"), rs.getString("MPA.MPA_NAME"))
        );
    }
}
