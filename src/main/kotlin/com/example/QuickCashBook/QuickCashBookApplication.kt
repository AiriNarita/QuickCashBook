package com.example.QuickCashBook

import com.example.QuickCashBook.services.MessagesService
import com.linecorp.bot.messaging.client.MessagingApiClient
import com.linecorp.bot.messaging.model.*
import com.linecorp.bot.spring.boot.handler.annotation.EventMapping
import com.linecorp.bot.spring.boot.handler.annotation.LineMessageHandler
import com.linecorp.bot.webhook.model.Event
import com.linecorp.bot.webhook.model.MessageEvent
import com.linecorp.bot.webhook.model.PostbackEvent
import com.linecorp.bot.webhook.model.TextMessageContent
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


fun main(args: Array<String>) {
    runApplication<QuickCashBookApplication>(*args)
}

@SpringBootApplication
@LineMessageHandler
open class QuickCashBookApplication(
    private val messagingApiClient: MessagingApiClient,
    private val messagesService: MessagesService
) {
    //	private val log = LoggerFactory.getLogger(EchoApplication::class.java)

    @EventMapping
    fun handlePostBack(event: PostbackEvent?): Message {
        when (event?.postback?.data.toString()) {
            "A" -> return TextMessage("AAAA！")
            "B" -> return TextMessage("BBBB")
        }
        return TextMessage("?")
    }

    // recursive
    @EventMapping
    fun handleTextMessageEvent(event: MessageEvent) {
        println("event: $event")
        val message = event.message
        if (message is TextMessageContent) {
            val originalMessageText = message.text + "円ですね！"
            val originalProposalMessage = "金額を教えてね"

            if (!messagesService.containsNumber(message.text)) {
                messagingApiClient.replyMessage(
                    ReplyMessageRequest(
                        event.replyToken,
                        listOf(TextMessage(originalProposalMessage)),
                        false
                    )
                )
                return
            }

            val numbersOnly = messagesService.extractNumbers(originalMessageText)

            val confirmTemplate = ConfirmTemplate(
                "￥" + "$numbersOnly" + "は何のジャンル？",
                listOf(
                    PostbackAction("食費", "A", "A", null, PostbackAction.InputOption.OPENKEYBOARD, null),
                    PostbackAction("交通費", "B", "B", null, PostbackAction.InputOption.OPENKEYBOARD, null),
                )
            )

            messagingApiClient.replyMessage(
                ReplyMessageRequest(
                    event.replyToken,
                    listOf(TemplateMessage("質問だよ", confirmTemplate)),
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