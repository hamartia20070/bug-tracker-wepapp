import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { NavigationStart, Router, RouterEvent } from '@angular/router';
import {Observable, OperatorFunction} from 'rxjs';
import {debounceTime, delay, distinctUntilChanged, filter, map} from 'rxjs/operators';
import { Project } from 'src/app/interfaces/project';
import { ProjectUserRoleLink } from 'src/app/interfaces/project-user-role-link';
import { Role } from 'src/app/interfaces/role';
import { User } from 'src/app/interfaces/user';
import { ProjectService } from 'src/app/services/project/project.service';
import { RoleService } from 'src/app/services/role/role.service';
import { UserServiceService } from 'src/app/services/user/user-service.service';
import * as Notifications from 'src/app/interfaces/globalNotifications/notifications'; 


@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})
export class ProjectComponent implements OnInit {
  public model: any;

  project!: Project;
  noOfUsers!: number;
  stats!: number[];
  noOfTickets!: number;
  usersPageNo = 0;
  users!: User[];
  roles!: Role[];
  thisUserLink!:ProjectUserRoleLink;
  addUserButtonDisabled=false;
  editProjectAndCreateTicketButton=false;



  constructor(public router:Router, public datepipe:DatePipe, public projectService:ProjectService, public userService: UserServiceService, 
    public roleService: RoleService) { 
    //I passed the entire already loaded project object from the projects menu when rerouting as a navigation state variable    
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
    this.noOfUsers = await this.projectService.getProjectNoOfUsers(this.project.id).toPromise();
    this.stats = await this.projectService.getProjectStats(this.project.id).toPromise();
    this.users = await this.userService.getUsersOfProject(this.project.id,this.usersPageNo).toPromise();
    this.noOfTickets = this.stats[0]+this.stats[1]
    const userId= localStorage.getItem("user_id");
    this.thisUserLink = await this.projectService.getUserProjectLink(this.project.id,Number(userId)).toPromise();
    this.roles = await this.roleService.getRoles().toPromise();

    if(this.thisUserLink.role.accessLevel<3){
      this.addUserButtonDisabled=true;
    }
    if(this.thisUserLink.role.accessLevel<2){
      this.editProjectAndCreateTicketButton=true;
    }
  }


  public async nextUsers(){       
    if(this.users.length!=0){
      console.warn("click called");
      this.usersPageNo+=1;
      this.users = await this.userService.getUsersOfProject(this.project.id,this.usersPageNo).toPromise();
    }
  }
  public async previousUsers(){
    if(this.usersPageNo!==0){
      this.usersPageNo-=1;
      console.warn("click called");
      this.users = await this.userService.getUsersOfProject(this.project.id,this.usersPageNo).toPromise();
    }
  }

  selectedRoleId!: number;
  userToBeAdded!: User;

  public async onAddUser(addUserForm: NgForm):Promise<void>{
    const username = addUserForm.form.get('username')?.value;
    this.userToBeAdded =  await this.userService.getUserByName(username).toPromise();
   
    if(this.userToBeAdded?.id){
      console.warn("User to be added ID: "+this.userToBeAdded.id)
      this.projectService.addUserToProject(this.userToBeAdded.id,this.project.id,this.selectedRoleId).subscribe(
        (response: ProjectUserRoleLink)=>  {
         //alert("User: "+this.userToBeAdded.userName+" Added to project");
         Notifications.addNewAlert(`New Role Created With Name: ${response.user.userName} `,"success")

         const currentUrl = this.router.url;
          this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
          this.router.navigate([currentUrl],{ state: { project: response.project } });
          });
        },
        (error: HttpErrorResponse) =>{
          alert(error.message);
        }    
      );
    }else{alert("User not found")}

  }
  public setRole(role:number):void{
    this.selectedRoleId=role;
    

  }

  public openEditProject(){
    this.router.navigateByUrl('projects/project/edit', { state: { project: this.project } });
  }
  public openAddTicket(){
    this.router.navigateByUrl('tickets/add', { state: { project: this.project } });
  }



}
