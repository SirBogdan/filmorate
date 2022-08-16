package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User getUserById(long id);

    Collection<User> findAll();

    User createUser(User user);

    User putUser(User user);
}