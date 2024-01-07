package com.example.QuickCashBook

import com.example.QuickCashBook.services.MessagesService
import com.linecorp.bot.messaging.client.MessagingApiClient
import com.linecorp.bot.messaging.model.*
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
    private val messagingApiClient: MessagingApiClient,
    private val messagesService: MessagesService
) {
    //	private val log = LoggerFactory.getLogger(EchoApplication::class.java)
    @EventMapping
    fun handleTextMessageEvent(event: MessageEvent) {
        println("event: $event")
        val message = event.message
        if (message is TextMessageContent) {
            val originalMessageText = message.text + "円ですね！"
            val originalProposalMessage = "金額を教えてね"


            val replyMessage = if (messagesService.containsNumber(message.text)) {
                // メッセージに数字が含まれている場合
                val numbersOnly = messagesService.extractNumbers(originalMessageText)
                "$numbersOnly" + "円ですね！"
            } else {
                // メッセージに数字が含まれていない場合
                "$originalProposalMessage"
            }

            //val textMessage = TextMessage(replyMessage)

//            val actions = listOf(
//                URIAction(
//                    "連携する",
//                    URI.create("https://www.lycorp.co.jp/ja/"), null
//                )
//            )
//            val templateMessage = TemplateMessage(
//                "アカウント連携",
//                ButtonsTemplate(null, null, null, null, "TEST TITLE", "TEST TEXT", actions.get(0), actions)
//            )
//

            messagingApiClient.replyMessage(
                ReplyMessageRequest(
                    event.replyToken,
                    listOf(TextMessage(replyMessage)),
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