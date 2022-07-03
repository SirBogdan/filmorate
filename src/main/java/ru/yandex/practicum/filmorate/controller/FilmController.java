package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final List<Film> films = new ArrayList<>();
    private int idCreator = 0;

    @GetMapping
    public List<Film> findAll() {
        return films;
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        if (!isFilmValid(film)) {
            log.debug("Ошибка валидации при создании фильма");
            throw new ValidationException("Ошибка валидации при создании фильма");
        }
        film.setId(createNewId());
        films.add(film);
        log.debug("Пользователь с названием {} успешно дообавлен", film.getName());
        return film;
    }

    @PutMapping
    public Film putUser(@RequestBody Film film) {
        boolean containsId = false;
        for(Film filmEach : films) {
            if(film.getId() == filmEach.getId()) {
                containsId = true;
                break;
            }
        }
        if (!isFilmValid(film) || !containsId) {
            log.debug("Ошибка валидации при обновлении фильма");
            throw new ValidationException("Ошибка валидации при обновлении фильма");
        }
        films.remove(film.getId()-1);
        films.add(film.getId()-1, film);
        log.debug("Пользователь с названием {} успешно обновлен", film.getName());
        return film;
    }

    private boolean isFilmValid(Film film) {
        boolean validName = !film.getName().isBlank();
        boolean validLength = film.getDescription().length() <= 200;
        boolean validReleaseDate = film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28));
        boolean validDuration = film.getDuration() >= 0;
        return validName && validLength && validReleaseDate && validDuration;
    }

    private int createNewId() {
        return ++idCreator;
    }
}
