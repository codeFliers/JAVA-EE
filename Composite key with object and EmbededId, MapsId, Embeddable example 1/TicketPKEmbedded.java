package com.example.jpaProject;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class TicketPKEmbedded implements Serializable {
    private int idClient;
    private int idSport;

    public TicketPKEmbedded() {
        //
    }

    public int getIdClient() {
        return idClient;
    }
    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getIdSport() {
        return idSport;
    }
    public void setIdSport(int idSport) {
        this.idSport = idSport;
    }

    @Override
    public boolean equals(Object o) {
        Boolean bool = false;
        if(o != null && o instanceof TicketPKEmbedded) {
            TicketPKEmbedded ticketPKEmbedded = (TicketPKEmbedded) o;

            if(ticketPKEmbedded.getIdClient() == this.getIdClient() &&
                    ticketPKEmbedded.getIdSport() == this.getIdSport()) {
                bool = true;
            }
        }
        return bool;
    }

    @Override
    public int hashCode() {
        return this.getIdSport()+this.getIdClient();
    }
}
