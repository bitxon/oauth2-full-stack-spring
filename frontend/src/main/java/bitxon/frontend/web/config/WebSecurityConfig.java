package bitxon.frontend.web.config;

import bitxon.frontend.web.config.extention.CustomGrantedAuthoritiesMapper;
import bitxon.frontend.web.config.extention.CustomOidcUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //To debug hasAuthority/hasRole see SecurityExpressionRoot
        http.authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/actuator/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .oauth2Login()
                .userInfoEndpoint()
                    .userAuthoritiesMapper(new CustomGrantedAuthoritiesMapper())
                    .oidcUserService(new CustomOidcUserService());
    }

    @Bean
    public WebClient webClient(ClientRegistrationRepository regRepo, OAuth2AuthorizedClientRepository clientRepo) {
        var filter = new ServletOAuth2AuthorizedClientExchangeFilterFunction(regRepo, clientRepo);
        filter.setDefaultOAuth2AuthorizedClient(true);
        return WebClient.builder().apply(filter.oauth2Configuration()).build();
    }
}
