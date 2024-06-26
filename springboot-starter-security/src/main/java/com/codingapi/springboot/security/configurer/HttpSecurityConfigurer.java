package com.codingapi.springboot.security.configurer;

import com.codingapi.springboot.security.filter.AuthenticationTokenFilter;
import com.codingapi.springboot.security.filter.MyAuthenticationFilter;
import com.codingapi.springboot.security.filter.MyLoginFilter;
import com.codingapi.springboot.security.filter.SecurityLoginHandler;
import com.codingapi.springboot.security.gateway.TokenGateway;
import com.codingapi.springboot.security.properties.CodingApiSecurityProperties;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

@AllArgsConstructor
public class HttpSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenGateway tokenGateway;

    private final SecurityLoginHandler securityLoginHandler;
    private final CodingApiSecurityProperties securityJwtProperties;
    private final AuthenticationTokenFilter authenticationTokenFilter;

    @Override
    public void configure(HttpSecurity security) throws Exception {
        AuthenticationManager manager = security.getSharedObject(AuthenticationManager.class);
        security.addFilter(new MyLoginFilter(manager, tokenGateway,securityLoginHandler, securityJwtProperties));
        security.addFilter(new MyAuthenticationFilter(manager, securityJwtProperties, tokenGateway, authenticationTokenFilter));
    }
}
