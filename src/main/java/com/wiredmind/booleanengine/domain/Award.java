package com.wiredmind.booleanengine.domain;

/**
 * Award.java
 * Created by Craig Earley on 4/12/13.
 * Copyright (c) 2013 Wired-Mind Labs, LLC.
 */
public class Award {

    public static final String AWARD_KEY = "award";
    private String description;
    private double amount;

    public Award() {
    }

    public Award(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    /**
     * Get the value of amount
     *
     * @return the value of amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Set the value of amount
     *
     * @param amount new value of amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Get the value of description
     *
     * @return the value of description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the value of description
     *
     * @param description new value of description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
