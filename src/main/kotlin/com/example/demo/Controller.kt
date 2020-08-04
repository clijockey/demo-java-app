package com.example.demo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {
  
  @GetMapping("/")
  fun index(map: ModelMap): String {  
        // Name of return template file, corresponding to src/main/resources/templates/index.html
        return "index"
  }
}