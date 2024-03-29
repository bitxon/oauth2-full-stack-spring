package bitxon.frontend.web.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.client.backend")
public class TicketClientProperties {
    private String baseUrl;
}
