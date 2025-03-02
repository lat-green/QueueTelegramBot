package com.greentree.telegram.queue.service;

import com.greentree.telegram.queue.model.Client;
import com.greentree.telegram.queue.model.Position;
import com.greentree.telegram.queue.model.Queue;
import com.greentree.telegram.queue.repository.ClientRepository;
import com.greentree.telegram.queue.repository.PositionRepository;
import com.greentree.telegram.queue.repository.QueueRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void enqueue(long clientId, long queueId) {
        Queue queue = queueRepository.getReferenceById(queueId);
        Client client = clientRepository.getReferenceById(clientId);
        List<Position> positions = positionRepository.findAllByQueue(queue);
        int number = findFirstFreeNumber(positions);
        Position position = new Position();
        position.setClient(client);
        position.setQueue(queue);
        position.setNumber(number);
        positionRepository.save(position);
    }

    private int findFirstFreeNumber(List<Position> positions) {
        int i = 0;
        while (i < positions.size()) {
            if (positions.get(i).getNumber() > i)
                return i;
            i++;
        }
        return positions.size();
    }

}
