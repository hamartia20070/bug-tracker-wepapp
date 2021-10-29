import { Component, Input, OnInit } from '@angular/core';
import { Project } from 'src/app/interfaces/project';
import { Ticket } from 'src/app/interfaces/ticket';
import { TicketService } from 'src/app/services/ticket/ticket.service';

@Component({
  selector: 'app-tickets-card',
  templateUrl: './tickets-card.component.html',
  styleUrls: ['./tickets-card.component.css']
})
export class TicketsCardComponent implements OnInit {

  constructor(public ticketService:TicketService) { }

  @Input()
  project!: Project;

  tickets!: Ticket[];

  pageNo=0;

  async ngOnInit(): Promise<void> {
    this.tickets = await this.ticketService.getTicketsOfProject(this.project.id,this.pageNo).toPromise();
  }
  public async nextTickets(){       
    if(this.tickets.length!=0){
      console.warn("click called");
      this.pageNo+=1;
      this.tickets = await this.ticketService.getTicketsOfProject(this.project.id,this.pageNo).toPromise();
    }
  }
  public async previousTickets(){
    if(this.pageNo!==0){
      this.pageNo-=1;
      console.warn("click called");
      this.tickets = await this.ticketService.getTicketsOfProject(this.project.id,this.pageNo).toPromise();
    }
  }

}
