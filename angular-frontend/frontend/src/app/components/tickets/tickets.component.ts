import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Tag } from 'src/app/interfaces/tag';
import { Ticket } from 'src/app/interfaces/ticket';
import { TicketService } from 'src/app/services/ticket/ticket.service';

@Component({
  selector: 'app-tickets',
  templateUrl: './tickets.component.html',
  styleUrls: ['./tickets.component.css']
})
export class TicketsComponent implements OnInit {

  constructor(public ticketService:TicketService,public router: Router) { 
    const navigation  = this.router.getCurrentNavigation();
    if(navigation){
     this.tag = navigation.extras.state ? navigation.extras.state.tag : {id:0,tag:{} as any, tickets: {} as any};
   }
  }

  tickets!: Ticket[];

  pageNo=0;
  sortBy="created";
  getBy="all"

  tag: Tag = {id:0,tag:{} as any, tickets: {} as any};


  async ngOnInit(): Promise<void> {
    this.loadTickets();
    
  }

  public async nextTickets(){       
    if(this.tickets.length!=0){
      console.warn("click called");
      this.pageNo+=1;
      this.loadTickets();
    }
  }
  public async previousTickets(){
    if(this.pageNo!==0){
      this.pageNo-=1;
      console.warn("click called");
      this.loadTickets();
    }
  }

  public async loadTickets(){
    this.tickets = await this.ticketService.getTicketsOfProjectsWithUser(this.pageNo,this.sortBy,this.getBy,this.tag.id).toPromise();
  }

  public changeSortBy(sortBy:string){
    this.sortBy = sortBy;
    this.loadTickets();
  }
  public changeGetBy(getBy:string){
    this.getBy = getBy;
    this.tag.id=0;
    this.loadTickets();
  }
  public changeTag(tag:Tag){
    this.tag=tag;
    this.loadTickets();
  }

}
