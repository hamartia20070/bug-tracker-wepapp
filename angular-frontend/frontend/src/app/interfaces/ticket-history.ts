import { Ticket } from "./ticket";

export interface TicketHistory{
    id: number;
    oldTitle: string;
    oldDesc: string;
    dateChanged: Date;

    ticket: Ticket;
}