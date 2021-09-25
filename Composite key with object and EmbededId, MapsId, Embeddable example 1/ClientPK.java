package com.example.jpaProject;

import java.io.Serializable;

public class ClientPK implements Serializable {
    private String name;
    private String surname;

    public ClientPK() {
        //
    }

    public ClientPK(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    //GETTERS && SETTERS =>
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getSurname() {
        return this.surname;
    }

    //<=
    @Override
    public int hashCode() {
        return this.name.hashCode() + this.surname.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof ClientPK){
            ClientPK clientPK = (ClientPK) o;

            if (this.getName().equals(clientPK.getName())) {
                if(this.getSurname().equals(clientPK.getSurname())) {
                    return true;
                }
            }
        }
        return false;
    }
}
