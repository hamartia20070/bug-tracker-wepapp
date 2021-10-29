import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { Priority } from 'src/app/interfaces/priority';
import { Ticket } from 'src/app/interfaces/ticket';
import { PriorityService } from 'src/app/services/priority/priority.service';
import { TicketService } from 'src/app/services/ticket/ticket.service';
import {NgbDateStruct, NgbCalendar, NgbDate} from '@ng-bootstrap/ng-bootstrap';
import { HttpErrorResponse } from '@angular/common/http';
import * as Notifications from 'src/app/interfaces/globalNotifications/notifications';

@Component({
  selector: 'app-edit-ticket',
  templateUrl: './edit-ticket.component.html',
  styleUrls: ['./edit-ticket.component.css'],
  styles: [`
  .custom-day {
    text-align: center;
    padding: 0.185rem 0.25rem;
    display: inline-block;
    height: 2rem;
    width: 2rem;
  }
  .custom-day.focused {
    background-color: #e6e6e6;
  }
  .custom-day.range, .custom-day:hover {
    background-color: rgb(2, 117, 216);
    color: white;
  }
  .custom-day.faded {
    background-color: rgba(2, 117, 216, 0.5);
  }
`]
})
export class EditTicketComponent implements OnInit {

  ticket !: Ticket;
  priorities!:Priority[];
  selectedPriority!: Priority;
  hoveredDate: NgbDate | null = null;

  fromDate!: NgbDate;
  toDate: NgbDate | null = null;
  constructor(public router:Router, public priorityService:PriorityService, public ticketService:TicketService,private calendar: NgbCalendar) { 
    const navigation  = this.router.getCurrentNavigation();
     if(navigation){
      this.ticket = navigation.extras.state ? navigation.extras.state.ticket : 0;
    }

    

    //On page refresh, the state is lost along with the object so I reroute the user back to tickets 
    if(this.ticket.id==null){
      this.router.navigateByUrl("/tickets");
    }
    
  }

  async ngOnInit(): Promise<void> {
    this.priorities = await this.priorityService.getPriorities().toPromise();
    this.fromDate = this.calendar.getToday();
    this.toDate = this.calendar.getNext(this.calendar.getToday(), 'd', 10);
    this.selectedPriority = this.ticket.ticketPriority;
    this.selectedStatus = this.ticket.status;
  }
  public setPriority(priority:Priority):void{
    this.selectedPriority=priority;
    

  }
  selectedStatus="";
  public setStatus(status:string):void{
    this.selectedStatus=status;
    

  }

  public async onUpdateTicket(updateTicketForm: NgForm):Promise<void>{
    const ticketTitle = updateTicketForm.form.get('ticket_title')?.value;
    const ticketDesc = updateTicketForm.form.get('ticket_desc')?.value;
    //apparently months start at 0 in js so we need to take 1 away....
    const startedDate = new Date(this.fromDate.year,this.fromDate.month-1,this.fromDate.day);
    if(this.toDate!==null){


      const deadline = new Date(this.toDate.year,this.toDate.month-1,this.toDate.day);
      console.warn(`Ticket title: ${ticketTitle}\n Ticket desc: ${ticketDesc}\n deadline: ${deadline}\n date started: ${startedDate}
      \n ticker prio: ${this.selectedPriority.priorityType}\n status: ${this.selectedStatus}\n project: ${this.ticket.project.id}`);

      this.ticket.ticketTitle= ticketTitle;
      this.ticket.ticketDesc=ticketDesc;
      this.ticket.ticketCreated=startedDate;
      this.ticket.deadline=deadline;
      this.ticket.status=this.selectedStatus;
      this.ticket.ticketPriority=this.selectedPriority;

      this.ticketService.updateTicket(this.ticket.id,this.selectedPriority.id,this.ticket).subscribe(
        (response: Ticket)=>  {
          //alert("Ticket Updated")
          Notifications.addNewAlert(`Ticket Updated: ${response.ticketTitle} `,"success");
          this.router.navigateByUrl('tickets/ticket', { state: { ticket: response } });
          
        },
        (error: HttpErrorResponse) =>{
          alert(error.message);
        }    
      )



    }else{alert("Deadline not selected");}
    




  }















  onDateSelection(date: NgbDate) {
    if (!this.fromDate && !this.toDate) {
      this.fromDate = date;
    } else if (this.fromDate && !this.toDate && date.after(this.fromDate)) {
      this.toDate = date;
    } else {
      this.toDate = null;
      this.fromDate = date;
    }
  }

  isHovered(date: NgbDate) {
    return this.fromDate && !this.toDate && this.hoveredDate && date.after(this.fromDate) && date.before(this.hoveredDate);
  }

  isInside(date: NgbDate) {
    return this.toDate && date.after(this.fromDate) && date.before(this.toDate);
  }

  isRange(date: NgbDate) {
    return date.equals(this.fromDate) || (this.toDate && date.equals(this.toDate)) || this.isInside(date) || this.isHovered(date);
  }

}
