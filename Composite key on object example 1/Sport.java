package com.example.jpaProject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Sport {
    @Id
    @Column(name="identifiant", table="Sport", nullable = false)
    private Long idendifiant;
    @Column(name="name", table="Sport", nullable = false)
    private String name;
    @Column(name="number_Players", table="Sport", nullable = false)
    private int number_Players;

    public Sport(){
        //
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getNumber_Players() {
        return number_Players;
    }
    public void setNumber_Players(int number_Players) {
        this.number_Players = number_Players;
    }

    public void setIdendifiant(Long idendifiant) {
        this.idendifiant = idendifiant;
    }
    public Long getIdendifiant() {
        return idendifiant;
    }

}
