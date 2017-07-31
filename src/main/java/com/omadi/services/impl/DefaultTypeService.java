package com.omadi.services.impl;

import com.omadi.entities.Type;
import com.omadi.repository.TypeRepository;
import com.omadi.services.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultTypeService implements TypeService {
    @Autowired
    private TypeRepository typeRepository;

    @Override
    public void save(Type type) {
        typeRepository.save(type);
    }

    @Override
    public List<Type> getAll() {
        int done = 0;
        return typeRepository.findAllByDone(done);
    }
}
