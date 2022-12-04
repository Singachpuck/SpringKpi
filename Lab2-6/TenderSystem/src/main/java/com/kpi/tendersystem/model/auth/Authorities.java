package com.kpi.tendersystem.model.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Authorities {
    DEFAULT("DEFAULT");

    private final GrantedAuthority grantedAuthority;

    Authorities(final String grantedAuthorityName) {
        this.grantedAuthority = new SimpleGrantedAuthority(grantedAuthorityName);
    }

    public GrantedAuthority getGrantedAuthority() {
        return grantedAuthority;
    }
}
