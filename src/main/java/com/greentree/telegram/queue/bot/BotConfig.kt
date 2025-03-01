package com.greentree.telegram.queue.bot

import lombok.Data
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
@Data
open class BotConfig(
	@Value("\${bot.name}")
	var botName: String,
	@Value("\${bot.token}")
	var token: String,
)