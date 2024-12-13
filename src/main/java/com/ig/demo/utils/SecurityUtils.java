package com.ig.demo.utils;

import com.ig.demo.security.CustomUsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

public class SecurityUtils {

    public static String getLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null) {
            return ((CustomUsernamePasswordAuthenticationToken) authentication).getUsername();
        }
        
        throw new IllegalStateException("Nessun utente loggato");
    }
}
