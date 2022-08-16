package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DAO.FriendsDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendsDbStorage friendsDbStorage;

    @Autowired
    public UserService(UserStorage userStorage, FriendsDbStorage friendsDbStorage) {
        this.userStorage = userStorage;
        this.friendsDbStorage = friendsDbStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User createUser(User user) {
        if (!isUserValid(user)) {
            throw new ValidationException("Ошибка валидации при создании пользователя");
        }
        return userStorage.createUser(user);
    }

    public User putUser(User user) {
        if (!isUserValid(user)) {
            throw new ValidationException("Ошибка валидации при обновлении пользователя");
        }
        return userStorage.putUser(user);
    }

    public User findUserById(Long userId) {
        return userStorage.getUserById(userId);
    }

    public void addFriend(Long userId, Long friendId) {
        friendsDbStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        friendsDbStorage.deleteFriend(userId, friendId);
    }

    public Collection<User> getUserFriends(Long userId) {
        return friendsDbStorage.getUserFriends(userId);
    }

    public Collection<User> findFriendsIntersection(Long userId, Long friendId) {
        return friendsDbStorage.findFriendsIntersection(userId, friendId);
    }

    private boolean isUserValid(User user) {
        boolean validEmail = !user.getEmail().isBlank() && user.getEmail().contains("@") && user.getEmail() != null;
        boolean validLogin = !user.getLogin().isBlank() && user.getLogin() != null;
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        boolean validBirthday = user.getBirthday().isBefore(LocalDate.now());
        return validEmail && validLogin && validBirthday;
    }
}
