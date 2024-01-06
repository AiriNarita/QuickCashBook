package com.example.QuickCashBook.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
@Configuration
class MessagesService {

    /**
     * 送られたメッセージが
     * 数値(金額)を含むなのか、テキスト(ジャンル等)なのか判断するメソッド
     */
     fun containsNumber(text: String): Boolean {
        // メッセージに数字が含まれているかどうかを判断
        return text.any { it.isDigit() }
    }

    /**
     * 数字が含まれていた場合、メッセージから数字だけを抽出して返す
     * @param text メッセージ
     * @return 数字のみに変換した文字列
     */
    fun extractNumbers(text: String): String {
        return text.filter { it.isDigit() }
    }
}