package bitxon.frontend.web.utils;

import org.springframework.security.oauth2.core.AbstractOAuth2Token;

import java.util.Base64;

public final class JwtUtils {

    private JwtUtils() {}

    public static String decodePayload(AbstractOAuth2Token token) {
        Base64.Decoder decoder = Base64.getDecoder();
        String[] chunks = token.getTokenValue().split("\\.");
        String payload = new String(decoder.decode(chunks[1]));
        return payload;
    }
}
