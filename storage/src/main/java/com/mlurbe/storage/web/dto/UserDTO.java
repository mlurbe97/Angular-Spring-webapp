package com.mlurbe.storage.web.dto;

import com.mlurbe.storage.models.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private long id;
    private String username;
    private String role;
    private String email;
    private boolean enabled;

    public UserDTO(User user) {
        super();
        this.id = user.getUserId();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.email = user.getEmail();
        this.enabled = user.getEnabled();
    }

    public String getUsername () {
        return this.username;
    }

    public void setUsername (String username) {
        this.username = username;
    }

    public long getId () {
        return this.id;
    }

    public void setId (long id) {
        this.id = id;
    }

    public String getRole () {
        return this.role;
    }

    public void setRole (String role) {
        this.role = role;
    }

    public String getEmail () {
        return this.email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public void setEnabled (boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getEnabled () {
        return this.enabled;
    }
}