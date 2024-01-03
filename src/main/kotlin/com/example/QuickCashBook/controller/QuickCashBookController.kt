package com.example.QuickCashBook.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/webhook")
class QuickCashBookController {

    @PostMapping("/")
    fun messsagePost(){
    }



}