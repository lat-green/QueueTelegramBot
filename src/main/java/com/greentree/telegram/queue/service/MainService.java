package com.greentree.telegram.queue.service;

import com.greentree.telegram.queue.model.Client;
import com.greentree.telegram.queue.model.Position;
import com.greentree.telegram.queue.model.Queue;
import com.greentree.telegram.queue.repository.ClientRepository;
import com.greentree.telegram.queue.repository.PositionRepository;
import com.greentree.telegram.queue.repository.QueueRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Transactional
@Service
@AllArgsConstructor
public class MainService {

    private final QueueRepository queueRepository;
    private final ClientRepository clientRepository;
    private final PositionRepository positionRepository;

    public boolean dequeue(long chatId, long queueId) {
        Queue queue = queueRepository.getReferenceById(queueId);
        Client client = clientRepository.findByChatId(chatId).orElseThrow();
        List<Position> positions = positionRepository.findAllByQueue(queue);

        if (isClientInQueue(positions, client)) {
            positionRepository.deleteByClientAndQueue(client, queue);
            return true;
        }
        return false;
    }

    private boolean isClientInQueue(List<Position> positions, Client client) {
        for (var position : positions)
            if (position.getClient() == client)
                return true;
        return false;
    }

    public void enqueue(long chatId, long queueId) {
        Queue queue = queueRepository.getReferenceById(queueId);
        Client client = clientRepository.findByChatId(chatId).orElseThrow();
        List<Position> positions = positionRepository.findAllByQueue(queue);

        if (!isClientInQueue(positions, client)) {
            int number = findFirstFreeNumber(positions);
            Position position = new Position();
            position.setClient(client);
            position.setQueue(queue);
            position.setNumber(number);
            positionRepository.save(position);
        }
    }

    public void enqueueDeadline(long chatId, long queueId){
        Queue queue = queueRepository.getReferenceById(queueId);
        Client client = clientRepository.findByChatId(chatId).orElseThrow();
        List<Position> positions = positionRepository.findAllByQueue(queue);

        if (!isClientInQueue(positions, client)) {
            int number = 0;
            Position position = new Position();
            position.setClient(client);
            position.setQueue(queue);
            position.setNumber(number);
            positionRepository.save(position);
        }
    }

    public static int findFirstFreeNumber(List<Position> positions) {
        if (positions.isEmpty() || positions.get(positions.size() - 1).getNumber() == 0)
            return 1;

        int i = 0, number = 1;

        while (positions.get(i).getNumber() == 0)
            i++;

        while (i < positions.size()){
            if (positions.get(i).getNumber() != number)
                return number;
            i++;
            number++;
        }

        return number;
    }

    public boolean enqueueByNumber(long chatId, long queueId, int number) {
        Queue queue = queueRepository.getReferenceById(queueId);
        Client client = clientRepository.findByChatId(chatId).orElseThrow();
        List<Position> positions = positionRepository.findAllByQueue(queue);

        if (isNumberFree(positions, number)) {
            Position position = new Position();
            position.setClient(client);
            position.setQueue(queue);
            position.setNumber(number);
            positionRepository.save(position);
            return true;
        }
        return false;
    }

    private boolean isNumberFree(List<Position> positions, int number) {
        for (var position : positions)
            if (position.getNumber() == number)
                return false;
        return true;
    }

    public boolean createQueue(String text, Long chatId) {
        if (queueRepository.findByName(text).isEmpty()) {
            Queue queue = new Queue();
            queue.setName(text);
            queueRepository.save(queue);
            return true;
        }
        return false;
    }

    public void deleteQueueByName(String name) {
        queueRepository.deleteByName(name);
    }

    public List<String> findAllQueueNames() {
        ArrayList<String> queueNames = new ArrayList<>();
        int i = 0;
        for (var queue : queueRepository.findAll()) {
            queueNames.add(i, queue.getName());
            i++;
        }
        return queueNames;
    }

    public List<Queue> findAllQueue() {
        return queueRepository.findAll();
    }

    public void addClient(Long chatId, String defaultName) {
        if (clientRepository.findByChatId(chatId).isEmpty()) {
            Client client = new Client();
            client.setChatId(chatId);
            client.setName(defaultName);
            client.setAdmin(false);
            clientRepository.save(client);
        }
    }

    public boolean isClientInQueue(long chatId, long queueId) {
        Client client = clientRepository.findByChatId(chatId).orElseThrow();
        Queue queue = queueRepository.getReferenceById(queueId);
        List<Position> positions = positionRepository.findAllByQueue(queue);
        for (var position : positions)
            if (position.getClient() == client)
                return true;
        return false;
    }

    public String getQueuePeople(long queueId) {
        Queue queue = queueRepository.getReferenceById(queueId);
        List<Position> positions = positionRepository.findAllByQueue(queue);
        if (positions.isEmpty())
            return "Очередь пуста";
        positions.sort(Comparator.comparingInt(Position::getNumber));
        StringBuilder builder = new StringBuilder();
        builder.append(queue.getName());
        builder.append('\n');
        for (var position : positions) {
            builder.append(position.getNumber());
            builder.append(")");
            builder.append(position.getClient().getName());
            if (position.getNumber() == 0)
                builder.append("\t//Дедлайн");
            builder.append('\n');
        }
        return builder.toString();
    }

    public Client findClientByChatId(long chatId) {
        return clientRepository.findByChatId(chatId).orElseThrow();
    }

    public void rename(String newName, long chatId){
        Client client = clientRepository.findByChatId(chatId).orElseThrow();

        client.setName(newName);
        clientRepository.save(client);
    }
}
