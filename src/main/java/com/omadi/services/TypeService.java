package com.omadi.services;

import com.omadi.entities.Type;

import javax.transaction.Transactional;
import java.util.List;

public interface TypeService {
    List<Type> getAll();

    @Transactional
    void save(Type type);
}
