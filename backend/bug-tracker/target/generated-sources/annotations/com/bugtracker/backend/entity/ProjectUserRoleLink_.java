package com.bugtracker.backend.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProjectUserRoleLink.class)
public abstract class ProjectUserRoleLink_ {

	public static volatile SingularAttribute<ProjectUserRoleLink, Role> role;
	public static volatile SingularAttribute<ProjectUserRoleLink, Project> project;
	public static volatile SingularAttribute<ProjectUserRoleLink, Integer> id;
	public static volatile SingularAttribute<ProjectUserRoleLink, User> user;

	public static final String ROLE = "role";
	public static final String PROJECT = "project";
	public static final String ID = "id";
	public static final String USER = "user";

}

