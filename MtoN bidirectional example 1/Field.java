package com.example.jpaProject;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="fields")
public class Field implements Serializable {
    @Id
    private Long identifiant;
    private String code;
    private int surface;

    public List<Sport> getAllowedSport() {
        return allowedSport;
    }

    public void setAllowedSport(List<Sport> allowedSport) {
        this.allowedSport = allowedSport;
    }

    public Field() {
        
    }
    @ManyToMany
    @JoinTable(
    name="fields_sports",
    joinColumns=@JoinColumn(name="identifiant_field",
        foreignKey=@ForeignKey(name="fk_fields"), nullable = false),
        inverseJoinColumns=@JoinColumn(name="identifiant_sport",
        foreignKey=@ForeignKey(name="fk_sports"), nullable = false))
        private List<Sport> allowedSport;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public int getSurface() {
        return surface;
    }
    public void setSurface(int surface) {
        this.surface = surface;
    }

    public void setIdentifiant(Long identifiant) {
        this.identifiant = identifiant;
    }
    public Long getIdentifiant() {
        return identifiant;
    }
}
