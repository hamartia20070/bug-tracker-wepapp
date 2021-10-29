import { Component, OnInit } from '@angular/core';
import { Ticket } from 'src/app/interfaces/ticket';
import { TicketService } from 'src/app/services/ticket/ticket.service';
import { DatePipe } from '@angular/common';
import { ThrowStmt } from '@angular/compiler';
import { Router } from '@angular/router';

@Component({
  selector: 'app-mini-tickets-menu-card',
  templateUrl: './mini-tickets-menu-card.component.html',
  styleUrls: ['./mini-tickets-menu-card.component.css']
})
export class MiniTicketsMenuCardComponent implements OnInit {

  active=1;
  pageNo=0;
  pageNoCreatedTab=0;
  constructor(private ticketService:TicketService,public datepipe: DatePipe,public router: Router) { }
  assignedTickets!: Ticket[];
  createdTickets!: Ticket[];
  async ngOnInit(): Promise<void> {
    this.assignedTickets = await this.ticketService.getAssignedTicketsOfUser(this.pageNo).toPromise();
    this.createdTickets = await this.ticketService.getCreatedTicketsOfUser(this.pageNoCreatedTab).toPromise();
  }

  public async nextTickets(){       
    if(this.assignedTickets.length!=0){
      console.warn("click called");
      this.pageNo+=1;
      this.assignedTickets = await this.ticketService.getAssignedTicketsOfUser(this.pageNo).toPromise();
    }
  }
  public async previousTickets(){
    if(this.pageNo!==0){
      this.pageNo-=1;
      console.warn("click called");
      this.assignedTickets = await this.ticketService.getAssignedTicketsOfUser(this.pageNo).toPromise();
    }
  }

  public async nextCreatedTickets(){     
    if(this.createdTickets.length!=0){
      console.warn("click called");
      this.pageNoCreatedTab+=1;
      this.createdTickets = await this.ticketService.getCreatedTicketsOfUser(this.pageNoCreatedTab).toPromise();
    }
  }
  public async previousCreatedTickets(){
    if(this.pageNoCreatedTab!==0){
      this.pageNoCreatedTab-=1;
      console.warn("click called");
      this.createdTickets = await this.ticketService.getCreatedTicketsOfUser(this.pageNoCreatedTab).toPromise();
    }
  } 

  public openTicket(ticket:Ticket){
    this.router.navigateByUrl('tickets/ticket', { state: { ticket: ticket } });
    
  }
}
