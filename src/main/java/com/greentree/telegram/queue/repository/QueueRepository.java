package com.greentree.telegram.queue.repository;

import com.greentree.telegram.queue.model.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QueueRepository extends JpaRepository<Queue, Long> {
    public Optional<Queue> findByName(String name);
}
