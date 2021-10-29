import { DatePipe } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Ticket } from 'src/app/interfaces/ticket';

@Component({
  selector: '[app-tickets-card-row]',
  templateUrl: './tickets-card-row.component.html',
  styleUrls: ['./tickets-card-row.component.css']
})
export class TicketsCardRowComponent implements OnInit {

  constructor(public datepipe:DatePipe,public router: Router ) { }


  @Input()
  ticket!:Ticket;
  

  ngOnInit(): void {
  }
  public openTicket(){
    this.router.navigateByUrl('tickets/ticket', { state: { ticket: this.ticket } });
    
  }

}
