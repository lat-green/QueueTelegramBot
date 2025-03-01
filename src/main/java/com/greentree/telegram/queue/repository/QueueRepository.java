package com.greentree.telegram.queue.repository;

import com.greentree.telegram.queue.model.Queue;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QueueRepository extends JpaRepository<Queue, Long> {

}
