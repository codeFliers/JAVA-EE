package com.example.jpaProject;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="addresses")
public class Address implements Serializable {
    @Id
    private int identifiant;

    @Column(name="rue", nullable = false, length=255)
    private String rue;
    @Column(name="codePostal", nullable = false, length=5)
    private int codePostal;
    @Column(name="ville", nullable = false, length=255)
    private String ville;

    //name of the variable on the client.java side
    @OneToOne(mappedBy = "address")
    private Client client;

    public Address(){}

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
