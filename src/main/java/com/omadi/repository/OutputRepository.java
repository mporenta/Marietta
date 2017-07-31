package com.omadi.repository;

import com.omadi.entities.Output;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutputRepository extends JpaRepository<Output, Integer> {

    Output findOneByNodeId(int nodeId);
}
