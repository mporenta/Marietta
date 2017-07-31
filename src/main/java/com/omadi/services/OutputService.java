package com.omadi.services;

import com.omadi.entities.Output;

import javax.transaction.Transactional;
import java.util.List;

public interface OutputService {

    Output isExist(int nodeId);

    @Transactional
    void save(Output output);

    List<Output> getAll();
}
