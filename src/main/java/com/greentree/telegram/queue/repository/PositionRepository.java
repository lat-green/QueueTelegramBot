package com.greentree.telegram.queue.repository;

import com.greentree.telegram.queue.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    @Query("delete from position where client = ?1")
    void deleteByClientId(long clientId);

}
