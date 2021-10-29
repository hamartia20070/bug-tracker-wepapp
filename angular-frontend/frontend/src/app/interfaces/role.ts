import { ProjectUserRoleLink } from "./project-user-role-link";

export interface Role{
    id: number;
    roleType: string;
    accessLevel: number;
   projectUserRoleLinks:ProjectUserRoleLink[];
}