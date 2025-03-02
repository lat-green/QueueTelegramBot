package com.greentree.telegram.queue.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import lombok.Data
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity(name = "Position")
@Data
data class Position @JvmOverloads constructor(
	@ManyToOne(
		optional = false,
		cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH]
	)
	@OnDelete(action = OnDeleteAction.CASCADE)
	var queue: Queue? = null,
	@ManyToOne(
		optional = false,
		cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH]
	)
	@OnDelete(action = OnDeleteAction.CASCADE)
	var client: Client? = null,
	@Column
	var number: Int = 0,
	@Id
	@GeneratedValue
	var id: Long? = null,
)
