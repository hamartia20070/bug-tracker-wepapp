import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { Priority } from 'src/app/interfaces/priority';
import { Project } from 'src/app/interfaces/project';
import { Role } from 'src/app/interfaces/role';
import { PriorityService } from 'src/app/services/priority/priority.service';
import { ProjectService } from 'src/app/services/project/project.service';
import * as Notifications from 'src/app/interfaces/globalNotifications/notifications';

@Component({
  selector: 'app-edit-project',
  templateUrl: './edit-project.component.html',
  styleUrls: ['./edit-project.component.css']
})
export class EditProjectComponent implements OnInit {


  project !: Project;
  priorities!:Priority[];
  selectedPriority!: Priority;
  constructor(public router:Router, public priorityService:PriorityService, public projectService:ProjectService) {
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
    this.selectedPriority = this.project.projectPriority;
    
  }

  public setPriority(priority:Priority):void{
    this.selectedPriority=priority;
    

  }
  public async onUpdateProject(updateProjectForm: NgForm):Promise<void>{
    const projectTitle = updateProjectForm.form.get('project_title')?.value;
    const projectDescription = updateProjectForm.form.get('project_description')?.value;
    const projectOpen =  updateProjectForm.form.get('project_status')?.value;
    this.project.projectName=projectTitle;
    this.project.projectDescription=projectDescription;
    this.project.projectOpen=projectOpen;
    

    this.projectService.updateProject(this.project.id,this.selectedPriority.id,this.project).subscribe(
      (response: Project)=>  {
        //alert("Project Updated")
        Notifications.addNewAlert(`Project Updated: ${response.projectName} `,"success")
        this.router.navigateByUrl('projects/project', { state: { project: response } });
        
      },
      (error: HttpErrorResponse) =>{
        alert(error.message);
      }    
    )
    


  }

}
