package com.example.jpaProject;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("P")
@Table(name = "PART_TIME_EMP")
public class PartTimeEmployee extends Employee {
    private int hourlyRate;

    public int getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(int hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public String toString() {
        return "PartTimeEmployee{" +
                super.toString() +
                ", hourlyRate='" + hourlyRate + '\'' +
                '}';
    }
}