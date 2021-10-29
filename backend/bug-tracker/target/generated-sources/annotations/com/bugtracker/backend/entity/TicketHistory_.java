package com.bugtracker.backend.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TicketHistory.class)
public abstract class TicketHistory_ {

	public static volatile SingularAttribute<TicketHistory, Date> dateChanged;
	public static volatile SingularAttribute<TicketHistory, Ticket> ticket;
	public static volatile SingularAttribute<TicketHistory, String> oldTitle;
	public static volatile SingularAttribute<TicketHistory, String> oldDesc;
	public static volatile SingularAttribute<TicketHistory, Integer> id;

	public static final String DATE_CHANGED = "dateChanged";
	public static final String TICKET = "ticket";
	public static final String OLD_TITLE = "oldTitle";
	public static final String OLD_DESC = "oldDesc";
	public static final String ID = "id";

}

