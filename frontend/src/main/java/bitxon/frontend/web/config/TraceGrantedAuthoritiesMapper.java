package bitxon.frontend.web.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import java.util.Collection;

public class TraceGrantedAuthoritiesMapper implements GrantedAuthoritiesMapper {
    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        System.out.println("Authorities: " + authorities);
        return authorities;
    }
}
