package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Long, Film> films = new HashMap<>();
    private int idCreator = 0;

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(++idCreator);
        films.put(film.getId(), film);
        log.debug("Фильм с названием {} успешно дообавлен", film.getName());
        return film;
    }

    @Override
    public Film putFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.debug("Ошибка обновления фильма: фильм не найден");
            throw new ObjectNotFoundException("Ошибка обновления фильма: фильм не найден");
        }
        films.put(film.getId(), film);
        log.debug("Фильм с названием {} успешно обновлен", film.getName());
        return film;
    }
}
