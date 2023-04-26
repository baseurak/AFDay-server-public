package com.baseurak.AFDay.user;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String email;

    public SessionUser(User user) {
        this.email = user.getEmail();
    }
}