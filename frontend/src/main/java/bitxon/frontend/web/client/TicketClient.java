package bitxon.frontend.web.client;

import bitxon.frontend.web.client.model.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TicketClient {
    private final TicketClientProperties properties;
    private final WebClient webClient;

    public List<Ticket> search() {
        return webClient.get().uri(properties.getBaseUrl())
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<Ticket>>() {
            })
            .block();
    }

}
