package com.mlurbe.storage.web.config.security;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsAuthentication implements Authentication {

    @Autowired
    private final UserDetails userDetails;
    private boolean authenticated = true;

    public UserDetailsAuthentication(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities () {
        return userDetails.getAuthorities();
    }

    @Override
    public Object getCredentials () {
        return userDetails.getPassword();
    }

    @Override
    public Object getDetails () {
        return userDetails;
    }

    @Override
    public Object getPrincipal () {
        return userDetails.getUsername();
    }

    @Override
    public boolean isAuthenticated () {
        return authenticated;
    }

    @Override
    public void setAuthenticated (boolean isAuthenticated) throws IllegalArgumentException {
        authenticated = isAuthenticated;
    }

    @Override
    public String getName () {
        return userDetails.getUsername();
    }
}
