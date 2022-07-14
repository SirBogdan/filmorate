package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User createUser(User user) {
        if (!isUserValid(user)) {
            log.debug("Ошибка валидации при создании пользователя");
            throw new ValidationException("Ошибка валидации при создании пользователя");
        }
        return userStorage.createUser(user);
    }

    public User putUser(User user) {
        if (!isUserValid(user)) {
            log.debug("Ошибка валидации при обновлении пользователя");
            throw new ValidationException("Ошибка валидации при обновлении пользователя");
        }
        return userStorage.putUser(user);
    }

    public User findUserById(Long userId) {
        return userStorage.findAll().stream()
                .filter(u -> Objects.equals(u.getId(), userId))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Пользователь с id %d не найден", userId)));
    }

    public void addFriend(Long userId, Long friendId) {
        User user = findUserById(userId);
        User friend = findUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = findUserById(userId);
        User friend = findUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public Collection<User> findFriendsIntersection(Long userId, Long friendId) {
        User user = findUserById(userId);
        User friend = findUserById(friendId);
        Set<Long> resultIds = new HashSet<>(user.getFriends());
        resultIds.retainAll(friend.getFriends());
        return resultIds.stream().map(this::findUserById).collect(Collectors.toList());
    }

    public Collection<User> getUserFriends(Long userId) {
        return findUserById(userId).getFriends().stream().map(this::findUserById).collect(Collectors.toList());
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
