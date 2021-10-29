import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { Priority } from 'src/app/interfaces/priority';
import { Ticket } from 'src/app/interfaces/ticket';
import { PriorityService } from 'src/app/services/priority/priority.service';
import { TicketService } from 'src/app/services/ticket/ticket.service';
import {NgbDateStruct, NgbCalendar, NgbDate} from '@ng-bootstrap/ng-bootstrap';
import { HttpErrorResponse } from '@angular/common/http';
import { Project } from 'src/app/interfaces/project';
import { AuthService } from 'src/app/services/auth/auth.service';
import * as Notifications from 'src/app/interfaces/globalNotifications/notifications';


@Component({
  selector: 'app-add-ticket',
  templateUrl: './add-ticket.component.html',
  styleUrls: ['./add-ticket.component.css'],
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
export class AddTicketComponent implements OnInit {


  project!: Project;
  priorities!:Priority[];
  selectedPriority!: Priority;
  hoveredDate: NgbDate | null = null;

  fromDate!: NgbDate;
  toDate: NgbDate | null = null;

  selectedStatus="";

  constructor(public router:Router, public priorityService:PriorityService, public ticketService:TicketService,private calendar: NgbCalendar,
    private authService:AuthService) { 
    const navigation  = this.router.getCurrentNavigation();
    if(navigation){
     this.project = navigation.extras.state ? navigation.extras.state.project : 0;
   }

   

   //On page refresh, the state is lost along with the object so I reroute the user back to projects 
   if(this.project.id==null){
     this.router.navigateByUrl("/projects");
   }
  }

  async ngOnInit(): Promise<void> {
    this.priorities = await this.priorityService.getPriorities().toPromise();
    this.fromDate = this.calendar.getToday();
    this.toDate = this.calendar.getNext(this.calendar.getToday(), 'd', 10);
  }
  public setPriority(priority:Priority):void{
    this.selectedPriority=priority;
  }
  public setStatus(status:string):void{
    this.selectedStatus=status;
  }

  public async onAddTicket(addTicketForm: NgForm):Promise<void>{
    const ticketTitle = addTicketForm.form.get('ticket_title')?.value;
    const ticketDesc = addTicketForm.form.get('ticket_desc')?.value;
    //apparently months start at 0 in js so we need to take 1 away....
    const startedDate = new Date(this.fromDate.year,this.fromDate.month-1,this.fromDate.day);
    let userId= localStorage.getItem("user_id") ? Number(localStorage.getItem("user_id")) : 0;

    if(this.toDate!==null){
      const deadline = new Date(this.toDate.year,this.toDate.month-1,this.toDate.day);

      console.log(`Ticket title: ${ticketTitle}\n Ticket desc: ${ticketDesc}\n deadline: ${deadline}\n date started: ${startedDate}
      \n ticker prio: ${this.selectedPriority.priorityType}\n status: ${this.selectedStatus}\n project: ${this.project.id}`);

      let newTicket: Ticket = {
        ticketTitle: ticketTitle,
        ticketDesc: ticketDesc,
        ticketCreated: startedDate,
        deadline: deadline,
        status: this.selectedStatus,
        ticketPriority: {} as any,
        id: 0,
        project: {} as any,
        submitUser: {} as any,
        assignedUser: {} as any,
        comments: {} as any,
        ticketHistory: {} as any,
        tags: []//this being null gave error idk
      }
      //creates 2 new user, new priority, new project and adds one of the two new users as assigned user
      this.ticketService.addTicket(this.selectedPriority.id,this.project.id,userId,newTicket).subscribe(
        (response: Ticket)=>  {
          //alert("Ticket Added")
          Notifications.addNewAlert(`Ticket Created: ${response.ticketTitle} `,"success");
          this.router.navigateByUrl('tickets/ticket', { state: { ticket: response } });
          
        },
        (error: HttpErrorResponse) =>{
          alert("You have no access rights to the project according to your token");
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
