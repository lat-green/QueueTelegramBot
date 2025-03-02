package com.greentree.telegram.queue.repository;

import com.greentree.telegram.queue.model.Client;
import com.greentree.telegram.queue.model.Position;
import com.greentree.telegram.queue.model.Queue;
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
