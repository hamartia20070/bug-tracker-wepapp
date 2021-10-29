import { Ticket } from "./ticket";

export interface Tag{
    id: number;
    tag: string;
    tickets: Ticket[];
}