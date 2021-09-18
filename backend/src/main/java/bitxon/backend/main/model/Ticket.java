package bitxon.backend.main.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ticket {
    private String id;
    private String passengerId;
    private String arrival;
    private String departure;
}
