package com.bugtracker.backend.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Tag.class)
public abstract class Tag_ {

	public static volatile SetAttribute<Tag, Ticket> tickets;
	public static volatile SingularAttribute<Tag, Integer> id;
	public static volatile SingularAttribute<Tag, String> tag;

	public static final String TICKETS = "tickets";
	public static final String ID = "id";
	public static final String TAG = "tag";

}

