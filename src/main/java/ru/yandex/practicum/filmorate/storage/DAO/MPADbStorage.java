package ru.yandex.practicum.filmorate.storage.DAO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.RowMappers.MPARowMapper;

import java.util.Collection;

@Repository
@Slf4j
public class MPADbStorage {
    private final JdbcTemplate jdbcTemplate;

    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MPA getMPAById(long id) {
        if (id<= 0 || id > 5) {
            throw new ObjectNotFoundException("Ошибка: MPA с запрашиваемым id не существует");
        }
        return jdbcTemplate.queryForObject("SELECT * FROM MPA WHERE MPA_ID = ?", new MPARowMapper(), id);
    }

    public Collection<MPA> getAllMPA() {
        return jdbcTemplate.query("SELECT * FROM MPA", new MPARowMapper());
    }

}
