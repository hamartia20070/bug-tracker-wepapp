import { Priority } from "./priority";
import { ProjectUserRoleLink } from "./project-user-role-link";
import { Ticket } from "./ticket";

export interface Project{
    id: number;
    projectName: string;
    projectDescription: string;
    projectOpen: boolean;
    dateStarted: Date;

    projectPriority: Priority;
    tickets: Ticket[];
    projestUserRoleLinks: ProjectUserRoleLink[];
}