package com.greentree.telegram.queue.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity(name = "Queue")
data class Queue @JvmOverloads constructor(
	@Column
	var name: String = "",
	@Id
	@GeneratedValue
	var id: Long? = null,
)
