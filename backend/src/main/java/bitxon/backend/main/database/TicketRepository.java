package bitxon.backend.main.database;

import bitxon.backend.main.model.Ticket;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TicketRepository {

    // this is hardcoded value in demo-realm
    private static final String ID_USER01 = "86783e07-b0d2-4470-b287-c899bc2aa09c";

    private final List<Ticket> tickets = new ArrayList<>(List.of(
        Ticket.builder().id("1").passengerId(ID_USER01).arrival("JFK").departure("LAS").build(),
        Ticket.builder().id("2").passengerId(ID_USER01).arrival("LAS").departure("JFK").build(),
        Ticket.builder().id("3").passengerId("ANY_OTHER").arrival("LAS").departure("ATL").build(),
        Ticket.builder().id("4").passengerId("ANY_OTHER").arrival("ATL").departure("LAS").build()
    ));

    public List<Ticket> findAll() {
        return new ArrayList<>(tickets);
    }

    public Optional<Ticket> getById(String id) {
        return tickets.stream()
            .filter(ticket -> Objects.equals(ticket.getId(), id))
            .findFirst();
    }

    public Ticket save(Ticket ticket) {
        ticket.setId(UUID.randomUUID().toString());
        tickets.add(ticket);
        return ticket;
    }
}
