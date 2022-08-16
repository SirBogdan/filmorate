package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class MPA {
    private long id;
    private String name;
}
