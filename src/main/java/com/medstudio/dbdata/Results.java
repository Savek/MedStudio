package com.medstudio.dbdata;

import javax.persistence.Entity;

/**
 * Created by Savek on 2016-12-10.
 */

public class Results {
    private String firstName;
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
