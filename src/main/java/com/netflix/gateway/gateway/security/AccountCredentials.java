package com.netflix.gateway.gateway.security;

/**
 * @author flaoliveira
 * @version : $<br/>
 * : $
 * @since 30/10/2019 23:14
 */
public class AccountCredentials {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
