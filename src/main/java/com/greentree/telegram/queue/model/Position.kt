package com.greentree.telegram.queue.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.io.Serializable

@Entity(name = "Position")
data class Position @JvmOverloads constructor(
	@Column
	var queue: Queue? = null,
	@Column
	var client: Client? = null,
	@Column
	var number: Int = 0,
	@Id
	@GeneratedValue
	var id: Long? = null,
) : Serializable
