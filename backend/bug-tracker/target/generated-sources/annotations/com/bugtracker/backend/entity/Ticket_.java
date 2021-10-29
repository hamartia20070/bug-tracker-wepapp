package com.bugtracker.backend.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Ticket.class)
public abstract class Ticket_ {

	public static volatile SingularAttribute<Ticket, String> ticketTitle;
	public static volatile ListAttribute<Ticket, Comment> comments;
	public static volatile SingularAttribute<Ticket, Project> project;
	public static volatile SetAttribute<Ticket, Tag> tags;
	public static volatile SingularAttribute<Ticket, Date> ticketCreated;
	public static volatile SingularAttribute<Ticket, User> submitUser;
	public static volatile SingularAttribute<Ticket, Priority> ticketPriority;
	public static volatile SetAttribute<Ticket, TicketHistory> ticketHistory;
	public static volatile SingularAttribute<Ticket, String> ticketDesc;
	public static volatile SingularAttribute<Ticket, Integer> id;
	public static volatile SingularAttribute<Ticket, Date> deadline;
	public static volatile SingularAttribute<Ticket, User> assignedUser;
	public static volatile SingularAttribute<Ticket, TicketStatus> status;

	public static final String TICKET_TITLE = "ticketTitle";
	public static final String COMMENTS = "comments";
	public static final String PROJECT = "project";
	public static final String TAGS = "tags";
	public static final String TICKET_CREATED = "ticketCreated";
	public static final String SUBMIT_USER = "submitUser";
	public static final String TICKET_PRIORITY = "ticketPriority";
	public static final String TICKET_HISTORY = "ticketHistory";
	public static final String TICKET_DESC = "ticketDesc";
	public static final String ID = "id";
	public static final String DEADLINE = "deadline";
	public static final String ASSIGNED_USER = "assignedUser";
	public static final String STATUS = "status";

}

