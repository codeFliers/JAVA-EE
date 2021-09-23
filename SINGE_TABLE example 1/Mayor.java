package com.example.jpaProject;

import javax.persistence.*;

@Entity
@DiscriminatorValue("MAYOR")
public class Mayor extends Person {
    @OneToOne
    private Town town;

    public Mayor(){
    }
}
