package com.example.demo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {
  
  @GetMapping("/")
  fun saySomething(): String {
    return "Kotlin app on Cloud Run, containerized by Jib!"
  }
}