package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final List<User> users = new ArrayList<>();
    private int idCreator = 0;

    @GetMapping
    public List<User> findAll() {
        return users;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        if (!isUserValid(user)) {
            log.debug("Ошибка валидации при создании пользователя");
            throw new ValidationException("Ошибка валидации при создании пользователя");
        }
        user.setId(createNewId());
        users.add(user);
        log.debug("Пользователь с логином {} успешно дообавлен", user.getLogin());
        return user;
    }

    @PutMapping
    public User putUser(@RequestBody User user) {
        boolean containsId = false;
        for (User userEach : users) {
            if (user.getId() == userEach.getId()) {
                containsId = true;
                break;
            }
        }
        if (!isUserValid(user) || !containsId) {
            log.debug("Ошибка валидации при обновлении пользователя");
            throw new ValidationException("Ошибка валидации при обнолвении пользователя");
        }
        users.remove(user.getId() - 1);
        users.add(user.getId() - 1, user);
        log.debug("Пользователь с логином {} успешно обновлен", user.getLogin());
        return user;
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

    private int createNewId() {
        return ++idCreator;
    }
}
