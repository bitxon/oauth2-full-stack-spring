package bitxon.frontend.web.config.extention;

import bitxon.frontend.web.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @see <a href="https://docs.spring.io/spring-security/site/docs/5.0.7.RELEASE/reference/html/oauth2login-advanced.html#oauth2login-advanced-map-authorities-oauth2userservice">Delegation-based strategy with OAuth2UserService</a>
 */
public class CustomOidcUserService extends OidcUserService {

    public CustomOidcUserService() {
        super();
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        // Delegate to the default implementation for loading a user
        OidcUser oidcUser = super.loadUser(userRequest);
        // 1) Fetch the authority information from the protected resource using accessToken
        var decodedAccessTokenPayload = decodePayload(userRequest.getAccessToken());
        // 2) Map the authority information to one or more GrantedAuthority's and add it to mappedAuthorities
        var originalAuthorities = oidcUser.getAuthorities();
        var additionalAuthorities = convert(decodedAccessTokenPayload);
        var mappedAuthorities = List.of(originalAuthorities, additionalAuthorities).stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());

        // 3) Create a copy of oidcUser but use the mappedAuthorities instead
        oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());

        return oidcUser;
    }


    private static Collection<? extends GrantedAuthority> convert(Map<String, Object> jwt) {
        return Optional.ofNullable((Map<String, Object>) jwt.get("realm_access"))
            .map(realm -> (List<String>) realm.get("roles"))
            .stream()
            .flatMap(Collection::stream)
            .map(keycloakRole -> "ROLE_" + keycloakRole)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    @SneakyThrows
    private static Map<String, Object> decodePayload(AbstractOAuth2Token token) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(JwtUtils.decodePayload(token), Map.class);
    }
};



