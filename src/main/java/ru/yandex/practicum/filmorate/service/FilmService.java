package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.DAO.LikeDbStorage;

import java.time.LocalDate;
import java.util.Collection;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeDbStorage likeDbStorage;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage, LikeDbStorage likeDbStorage) {
        this.filmStorage = filmStorage;
        this.likeDbStorage = likeDbStorage;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film createFilm(Film film) {
        if (!isFilmValid(film)) {
            throw new ValidationException("Ошибка валидации при создании фильма");
        }
        return filmStorage.createFilm(film);
    }

    public Film putFilm(Film film) {
        if (!isFilmValid(film)) {
            throw new ValidationException("Ошибка валидации при обновлении фильма");
        }
        return filmStorage.putFilm(film);
    }

    public Film findFilmById(Long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public void addLike(Long filmId, Long userId) {
        likeDbStorage.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        likeDbStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> findMostLikedFilms(Integer count) {
        return likeDbStorage.findMostLikedFilms(count);
    }

    private boolean isFilmValid(Film film) {
        boolean validName = !film.getName().isBlank() && film.getName() != null;
        boolean validDescription = film.getDescription().length() <= 200 && film.getDescription() != null;
        boolean validReleaseDate = film.getReleaseDate().isAfter(MIN_RELEASE_DATE);
        boolean validDuration = film.getDuration() >= 0;
        boolean validMPA = film.getMpa() != null;
        return validName && validDescription && validReleaseDate && validDuration && validMPA;
    }
}
