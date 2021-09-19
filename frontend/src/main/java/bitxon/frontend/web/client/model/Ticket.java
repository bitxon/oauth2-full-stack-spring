package bitxon.frontend.web.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private String id;
    private String passengerId;
    private String arrival;
    private String departure;
}
