package com.example.jpaProject;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@DiscriminatorColumn(name="TYPE_ENTITY")
@DiscriminatorValue("PERSON")
public class Person implements Serializable {
    @Id
    private Long id;
    @Column(length=40)
    private String name;
    @Column(length=40)
    private String surname;

    public Person(){
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
