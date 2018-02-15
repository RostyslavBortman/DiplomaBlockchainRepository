package hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ErrorController {

    @PostMapping("/error")
    public String error(){
        return "error";
    }

}
