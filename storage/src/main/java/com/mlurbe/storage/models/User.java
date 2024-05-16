package com.mlurbe.storage.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_seq", allocationSize = 1)
    @Column(name = "user_uuid", nullable = false, unique = true)
    private Long userId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "last_password_reset_date", nullable = false)
    private Timestamp lastPasswordResetDate;

    @Column(name = "create_time", nullable = false)
    private Timestamp create_time;


    /*
     * VALORES PARA ROLE
     */
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    /*
     * VALORES PARA ENABLED
     */
    public static final boolean NOT_ENABLED = false;
    public static final boolean ENABLED = true;

    public User(String username, String password, String email, boolean enabled, Timestamp lastPasswordResetDate, Timestamp create_time, String role) {
        super();
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.create_time = create_time;
        this.role = role;
    }

    public void setPassword (String password) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        this.setLastPasswordResetDate(now);

        this.password = password;
    }

    public String getPassword () {
        return password;
    }

    public Long getUserId () {
        return userId;
    }

    public String getUsername () {
        return username;
    }

    public String getEmail () {
        return email;
    }

    public boolean getEnabled () {
        return enabled;
    }

    public Timestamp getLastPasswordResetDate () {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate (Timestamp lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public Timestamp getCreateTime () {
        return create_time;
    }

    public Long getId () {
        return userId;
    }

    public void setId (Long Id) {
        this.userId = Id;
    }

    public String getRole() {
        return this.role;
    }

    public List<String> getRoles () {
        List<String> roles = new ArrayList<String>();
        roles.add(role);
        return roles;
    }

    public void getRole (String role) {
        this.role = role;
    }

}
