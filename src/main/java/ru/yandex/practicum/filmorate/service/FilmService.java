package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film createFilm(Film film) {
        if (!isFilmValid(film)) {
            log.debug("Ошибка валидации при создании фильма");
            throw new ValidationException("Ошибка валидации при создании фильма");
        }
        return filmStorage.createFilm(film);
    }

    public Film putFilm(Film film) {
        if (!isFilmValid(film)) {
            log.debug("Ошибка валидации при обновлении фильма");
            throw new ValidationException("Ошибка валидации при обновлении фильма");
        }
        return filmStorage.putFilm(film);
    }

    public Film findFilmById(Long filmId) {
        return filmStorage.findAll().stream()
                .filter(film -> Objects.equals(film.getId(), filmId))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Фильм с id %d не найден", filmId)));
    }

    public void addLike(Long filmId, Long userId) {
        Film film = findFilmById(filmId);
        film.getLikes().add(userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = findFilmById(filmId);
        if (!film.getLikes().remove(userId)) {
            throw new ObjectNotFoundException(String.format("Лайк от пользователя с id %d отсутствует", userId));
        }
    }

    public Collection<Film> findMostLikedFilms(Integer count) {
        return filmStorage.findAll().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count).collect(Collectors.toList());
    }

    private boolean isFilmValid(Film film) {
        boolean validName = !film.getName().isBlank() && film.getName() != null;
        boolean validDescription = film.getDescription().length() <= 200 && film.getDescription() != null;
        boolean validReleaseDate = film.getReleaseDate().isAfter(MIN_RELEASE_DATE);
        boolean validDuration = film.getDuration() >= 0;
        return validName && validDescription && validReleaseDate && validDuration;
    }
}
