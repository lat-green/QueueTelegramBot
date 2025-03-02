package com.greentree.telegram.queue.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.io.Serializable

@Entity(name = "Position")
data class Position @JvmOverloads constructor(
	@Column
	@OnDelete(action = OnDeleteAction.CASCADE)
	var queue: Queue? = null,
	@Column
	@OnDelete(action = OnDeleteAction.CASCADE)
	var client: Client? = null,
	@Column
	var number: Int = 0,
	@Id
	@GeneratedValue
	var id: Long? = null,
) : Serializable
