package com.omadi.services.impl;

import com.omadi.entities.Output;
import com.omadi.repository.OutputRepository;
import com.omadi.services.OutputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultOutputService implements OutputService {

    @Autowired
    private OutputRepository outputRepository;

    @Override
    public List<Output> getAll() {
        return outputRepository.findAll();
    }

    @Override
    public void save(Output output) {
        outputRepository.save(output);
    }

    @Override
    public Output isExist(int nodeId) {
        return outputRepository.findOneByNodeId(nodeId);
    }
}
