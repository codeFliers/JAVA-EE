package com.example.jpaProject;


import javax.persistence.*;
import javax.persistence.Column;

@Entity
@Table(name="clients")
@IdClass(ClientPK.class)
public class Client {
    @Id
    @Column(length=45, name="name", table="clients", nullable = false)
    private String name;
    @Id
    @Column(length=45, name="surname", table="clients", nullable = false)
    private String surname;

    public Client() {
        //
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
