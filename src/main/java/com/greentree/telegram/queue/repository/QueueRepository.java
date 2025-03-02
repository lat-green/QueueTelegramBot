package com.greentree.telegram.queue.repository;

import com.greentree.telegram.queue.entity.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QueueRepository extends JpaRepository<Queue, Long> {

    Optional<Queue> findByName(String name);

    void deleteByName(String name);

}
