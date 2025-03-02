package com.greentree.telegram.queue.repository;

import com.greentree.telegram.queue.model.Client;
import jakarta.ws.rs.OPTIONS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.ref.Cleaner;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    public Optional<Client> findByChatId(long chatId);
}
