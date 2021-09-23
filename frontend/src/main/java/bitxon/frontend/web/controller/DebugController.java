package bitxon.frontend.web.controller;

import bitxon.frontend.web.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class DebugController {

    private final OAuth2AuthorizedClientService oauth2ClientService;

    @GetMapping("/debug")
    public String debug(Model model, @AuthenticationPrincipal OidcUser principal) {

        //Populate Common Info
        List<String> authorities = principal.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
        model.addAttribute("authorities", authorities);


        //Populate Id Token details
        OidcIdToken idToken = principal.getIdToken();
        model.addAttribute("idToken", idToken);
        model.addAttribute("idTokenDecodedPayload", JwtUtils.decodePayload(idToken));

        //Populate Access Token details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        OAuth2AuthorizedClient authClient = oauth2ClientService.loadAuthorizedClient(
            oauthToken.getAuthorizedClientRegistrationId(),
            oauthToken.getName());
        OAuth2AccessToken accessToken = authClient.getAccessToken();

        model.addAttribute("accessToken", accessToken);
        model.addAttribute("accessTokenDecodedPayload", JwtUtils.decodePayload(accessToken));

        return "debug";
    }

}
