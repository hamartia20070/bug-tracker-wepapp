package com.bugtracker.backend.entity;

public enum TicketStatus {
    OPEN("Open"),
    CLOSED("Closed"),
    ONHOLD("On Hold");

    private final String status;

    TicketStatus(String status){ this.status=status;}

    @Override
    public String toString(){return status;}
}
