package com.luangeng.oauthserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.*;

/**
 * token管理
 */
@Controller
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ConsumerTokenServices tokenServices;


    @RequestMapping(value = "/{token}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> revokeToken(@PathVariable String token) {
        //checkResourceOwner(user, principal);
        if (tokenServices.revokeToken(token)) {
            return new ResponseEntity<Void>(HttpStatus.OK);
        } else {
            return new ResponseEntity<Void>(HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping
    public String listTokens(Model model) throws Exception {
        List<OAuth2AccessToken> tokens = new ArrayList<>();
        List<String> clients = Arrays.asList("photo", "note", "demo");
        for (String client : clients) {
            tokens.addAll(enhance(tokenStore.findTokensByClientId(client)));
        }
        model.addAttribute("tokens", tokens);
        return "index";
    }

    private List<OAuth2AccessToken> enhance(Collection<OAuth2AccessToken> tokens) {
        List<OAuth2AccessToken> result = new ArrayList<OAuth2AccessToken>();
        for (OAuth2AccessToken prototype : tokens) {
            DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(prototype);
            OAuth2Authentication authentication = tokenStore.readAuthentication(token);
            if (authentication == null) {
                continue;
            }
            String clientId = authentication.getOAuth2Request().getClientId();
            String userName = authentication.getUserAuthentication().getName();
            String userRole = printList(authentication.getAuthorities());

            Map<String, Object> map = new HashMap<String, Object>(token.getAdditionalInformation());
            map.put("client_id", clientId);
            map.put("user_name", userName);
            map.put("user_role", userRole);
            token.setAdditionalInformation(map);
            result.add(token);
        }
        return result;
    }

    private void checkResourceOwner(String user, Principal principal) {
        if (principal instanceof OAuth2Authentication) {
            OAuth2Authentication authentication = (OAuth2Authentication) principal;
            if (!authentication.isClientOnly() && !user.equals(principal.getName())) {
                throw new AccessDeniedException(String.format("User %s cannot obtain tokens for user %s",
                        principal.getName(), user));
            }
        }
    }

    private <T> String printList(Collection<T> collection) {
        StringBuilder sb = new StringBuilder();
        for (T t : collection) {
            sb.append("[");
            sb.append(t);
            sb.append("] ");
        }
        return sb.toString();
    }

}
