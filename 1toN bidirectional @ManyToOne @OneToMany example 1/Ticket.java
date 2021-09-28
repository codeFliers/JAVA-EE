package com.example.jpaProject;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="TICKETS")
@IdClass(TicketPK.class)
public class Ticket implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name="identifiant_client",
            foreignKey=@ForeignKey(name="FK_TICKETS_CLIENTS"))
    private Client client;

    @Id
    @ManyToOne
    @JoinColumn(name="identifiant_sport",
            foreignKey=@ForeignKey(name="FK_TICKETS_SPORTS"))
    //@Column(name="sport", table="TICKETS", nullable = false)
    private Sport sport;

    @Column(name="number_Entry", nullable = false)
    private int number_Entry;


    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public Sport getSport() {
        return sport;
    }
    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public int getNumber_Entry() {
        return number_Entry;
    }
    public void setNumber_Entry(int number_Entry) {
        this.number_Entry = number_Entry;
    }


}
