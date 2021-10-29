import { Project } from "./project";
import { Role } from "./role";
import { User } from "./user";

export interface ProjectUserRoleLink{
    id: number;

    project: Project;
    user: User;
    role: Role;
}