package com.example.QuickCashBook

import MessagesService.MessagesService
import com.linecorp.bot.messaging.client.MessagingApiClient
import com.linecorp.bot.messaging.model.ReplyMessageRequest
import com.linecorp.bot.messaging.model.TextMessage
import com.linecorp.bot.spring.boot.handler.annotation.EventMapping
import com.linecorp.bot.spring.boot.handler.annotation.LineMessageHandler
import com.linecorp.bot.webhook.model.Event
import com.linecorp.bot.webhook.model.MessageEvent
import com.linecorp.bot.webhook.model.TextMessageContent
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

fun main(args: Array<String>) {
	runApplication<QuickCashBookApplication>(*args)
}

@SpringBootApplication
@LineMessageHandler
open class QuickCashBookApplication(
	private val messagingApiClient: MessagingApiClient
) {
//	private val log = LoggerFactory.getLogger(EchoApplication::class.java)

	@EventMapping
	fun handleTextMessageEvent(event: MessageEvent) {
		println("event: $event")
		val message = event.message
		if (message is TextMessageContent) {
			val originalMessageText = message.text + "と、送信されました！"
			messagingApiClient.replyMessage(
				ReplyMessageRequest(
					event.replyToken,
					listOf(TextMessage(originalMessageText)),
					false
				)
			)
		}
	}

	@EventMapping
	fun handleDefaultMessageEvent(event: Event) {
		println("event: $event")
	}
}