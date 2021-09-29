package com.example.jpaTest;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="clients")
public class Client implements Serializable {
    @Column(name="identifiant", table="clients")
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator="native"
    )
    private Long identifiant;

    @Column(length=45, name="name", table="clients", nullable = false)
    private String name;
    @Column(length=45, name="surname", table="clients", nullable = false)
    private String surname;
    @Column(length=45, name="email", table="clients",nullable = false)
    private String email;
    @Column(length=100, name="password", table="clients", nullable = false)
    private String password;


    public Client() {
        //
    }
    public Client(String name, String surname, String email, String password) {
        //this.identifiant = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Long getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(Long identifiant) {
        this.identifiant = identifiant;
    }

}
