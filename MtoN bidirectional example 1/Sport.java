package com.example.jpaProject;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="sports")
public class Sport {
    @Id
    //@Column(name="identifiant", table="sports", nullable = false)
    private Long idendifiant;
    //@Column(name="name", table="sports", nullable = false)
    private String name;
    //@Column(name="number_Players", table="sports", nullable = false)
    private int number_Players;

    @ManyToMany(mappedBy = "allowedSport")
    private List<Field> compatibleFields;

    public List<Field> getCompatibleFields() {
        return compatibleFields;
    }

    public void setCompatibleFields(List<Field> compatibleFields) {
        this.compatibleFields = compatibleFields;
    }

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
