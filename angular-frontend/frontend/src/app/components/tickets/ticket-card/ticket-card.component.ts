import { DatePipe } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { Tag } from 'src/app/interfaces/tag';
import { Ticket } from 'src/app/interfaces/ticket';
import { TicketService } from 'src/app/services/ticket/ticket.service';

@Component({
  selector: 'app-ticket-card',
  templateUrl: './ticket-card.component.html',
  styleUrls: ['./ticket-card.component.css']
})
export class TicketCardComponent implements OnInit {

  @Input()
  ticket!: Ticket;

  commentCount=0;
  currentUsername = localStorage.getItem("username");

  today:Date = new Date();

  deadlineBool=false;

  @Output() onTagClicked = new EventEmitter<any>();
  

  constructor(public ticketService:TicketService,public router: Router, public datepipe:DatePipe) { }

  async ngOnInit(): Promise<void> {
    this.commentCount = await this.ticketService.getCommentsCountForTicket(this.ticket.id).toPromise();

    if(Date.parse(this.ticket?.deadline.toString())<Date.parse(this.today.toString())){
      console.warn("pog");
      this.deadlineBool=true;
    }
  }
  public openTicket(){
    this.router.navigateByUrl('tickets/ticket', { state: { ticket: this.ticket } });
    
  }

  public onTagClick(tag:Tag){
      this.onTagClicked.emit(tag);
  }

}
