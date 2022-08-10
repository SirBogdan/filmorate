package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MPAController {

    private final MPAService mpaService;

    @Autowired
    public MPAController(MPAService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public Collection<MPA> getAllMPA() {
        return mpaService.getAllMPA();
    }

    @GetMapping("/{id}")
    public MPA getMPAById(@PathVariable("id") long id) {
        return mpaService.getMPAById(id);
    }
}
