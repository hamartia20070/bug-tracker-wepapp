package com.bugtracker.backend.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Comment.class)
public abstract class Comment_ {

	public static volatile SingularAttribute<Comment, Ticket> ticket;
	public static volatile SingularAttribute<Comment, Integer> id;
	public static volatile SingularAttribute<Comment, String> message;
	public static volatile SingularAttribute<Comment, User> user;

	public static final String TICKET = "ticket";
	public static final String ID = "id";
	public static final String MESSAGE = "message";
	public static final String USER = "user";

}

