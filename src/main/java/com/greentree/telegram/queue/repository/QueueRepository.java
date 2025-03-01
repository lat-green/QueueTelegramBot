package com.greentree.telegram.queue.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Queue;

public interface QueueRepository extends JpaRepository<Queue, Long> {

}
