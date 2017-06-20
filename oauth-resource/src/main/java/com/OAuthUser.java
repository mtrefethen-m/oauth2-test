package com;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

public class OAuthUser {

    private String username;
    private Set<String> scopes;
    private Collection<? extends GrantedAuthority> authorities;

    public OAuthUser() {
    }

    public OAuthUser(String username, Set<String> scopes, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.scopes = scopes;
        this.authorities = authorities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
