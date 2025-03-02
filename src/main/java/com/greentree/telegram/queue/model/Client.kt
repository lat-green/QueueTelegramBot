package com.greentree.telegram.queue.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.io.Serializable

@Entity(name = "Client")
data class Client  @JvmOverloads constructor(
    @Column
    var name: String = "",
    @Column
    var isAdmin: Boolean = false,
    @Column
    var chatId: Long? = null,
    @Id
    @GeneratedValue
    var id: Long? = null,
) : Serializable
