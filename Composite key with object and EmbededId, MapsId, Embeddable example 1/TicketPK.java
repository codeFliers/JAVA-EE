package com.example.jpaProject;

import java.io.Serializable;
public class TicketPK implements Serializable {
    private int client;
    private int sport;

    public TicketPK() {
        //
    }

    public void setClient(int client) {
        this.client = client;
    }
    public int getClient() {
        return this.client;
    }

    public void setSport(int sport) {
        this.sport = sport;
    }
    public int getSport() {
        return this.sport;
    }

    @Override
    public int hashCode() {
        return this.getClient() + this.getSport();
    }
    @Override
    public boolean equals(Object o) {
        boolean boolRes = false;
        if (o != null && o instanceof TicketPK) {
            TicketPK ticketPK = (TicketPK) o;
            boolRes = ( ticketPK.getClient() == this.getClient() && ticketPK.getSport() == this.getSport() );
        }
        return boolRes;
    }
}
