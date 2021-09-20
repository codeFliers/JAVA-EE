package com.example.jpaProject;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Client {
    private String name;
    private String surname;
    private String email;
    private Long id;

    public Client() {
        //
    }

    protected void setName(String name) {
        this.name = name;
    }
    protected void setSurname(String surname) {
        this.surname = surname;
    }
    protected void setEmail(String email) {
        this.email = email;
    }

    protected String getName() {
        return name;
    }
    protected String getSurname() {
        return surname;
    }
    protected String getEmail() {
        return email;
    }

    public void setId(Long id) { this.id = id; }
    @Id
    public Long getId() { return id; }
}
