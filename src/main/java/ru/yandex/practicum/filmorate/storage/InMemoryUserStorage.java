package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> users = new HashMap<>();
    private int idCreator = 0;

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User createUser(User user) {
        user.setId(createNewId());
        users.put(user.getId(), user);
        log.debug("Пользователь с логином {} успешно добавлен", user.getLogin());
        return user;
    }

    @Override
    public User putUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.debug("Ошибка обновления пользователя: пользователь не найден");
            throw new ObjectNotFoundException("Ошибка обновления пользователя: пользователь не найден");
        }
        users.put(user.getId(), user);
        log.debug("Пользователь с логином {} успешно обновлен", user.getLogin());
        return user;
    }

    private int createNewId() {
        return ++idCreator;
    }
}
