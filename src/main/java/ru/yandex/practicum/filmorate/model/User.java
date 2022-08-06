package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")


public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;


    public User(Long id, String email, String login, String name, LocalDate birthday) {
    }

    // private Set<Long> friends = new HashSet<>();
}
