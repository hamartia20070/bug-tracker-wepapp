package com.bugtracker.backend.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Project.class)
public abstract class Project_ {

	public static volatile ListAttribute<Project, Ticket> tickets;
	public static volatile SingularAttribute<Project, Date> dateStarted;
	public static volatile SingularAttribute<Project, String> projectDescription;
	public static volatile SingularAttribute<Project, Integer> id;
	public static volatile SingularAttribute<Project, Boolean> projectOpen;
	public static volatile SingularAttribute<Project, Priority> projectPriority;
	public static volatile SingularAttribute<Project, String> projectName;
	public static volatile ListAttribute<Project, ProjectUserRoleLink> projectUserRoleLinks;

	public static final String TICKETS = "tickets";
	public static final String DATE_STARTED = "dateStarted";
	public static final String PROJECT_DESCRIPTION = "projectDescription";
	public static final String ID = "id";
	public static final String PROJECT_OPEN = "projectOpen";
	public static final String PROJECT_PRIORITY = "projectPriority";
	public static final String PROJECT_NAME = "projectName";
	public static final String PROJECT_USER_ROLE_LINKS = "projectUserRoleLinks";

}

