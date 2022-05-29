package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String Hello(Model model) { // model에 데이터를 실어서 view로 보낼 수 있다
        model.addAttribute("data", "hello~!");
        return "hello"; // 화면 이름
    }
}
