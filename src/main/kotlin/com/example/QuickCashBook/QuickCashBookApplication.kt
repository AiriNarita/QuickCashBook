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
import java.net.URI


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
            "C" -> return TextMessage("CCCC")
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
            val A_Action = PostbackAction("食費", "A", "A", null, PostbackAction.InputOption.OPENKEYBOARD, null)
            val B_Action = PostbackAction("交通費", "B", "B", null, PostbackAction.InputOption.OPENKEYBOARD, null)
            val C_Action = PostbackAction("通費", "C", "C", null, PostbackAction.InputOption.OPENKEYBOARD, null)

            //　ここをbutton templateに変えたい
            val buttonTemplate = ButtonsTemplate(
                URI.create("https://任意の画像URL.jpg"), null, "cover", "#000000",
                "￥" + "$numbersOnly" + "は何のジャンル？", "ひとつ選んでね",
                null,
                listOf(
                    A_Action,
                    B_Action,
                    C_Action
                )
            )

            messagingApiClient.replyMessage(
                ReplyMessageRequest(
                    event.replyToken,
                    listOf(TemplateMessage("質問だよ", buttonTemplate)),
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