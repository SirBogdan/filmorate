package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.DAO.MPADbStorage;

import java.util.Collection;

@Service
public class MPAService {
    private final MPADbStorage mpaDbStorage;

    @Autowired
    public MPAService(MPADbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public MPA getMPAById(long id) {
        return mpaDbStorage.getMPAById(id);
    }

    public Collection<MPA> getAllMPA() {
        return mpaDbStorage.getAllMPA();
    }
}
