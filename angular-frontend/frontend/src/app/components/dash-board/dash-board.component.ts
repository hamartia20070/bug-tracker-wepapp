import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BarchartItem } from 'src/app/interfaces/statInterfaces/barchart-items';
import { Ticket } from 'src/app/interfaces/ticket';
import { ProjectService } from 'src/app/services/project/project.service';
import { TicketService } from 'src/app/services/ticket/ticket.service';

@Component({
  selector: 'app-dash-board',
  templateUrl: './dash-board.component.html',
  styleUrls: ['./dash-board.component.css']
})
export class DashBoardComponent implements OnInit {

  view: [number,number] = [400, 200];
  showLegend = false;
  showLabels = false;
  doughnut = true;

  ticketsByPriority!: BarchartItem[];
  projectsByPriority!: BarchartItem[];
  projectsByActiveTickets!: BarchartItem[];
  //projects by active tickets count
  allAndFinishedTickets!: number[];
  userAssignedAndCreatedTicketsCount!: number[];
  allProjectCount!: number;

  tickets!: Ticket[];

  constructor(private ticketService:TicketService, private projectService:ProjectService, private router:Router,public datepipe: DatePipe) { }
 
  async ngOnInit(): Promise<void> {
    this.ticketsByPriority = await this.ticketService.getTicketsCountByPriority().toPromise();
    this.projectsByPriority = await this.projectService.getProjectsCountByPriority().toPromise();
    this.projectsByActiveTickets = await this.projectService.getActiveTicketsCountForAllProjects().toPromise();
    this.allAndFinishedTickets = await this.ticketService.getTicketsCountAllAndFnished().toPromise();
    this.allProjectCount = await this.projectService.getAllProjectsCount().toPromise();
    const userId = localStorage.getItem("user_id");
    if(userId)
    this.userAssignedAndCreatedTicketsCount = await this.ticketService.getUserAssignedAndCreatedTicketsCount(Number(userId)).toPromise();
    this.tickets = await this.ticketService.getTicketsOfProjectsWithUser(0,"created","all",0).toPromise();
  }
  public openTicket(ticket:Ticket){
    this.router.navigateByUrl('tickets/ticket', { state: { ticket: ticket } });
    
  }

}
