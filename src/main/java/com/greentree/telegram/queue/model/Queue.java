package com.greentree.telegram.queue.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Queue")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Queue {

    @Id
    @GeneratedValue
    private long id;

}
