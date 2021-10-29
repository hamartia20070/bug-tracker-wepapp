import { Ticket } from "./ticket";
import { User } from "./user";

export interface Comment{
    id: number;
    ticket: Ticket;
    message: string;

    user: User;
    
}