package Models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Embeddable
public class Adresse implements Serializable {
    @Column(name="adresse", nullable = false, length=255)
    String adresse;

    @Column(name="code_postal")
    String codePostal;

    @Column(name="ville", nullable = false, length=255)
    String ville;

    public Adresse() {
    }

    public void setCodePostal(String v) {codePostal = v;}
    public String getCodePostal() {return codePostal;}

    public void setVille(String v) {ville = v;}
    public String getVille() {return ville;}
}