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

        // FOOD details
        // A?price=${message.text}&genre=food
        // [0] : A
        // [1] : price=2000&genre=food&detail=external
        // result : final?price=2000&genre=food&detail=external
        val Food_A_Action = PostbackAction(
            "外食",
            "food_final?" + event?.postback?.data.toString().split("?")[1] + "&detail=external",
            "外食",
            null,
            PostbackAction.InputOption.OPENKEYBOARD,
            null
        )
        val Food_B_Action = PostbackAction(
            "スーパー",
            "food_final?" + event?.postback?.data.toString().split("?")[1] + "&detail=mart",
            "スーパー",
            null,
            PostbackAction.InputOption.OPENKEYBOARD,
            null
        )
        val Food_C_Action = PostbackAction(
            "コンビニ",
            "food_final?" + event?.postback?.data.toString().split("?")[1] + "&detail=convenience",
            "コンビニ",
            null,
            PostbackAction.InputOption.OPENKEYBOARD,
            null
        )
        val Food_D_Action = PostbackAction(
            "カフェ",
            "food_final?" + event?.postback?.data.toString().split("?")[1] + "&detail=cafe",
            "カフェ",
            null,
            PostbackAction.InputOption.OPENKEYBOARD,
            null
        )

        val foodButtonTemplate = ButtonsTemplate(
            URI.create("https://任意の画像URL.jpg"), null, "cover", "#000000",
            "食費項目", "ひとつ選んでね",
            null,
            listOf(
                Food_A_Action,
                Food_B_Action,
                Food_C_Action,
                Food_D_Action,
            )
        )

        // food detail button
        val Life_A_Action = PostbackAction(
            "生活用品",
            "life_final?" + event?.postback?.data.toString().split("?")[1] + "&detail=needGoods",
            "生活用品(雑貨。ex.さら、キッチン用具)",
            null,
            PostbackAction.InputOption.OPENKEYBOARD,
            null
        )
        val Life_B_Action = PostbackAction(
            "日用消耗品",
            "life_final?" + event?.postback?.data.toString().split("?")[1] + "&detail=dairyConsumable",
            "日用消耗品",
            null,
            PostbackAction.InputOption.OPENKEYBOARD,
            null
        )
        val Life_C_Action = PostbackAction(
            "その他",
            "life_final?" + event?.postback?.data.toString().split("?")[1] + "&detail=dairyOther",
            "その他",
            null,
            PostbackAction.InputOption.OPENKEYBOARD,
            null
        )
        val Life_D_Action = PostbackAction(
            "衣類",
            "life_final?" + event?.postback?.data.toString().split("?")[1] + "&detail=closes",
            "衣類",
            null,
            PostbackAction.InputOption.OPENKEYBOARD,
            null
        )

        val lifeButtonTemplate = ButtonsTemplate(
            URI.create("https://任意の画像URL.jpg"), null, "cover", "#000000",
            "生活費項目", "ひとつ選んでね",
            null,
            listOf(
                Life_A_Action,
                Life_B_Action,
                Life_C_Action,
                Life_D_Action,
            )
        )

        val Transportation_A_Action = PostbackAction(
            "バス",
            "Transportation_final?" + event?.postback?.data.toString().split("?")[1] + "&detail=bus",
            "バス",
            null,
            PostbackAction.InputOption.OPENKEYBOARD,
            null
        )
        val Transportation_B_Action = PostbackAction(
            "電車",
            "Transportation_final?" + event?.postback?.data.toString().split("?")[1] + "&detail=train",
            "電車",
            null,
            PostbackAction.InputOption.OPENKEYBOARD,
            null
        )

        val Transportation_C_Action = PostbackAction(
            "その他",
            "Transportation_final?" + event?.postback?.data.toString().split("?")[1] + "&detail=other",
            "その他",
            null,
            PostbackAction.InputOption.OPENKEYBOARD,
            null
        )

        val transportationButtonTemplate = ButtonsTemplate(
            URI.create("https://任意の画像URL.jpg"), null, "cover", "#000000",
            "交通費項目", "ひとつ選んでね",
            null,
            listOf(
                Transportation_A_Action,
                Transportation_B_Action,
                Transportation_C_Action
            )
        )


        when (event?.postback?.data.toString().split("?")[0]) {
            "A" -> return TemplateMessage("食費の項目は？", foodButtonTemplate)
            "B" -> return TemplateMessage("交通費", transportationButtonTemplate)
            "C" -> return TemplateMessage("生活費は何？", lifeButtonTemplate)
            "D" -> return TextMessage("その他だね！")
            // food_final?price=2000&genre=food&detail=external
            // [0] : food_final
            // [1] : price=2000&genre=food&detail=external
            "food_final" -> {
                val template = event?.postback?.data.toString().split("?")[1]
                println(template)
                // dbに入れる
                return TextMessage("保存したよ！$template")
            }

            "life_final" -> {
                val template = event?.postback?.data.toString().split("?")[1]
                println(template)
                // dbに入れる
                return TextMessage("保存したよ！$template")
            }

            "Transportation_final" -> {
                val template = event?.postback?.data.toString().split("?")[1]
                println(template)
                // dbに入れる
                return TextMessage("保存したよ！$template")
            }
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

            //TODO: 修復が必要
            if (!messagesService.containsNumber(message.text)) {
                // メッセージに数字が含まれていない場合
                replyOnlyString(event, "金額を教えてね")
                return;
            }

            val numbersOnly = messagesService.extractNumbers(originalMessageText)
            // ジャンル選択①
            val A_Action = PostbackAction(
                "食費",
                "A?price=${message.text}&genre=food",
                "A",
                null,
                PostbackAction.InputOption.OPENKEYBOARD,
                null
            )
            val B_Action = PostbackAction(
                "交通費",
                "B?price=${message.text}&genre=transportation",
                "B",
                null,
                PostbackAction.InputOption.OPENKEYBOARD,
                null
            )
            val C_Action = PostbackAction(
                "生活費(ex.日常用品、消耗品、服)",
                "C?price=${message.text}&genre=life",
                "C",
                null,
                PostbackAction.InputOption.OPENKEYBOARD,
                null
            )
            val D_Action = PostbackAction("そのほか", "C", "C", null, PostbackAction.InputOption.OPENKEYBOARD, null)

            //　ここをbutton templateに変えたい
            val buttonTemplate = ButtonsTemplate(
                URI.create("https://任意の画像URL.jpg"), null, "cover", "#000000",
                "￥" + "$numbersOnly" + "は何のジャンル？", "ひとつ選んでね",
                null,
                listOf(
                    A_Action,
                    B_Action,
                    C_Action,
                    D_Action,
                )
            )
            replyOnlyTemplate(event, listOf(TemplateMessage("質問だよ", buttonTemplate)))
        }
    }


    /**
     * 指定されたテキストメッセージで返信するメソッド
     * @param replyToken String メッセージの返信先トークン
     * @param messages List<TextMessage> 送信するテキストメッセージのリスト
     * @param notificationDisabled Boolean 通知を無効にするかどうかのフラグ
     */

    private fun replyOnlyString(event: MessageEvent, originalProposalMessage: String) {
        messagingApiClient.replyMessage(
            ReplyMessageRequest(
                event.replyToken,
                listOf(TextMessage(originalProposalMessage)),
                false
            )
        )
    }

    /**
     * 指定されたテンプレートメッセージで返信するメソッド
     * @param replyToken String メッセージの返信先トークン
     * @param messages List<Message> 送信するメッセージのリスト
     * @param notificationDisabled Boolean 通知を無効にするかどうかのフラグ
     */

    private fun replyOnlyTemplate(event: MessageEvent, messageList: List<Message>) {
        messagingApiClient.replyMessage(
            ReplyMessageRequest(
                event.replyToken,
                messageList,
                false
            )
        )
    }

    @EventMapping
    fun handleDefaultMessageEvent(event: Event) {
        println("event: $event")
    }

}