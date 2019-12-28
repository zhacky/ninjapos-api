package com.zhacky.ninjapos.data.request;

import java.io.Serializable;

public class JwtAuthByPinRequest implements Serializable {

    private static final long serialVersionUID = -4001888679264164126L;

    private String username;
    private Integer pin;

    public JwtAuthByPinRequest(String username, Integer pin) {
        this.username = username;
        this.pin = pin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }
}
