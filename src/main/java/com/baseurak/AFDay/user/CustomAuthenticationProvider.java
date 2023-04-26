package com.baseurak.AFDay.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author: Ru
 */
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = (String)authentication.getCredentials();

        //UserContext userContext = (UserContext) userService.loadUserByUsername(username);
        UserDetails findUserDetails = userService.loadUserByUsername(username);

        if(!passwordEncoder.matches(password, findUserDetails.getPassword())){
            throw new BadCredentialsException("BadCredentialsException");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(findUserDetails, null, findUserDetails.getAuthorities());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
