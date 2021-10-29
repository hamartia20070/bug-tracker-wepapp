import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { jwtTokens } from 'src/app/interfaces/jwtTokens';
import { Project } from 'src/app/interfaces/project';
import { ProjectUserRoleLink } from 'src/app/interfaces/project-user-role-link';
import { AuthService } from 'src/app/services/auth/auth.service';
import { ProjectService } from 'src/app/services/project/project.service';
import * as Notifications from 'src/app/interfaces/globalNotifications/notifications'; 

@Component({
  selector: 'app-project-card',
  templateUrl: './project-card.component.html',
  styleUrls: ['./project-card.component.css']
})
export class ProjectCardComponent implements OnInit {


  constructor(public projectService: ProjectService, public router: Router, private authService:AuthService) { }

  @Input()
  project!: Project;

  projectUserLink!: ProjectUserRoleLink;

  viewDisabled=false;


  stats: number[] = [];

  async ngOnInit(): Promise<void> {
    this.stats= await this.projectService.getProjectStats(this.project.id).toPromise();
    const userId = localStorage.getItem("user_id");
    if(userId)
    this.projectService.getUserProjectLink(this.project.id,Number(userId)).subscribe(
      (response: ProjectUserRoleLink)=>  {
        if(response){
          this.projectUserLink= response;
        }else{this.viewDisabled=true;}
       
      },
      (error: HttpErrorResponse) =>{
        alert(error)
        this.viewDisabled=true;
      }    
    )
  }
    //adds user with fixed owner role, just for demo's sake to easily access stuff
    public addUserToProject(){
    const userId= localStorage.getItem("user_id");

      this.projectService.addUserToProject(Number(userId),this.project.id,1).subscribe(
        (response: ProjectUserRoleLink)=>  {
          this.projectUserLink = response;
          this.viewDisabled=false;
          console.warn("User added to project with ID: "+this.project.id+"\n As role: "+response.role.roleType+"\n With access level: "+response.role.accessLevel);
          Notifications.addNewAlert(`You have been added to project: ${this.project.projectName} \n With role: ${response.role.roleType} And level ${response.role.accessLevel}`,"success")

           //the token has user access rights in it so we need to get a new one to save changes
           this.authService.refreshToken('').subscribe(
            (response: jwtTokens)=>{
              console.warn("In the {getting new access token using refresh token} process @assigning user to project");
              if(response.access_token && response.refresh_token && response.access_token_expires&&response.refresh_token_expires){
                localStorage.setItem('access_token',response.access_token);
                localStorage.setItem('refresh_token',response.refresh_token);
                localStorage.setItem('access_token_expires',response.access_token_expires);
                localStorage.setItem('refresh_token_expires',response.refresh_token_expires);
              }
              console.warn("saved new token @assigning user to project");
            }  
          )
        },
        (error: HttpErrorResponse) =>{
          alert(error.message);
        }    
      )
    }

  public openProject(){
    this.router.navigateByUrl('projects/project', { state: { project: this.project } });
  }

}
