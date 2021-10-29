package com.bugtracker.backend.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Priority.class)
public abstract class Priority_ {

	public static volatile SetAttribute<Priority, Project> projects;
	public static volatile SetAttribute<Priority, Ticket> tickets;
	public static volatile SingularAttribute<Priority, Integer> importance;
	public static volatile SingularAttribute<Priority, Integer> id;
	public static volatile SingularAttribute<Priority, String> priorityType;

	public static final String PROJECTS = "projects";
	public static final String TICKETS = "tickets";
	public static final String IMPORTANCE = "importance";
	public static final String ID = "id";
	public static final String PRIORITY_TYPE = "priorityType";

}

