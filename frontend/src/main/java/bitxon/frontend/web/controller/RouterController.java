package bitxon.frontend.web.controller;

import bitxon.frontend.web.client.TicketClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class RouterController {

    private final TicketClient ticketClient;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/tickets")
    public String tickets(Model model) {
        var tickets = ticketClient.search();
        model.addAttribute("tickets", tickets);
        return "tickets";
    }

}
