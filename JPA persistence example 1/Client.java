package com.example.jpaProject;


import javax.persistence.*;
import javax.persistence.Column;

//CLient is an entity
@Entity
//Rename the entity to clients with an 's'
@Table(name="clients")
//add a 'preferences' table
@SecondaryTable(name="preferences",
        //1 to 1 relationship between 'identifiant' FROM Client and
        //identifiant_client FROM Preferences
        pkJoinColumns=@PrimaryKeyJoinColumn(name="identifiant_client"),
        //name of the constraint
        foreignKey=@ForeignKey(name="fk_preferences_clients")
)

public class Client {
    @Column(length=45, name="name", table="clients", nullable = false)
    private String name;
    @Column(length=45, name="surname", table="clients", nullable = false)
    private String surname;
    @Column(length=45, name="email", table="clients",nullable = false)
    private String email;
    @Column(length=100, name="password", table="clients", nullable = false)
    private String password;
    @Column(name="identifiant", table="clients")
    @Id
    private Long identifiant;

    @Column(name="theme", table="preferences")
    private String theme;
    @Column(name="language", table="preferences")
    private String language;


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

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
