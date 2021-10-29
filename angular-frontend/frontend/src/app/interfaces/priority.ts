import { Project } from "./project";
import { Ticket } from "./ticket";

export interface Priority{
    id: number;
    priorityType: string;
    importance: number;

    projects: Project[];
    tickets: Ticket[];
}