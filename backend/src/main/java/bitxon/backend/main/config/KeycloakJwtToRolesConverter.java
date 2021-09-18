package bitxon.backend.main.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class KeycloakJwtToRolesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        return Optional.ofNullable((Map<String, Object>) jwt.getClaim("realm_access"))
            .map(realm -> (List<String>) realm.get("roles"))
            .stream()
            .flatMap(Collection::stream)
            .map(keycloakRole -> "ROLE_" + keycloakRole)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }
}
