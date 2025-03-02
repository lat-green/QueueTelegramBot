package com.greentree.telegram.queue.repository;

import com.greentree.telegram.queue.model.Client;
import com.greentree.telegram.queue.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

}
