package bitxon.backend.main.controller;

import bitxon.backend.main.database.TicketRepository;
import bitxon.backend.main.model.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketRepository ticketRepository;

    @GetMapping
    @PostFilter("hasRole('app-admin') or filterObject.passengerId == authentication.name")
    public List<Ticket> search() {
        return ticketRepository.findAll();
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasRole('app-admin') or returnObject.passengerId == authentication.name")
    public Ticket getTicket(@PathVariable String id) {
        System.out.println("Name = " + SecurityContextHolder.getContext().getAuthentication().getName());
        return ticketRepository.getById(id)
            .orElseThrow(() -> new RuntimeException("Not found"));
    }

    @PostMapping
    @PreAuthorize("hasRole('app-admin')")
    public Ticket create(@RequestBody Ticket ticket) {
        return ticketRepository.save(ticket);
    }
}
