package com.bugtracker.backend.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SingularAttribute<User, String> password;
	public static volatile SetAttribute<User, Comment> comments;
	public static volatile SingularAttribute<User, String> userPhoneNo;
	public static volatile SetAttribute<User, Ticket> ticketsAssigned;
	public static volatile SetAttribute<User, Ticket> ticketsMade;
	public static volatile SingularAttribute<User, String> userEmail;
	public static volatile SingularAttribute<User, Integer> id;
	public static volatile SingularAttribute<User, String> userName;
	public static volatile SetAttribute<User, ProjectUserRoleLink> projectUserRoleLinks;

	public static final String PASSWORD = "password";
	public static final String COMMENTS = "comments";
	public static final String USER_PHONE_NO = "userPhoneNo";
	public static final String TICKETS_ASSIGNED = "ticketsAssigned";
	public static final String TICKETS_MADE = "ticketsMade";
	public static final String USER_EMAIL = "userEmail";
	public static final String ID = "id";
	public static final String USER_NAME = "userName";
	public static final String PROJECT_USER_ROLE_LINKS = "projectUserRoleLinks";

}

