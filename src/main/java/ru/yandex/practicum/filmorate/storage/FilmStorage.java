package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film getFilmById(long id);

    Collection<Film> findAll();

    Film createFilm(Film film);

    Film putFilm(Film film);
}
