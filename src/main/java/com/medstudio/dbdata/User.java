package com.medstudio.dbdata;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by krzysztof.mazur on 2016-12-13.
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    private String name;

    @NotNull
    private boolean enabled;

    @NotNull
    private String password;

    public User(String name, boolean enabled, String password) {
        this.name = name;
        this.enabled = enabled;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
