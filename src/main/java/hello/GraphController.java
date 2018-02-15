package hello;

import model.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GraphController {

    @RequestMapping("/graph")
    public String graph(Model model){
        model.addAttribute("account", new Account());
        return "graph";
    }

}
