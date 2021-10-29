import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Ticket } from 'src/app/interfaces/ticket';
import { TicketHistory } from 'src/app/interfaces/ticket-history';
import { Comment } from 'src/app/interfaces/comment';
import { TicketService } from 'src/app/services/ticket/ticket.service';
import { NgForm } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { UserServiceService } from 'src/app/services/user/user-service.service';
import { User } from 'src/app/interfaces/user';
import { Tag } from 'src/app/interfaces/tag';
import { TagService } from 'src/app/services/tag/tag.service';
import { NgbDropdownConfig } from '@ng-bootstrap/ng-bootstrap';
import * as Notifications from 'src/app/interfaces/globalNotifications/notifications'; 

@Component({
  selector: 'app-ticket',
  templateUrl: './ticket.component.html',
  styleUrls: ['./ticket.component.css'],
  providers: [NgbDropdownConfig] 
})
export class TicketComponent implements OnInit {

  ticket!: Ticket;
  ticketHistory!: TicketHistory[];
  ticketHistoryPageNo=0;

  ticketComments!: Comment[];
  commentsPageNo=0;

  editTicketButtonDisabled=false;

  allTags!: Tag[];
  selectedTags: Tag[] = [];

  //for selecting tags to add, already added tags button will be disabled forever
  foreverDisabledButtons: any = [];


  constructor(public router:Router, public datepipe:DatePipe,public ticketService:TicketService, public userService:UserServiceService,
    public tagService: TagService) {
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
    this.ticketHistory = await this.ticketService.getTicketHistory(this.ticket.id,this.ticketHistoryPageNo).toPromise();
    this.ticketComments= await this.ticketService.getTicketComments(this.ticket.id,this.commentsPageNo).toPromise();
    this.allTags = await this.tagService.getTags().toPromise();

    //for adding tags, I filter out the tags already added to avoid duplication
    this.allTags = this.allTags.filter((cv) =>{
      return !this.ticket.tags.find(function(e:Tag){
          return e.id == cv.id;
      });
    });

    const username = localStorage.getItem("username");
    if(this.ticket.submitUser.userName!==username){
      this.editTicketButtonDisabled=true;
    }
  }
  public openEditTicket(){
    this.router.navigateByUrl('tickets/ticket/edit', { state: { ticket: this.ticket } });
  }

  public async nextTicketHistory(){       
    if(this.ticketHistory.length!=0){
      console.warn("click called");
      this.ticketHistoryPageNo+=1;
      this.ticketHistory = await this.ticketService.getTicketHistory(this.ticket.id,this.ticketHistoryPageNo).toPromise();
    }
  }
  public async previousTicketHistory(){
    if(this.ticketHistoryPageNo!==0){
      this.ticketHistoryPageNo-=1;
      console.warn("click called");
      this.ticketHistory = await this.ticketService.getTicketHistory(this.ticket.id,this.ticketHistoryPageNo).toPromise();
    }
  }


  public async nextComments(){       
    if(this.ticketComments.length!=0){
      console.warn("click called");
      this.commentsPageNo+=1;
      this.ticketComments= await this.ticketService.getTicketComments(this.ticket.id,this.commentsPageNo).toPromise();
    }
  }
  public async previousComments(){
    if(this.commentsPageNo!==0){
      this.commentsPageNo-=1;
      console.warn("click called");
      this.ticketComments= await this.ticketService.getTicketComments(this.ticket.id,this.commentsPageNo).toPromise();
    }
  }





  public async onAddComment(addCommentForm: NgForm):Promise<void>{
    const comment = addCommentForm.form.get('comment_message')?.value;
    let userId= localStorage.getItem("user_id") ? Number(localStorage.getItem("user_id")) : 0;

    let newComment: Comment = {
      message: comment,
      ticket: this.ticket,
      id: 0,
      user: {} as any
    }

    this.ticketService.addComment(this.ticket.id,userId,newComment).subscribe(
      (response: Comment)=>  {
        Notifications.addNewAlert("Your comment is added","success");
        this.ticketComments.push(response);
        
      },
      (error: HttpErrorResponse) =>{
        alert(error.message);
      }    
    )
  }

  userToBeAdded!: User;
  public async onAssignUser(assignUserForm: NgForm):Promise<void>{
    const username = assignUserForm.form.get('assign_username')?.value;
    
    this.userToBeAdded =  await this.userService.getUserByName(username).toPromise();

    if(this.userToBeAdded?.id){
      console.warn(`User to be assigned to ticket ID: ${this.userToBeAdded.id}`);

      this.ticketService.assignUserToTicket(this.userToBeAdded.id,this.ticket.id).subscribe(
        (response: any)=>  {
          //alert(`User: ${this.userToBeAdded.userName} Assigned to Ticket`);
          Notifications.addNewAlert(`User: ${this.userToBeAdded.userName} Assigned to Ticket`,"success");
          
          //refresh
          const currentUrl = this.router.url;
          this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
          this.router.navigate([currentUrl],{ state: { ticket: response } });
          });
         },
         (error: HttpErrorResponse) =>{
           alert(error.message);
         }    
      );
    }else{alert("User not found")}
  }

  public onTagSelect(tag:Tag){
    this.selectedTags.push(tag);
  }

  public onTagClick(tag:Tag){
    this.router.navigateByUrl('tickets', { state: { tag: tag } });
  }

  disableButtonForever(button: any) {
    this.foreverDisabledButtons.push(button);
  }
  enableDisabledButtons() {
    this.foreverDisabledButtons = [];
    this.selectedTags=[];
  }

  public async onAddTags():Promise<void>{
    
    this.selectedTags.forEach((tag) => {
      this.ticket.tags.push(tag);
      this.ticketService.addTagToTicket(tag.id,this.ticket.id).toPromise();
    })

    const currentUrl = this.router.url;
          this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
          this.router.navigate([currentUrl],{ state: { ticket: this.ticket } });
          });

  }


}
