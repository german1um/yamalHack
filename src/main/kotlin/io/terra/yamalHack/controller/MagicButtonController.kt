package io.terra.yamalHack.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/button")
class MagicButtonController {

    @GetMapping
    fun submitDickPic(
            @RequestParam("dickPick")
            picture: String
    ): String {
        return "Hello"
    }
}