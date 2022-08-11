package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DAO.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmorateApplicationTests {

    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;
    private final FriendsDbStorage friendsDbStorage;
    private final LikeDbStorage likeDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final MPADbStorage mpaDbStorage;

    @Test
    @Order(1)
    public void userDbStorageTest() {
        User user1 = new User(1L, "user1@mail.ru", "user1Login", "user1Name",
                LocalDate.of(2001, 1, 1));
        User user2 = new User(2L, "user2@mail.ru", "user2Login", "user2Name",
                LocalDate.of(2002, 1, 1));
        User user1Changed = new User(1L, "user1Changed@mail.ru", "user1ChangedLogin",
                "user3ChangedName", LocalDate.of(2001, 2, 1));
        User user3 = new User(3L, "user3@mail.ru", "user3Login", "user3Name",
                LocalDate.of(2003, 1, 1));

        userDbStorage.createUser(user1);

        assertEquals(user1, userDbStorage.getUserById(1)); //тест методов createUser и getUserById

        userDbStorage.createUser(user2);
        Collection<User> userCollection = List.of(user1, user2);

        assertEquals(userCollection, userDbStorage.findAll()); // тест метода findAll

        userDbStorage.putUser(user1Changed);

        assertEquals(user1Changed, userDbStorage.getUserById(1)); // тест метода putUser

        friendsDbStorage.addFriend(1L, 2L);

        assertEquals(List.of(user2), friendsDbStorage.getUserFriends(1L)); //тест методов addFriend и getUserFriends

        friendsDbStorage.deleteFriend(1L, 2L);

        assertEquals(List.of(), friendsDbStorage.getUserFriends(1L)); // тест метода getUserFriends

        userDbStorage.createUser(user3);
        friendsDbStorage.addFriend(1L, 3L);
        friendsDbStorage.addFriend(2L, 3L);

        // тест метода findFriendsIntersection
        assertEquals(List.of(user3), friendsDbStorage.findFriendsIntersection(1L, 2L));
    }

    @Test
    @Order(2)
    public void filmDbStorageTest() {
        Film film1 = new Film(1L, "film1Name", "film1Description",
                LocalDate.of(2001, 1, 1), 100, 1, new MPA(1, "Комедия"));
        Film film2 = new Film(2L, "film2Name", "film2Description",
                LocalDate.of(2002, 1, 1), 200, 2, new MPA(2, "Драма"));
        Film film1Changed = new Film(1L, "film1film1ChangedName", "film1film1ChangedDescription",
                LocalDate.of(2001, 2, 1), 101, 3, new MPA(3, "Мультфильм"));

        filmDbStorage.createFilm(film1);

        assertEquals(film1, filmDbStorage.getFilmById(1)); // тест методов createFilm и getFilmById

        filmDbStorage.createFilm(film2);
        Collection<Film> filmCollection = List.of(film1, film2);

        assertEquals(filmCollection, filmDbStorage.findAll()); // тест метода findAll

        filmDbStorage.putFilm(film1Changed);

        assertEquals(film1Changed, filmDbStorage.getFilmById(1)); // тест метода putFilm
    }

    @Test
    @Order(3)
    public void genreDbStorageTest() {
        Collection<Genre> genreCollection = List.of(
                new Genre(1L, "Комедия"),
                new Genre(2L, "Драма"),
                new Genre(3L, "Мультфильм"),
                new Genre(4L, "Триллер"),
                new Genre(5L, "Документальный"),
                new Genre(6L, "Боевик"));

        assertEquals(genreCollection, genreDbStorage.getAllGenres()); //тест метода getAllGenres
        assertEquals(new Genre(2L, "Драма"), genreDbStorage.getGenreById(2L)); //тест метода getGenreById
    }

    @Test
    @Order(4)
    public void mpaDbStorageTest() {
        Collection<MPA> mpaCollection = List.of(
                new MPA(1L, "G"),
                new MPA(2L, "PG"),
                new MPA(3L, "PG-13"),
                new MPA(4L, "R"),
                new MPA(5L, "NC-17"));

        assertEquals(mpaCollection, mpaDbStorage.getAllMPA()); // тест метода getAllMPA
        assertEquals(new MPA(3L, "PG-13"), mpaDbStorage.getMPAById(3L));  // тест метода getMPAById
    }

    @Test
    @Order(10)
    public void likeDbStorageTest() {
        Film film1Changed = likeDbStorage.findMostLikedFilms(1).stream().findAny().get();
        assertEquals(1L, film1Changed.getId()); // тест метода findMostLikedFilms


        likeDbStorage.addLike(1L, 1L);
        assertEquals(4, filmDbStorage.getFilmById(1).getRate()); // тест метода addLike

        likeDbStorage.deleteLike(1L, 1L);
        assertEquals(3, filmDbStorage.getFilmById(1).getRate()); // тест метода deleteLike
    }

}
