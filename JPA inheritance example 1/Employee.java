package com.example.jpaProject;

import javax.persistence.*;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@DiscriminatorColumn(name = "EMP_TYPE")
public class Employee {
    @Id
    @GeneratedValue
    private long id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return
                "id=" + id +
                        ", name='" + name + '\'';
    }
}
