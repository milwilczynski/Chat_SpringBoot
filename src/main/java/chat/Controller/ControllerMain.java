package chat.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerMain {
    @GetMapping(value = "/")
    public String x(){
        return "index.html";
    }
}
