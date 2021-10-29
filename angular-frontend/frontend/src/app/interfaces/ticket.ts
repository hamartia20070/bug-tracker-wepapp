import { Project } from "./project";
import { User } from "./user";
import { Comment } from "./comment";
import { TicketHistory } from "./ticket-history";
import { Tag } from "./tag";
import { Priority } from "./priority";

export interface Ticket{
    id: number;
    ticketTitle: string;
    ticketDesc: string;
    ticketCreated: Date;
    deadline: Date;
    status: string;

    project: Project;
    submitUser: User;
    assignedUser: User;
    comments:Comment[];
    ticketHistory:TicketHistory[];
    ticketPriority: Priority;
    tags:Tag[];   
    
}