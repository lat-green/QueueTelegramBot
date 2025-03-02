package com.greentree.telegram.queue.repository;

import com.greentree.telegram.queue.entity.Client;
import com.greentree.telegram.queue.entity.Position;
import com.greentree.telegram.queue.entity.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    List<Position> findAllByQueue(Queue queue);

    Optional<Position> findByClient(Client client);

    void deleteByClientAndQueue(Client client, Queue queue);

}
