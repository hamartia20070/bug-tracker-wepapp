package com.bugtracker.backend.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Role.class)
public abstract class Role_ {

	public static volatile SingularAttribute<Role, Integer> accessLevel;
	public static volatile SingularAttribute<Role, Integer> id;
	public static volatile SingularAttribute<Role, String> roleType;
	public static volatile SetAttribute<Role, ProjectUserRoleLink> projectUserRoleLinks;

	public static final String ACCESS_LEVEL = "accessLevel";
	public static final String ID = "id";
	public static final String ROLE_TYPE = "roleType";
	public static final String PROJECT_USER_ROLE_LINKS = "projectUserRoleLinks";

}

