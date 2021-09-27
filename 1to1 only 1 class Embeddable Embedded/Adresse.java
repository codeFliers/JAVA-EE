package com.example.jpaProject;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Adresse implements Serializable {
    @Column(name="rue", nullable = false, length=255)
    private String rue;
    @Column(name="codePostal", nullable = false, length=5)
    private int codePostal;
    @Column(name="ville", nullable = false, length=255)
    private String ville;

    public Adresse(){}

    public String getRue() {
        return rue;
    }
    public void setRue(String rue) {
        this.rue = rue;
    }


    public int getCodePostal() {
        return codePostal;
    }
    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }


    public String getVille() {
        return ville;
    }
    public void setVille(String ville) {
        this.ville = ville;
    }


}
