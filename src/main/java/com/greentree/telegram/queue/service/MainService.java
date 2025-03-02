package com.greentree.telegram.queue.service;

import com.greentree.telegram.queue.entity.Client;
import com.greentree.telegram.queue.entity.Queue;
import com.greentree.telegram.queue.repository.ClientRepository;
import com.greentree.telegram.queue.repository.PositionRepository;
import com.greentree.telegram.queue.repository.QueueRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service
@AllArgsConstructor
public class MainService {

    private final QueueRepository queueRepository;
    private final ClientRepository clientRepository;
    private final PositionRepository positionRepository;

    public void dequeue(long clientId, long queueId) {
        Queue queue = queueRepository.getReferenceById(queueId);
        Client client = clientRepository.getReferenceById(clientId);
        positionRepository.deleteByClientAndQueue(client, queue);
    }

}
