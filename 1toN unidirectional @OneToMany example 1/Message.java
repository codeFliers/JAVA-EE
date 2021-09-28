package com.example.jpaProject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name="messages")
public class Message implements Serializable {
    @Id
    @Column(table="messages", nullable = false)
    private Long identifiant;

    @Column(name="message", table="messages", length=255, nullable = false)
    private String message;
    @Column(table="messages", name="message_date", nullable = false)
    private Date messageDate;

    public Message(){
    }

    public Long getIdentifiant() {
        return identifiant;
    }
    public void setIdentifiant(Long identifiant) {
        this.identifiant = identifiant;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Date getMessageDate() {
        return messageDate;
    }
    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }
}
