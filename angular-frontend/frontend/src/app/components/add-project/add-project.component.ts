import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { jwtTokens } from 'src/app/interfaces/jwtTokens';
import { Priority } from 'src/app/interfaces/priority';
import { Project } from 'src/app/interfaces/project';
import { Role } from 'src/app/interfaces/role';
import { AuthService } from 'src/app/services/auth/auth.service';
import { PriorityService } from 'src/app/services/priority/priority.service';
import { ProjectService } from 'src/app/services/project/project.service';
import { RoleService } from 'src/app/services/role/role.service';
import * as Notifications from 'src/app/interfaces/globalNotifications/notifications';

@Component({
  selector: 'app-add-project',
  templateUrl: './add-project.component.html',
  styleUrls: ['./add-project.component.css']
})
export class AddProjectComponent implements OnInit {

  priorities!:Priority[];
  selectedPriority!: Priority;
  roles!:Role[];
  constructor(public priorityService:PriorityService,public projectService:ProjectService, public roleService:RoleService,public router:Router,
    private authService:AuthService) { }

  async ngOnInit(): Promise<void> {
    this.priorities = await this.priorityService.getPriorities().toPromise();
    this.roles= await this.roleService.getRolesByAcessLevel(3,0).toPromise();
  }
  public setPriority(priority:Priority):void{
    this.selectedPriority=priority;
  }

  tickBoxDefault = true;
  public async onAddProject(updateProjectForm: NgForm):Promise<void>{
    const projectTitle = updateProjectForm.form.get('project_title')?.value;
    const projectDescription = updateProjectForm.form.get('project_description')?.value;
    const projectOpen =  updateProjectForm.form.get('project_status')?.value;
    let userId= localStorage.getItem("user_id") ? Number(localStorage.getItem("user_id")) : 0;

    let newProject: Project = {
      projectName: projectTitle, 
      projectDescription: projectDescription, 
      projectOpen: projectOpen, 
      dateStarted: new Date(),
      id: 0,
      projectPriority: this.selectedPriority,
      tickets: {} as any,
      projestUserRoleLinks: {} as any
    }
     
    alert(`Project Name: ${ newProject.projectName}\n Project desc: ${newProject.projectDescription}\n project open: ${newProject.projectOpen}
    Date started: ${newProject.dateStarted}`);

    //if(userId!==0)
    this.projectService.addProject(newProject.projectPriority.id,
      newProject,userId,this.roles[0].id).subscribe(
        (response: Project)=>  {
          alert("Project Created")
          Notifications.addNewAlert(`Project Created: ${response.projectName} `,"success");
          //the token has user access rights in it so we need to get a new one to save changes
          this.authService.refreshToken('').subscribe(
            (response: jwtTokens)=>{
              console.warn("In the {getting new access token using refresh token} process @creating project");
              if(response.access_token && response.refresh_token && response.access_token_expires&&response.refresh_token_expires){
                localStorage.setItem('access_token',response.access_token);
                localStorage.setItem('refresh_token',response.refresh_token);
                localStorage.setItem('access_token_expires',response.access_token_expires);
                localStorage.setItem('refresh_token_expires',response.refresh_token_expires);
              }
              console.warn("saved new token @createProject");
            }  
          )


          this.router.navigateByUrl('projects');
          
        },
        (error: HttpErrorResponse) =>{
          alert(error.message);
        }    
      )
    
 


  }

}
