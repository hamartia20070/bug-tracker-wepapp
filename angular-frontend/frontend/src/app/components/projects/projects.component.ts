import { Component, OnInit } from '@angular/core';
import { Project } from 'src/app/interfaces/project';
import { ProjectService } from 'src/app/services/project/project.service';

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.css']
})
export class ProjectsComponent implements OnInit {

  projects!: Project[];
  pageNo=0;
  sortBy="created";
  getBy="all"
  filterBy="";
  userId!: number;

  priorityLevels: number [] = [1,2,3,4,5,6,7,8,9,10];

  constructor(public projectsService:ProjectService) { }

  async ngOnInit(): Promise<void> {
    this.loadProjects();
    this.userId = Number(localStorage.getItem("user_id"));
  }

  public async nextProjects(){       
    if(this.projects.length!=0){
      console.warn("click called");
      this.pageNo+=1;
      this.loadProjects();
    }
  }
  public async previousProjects(){
    if(this.pageNo!==0){
      this.pageNo-=1;
      console.warn("click called");
      this.loadProjects();
    }
  }

  public async loadProjects(){
    if(this.getBy==="all"){
      this.projects = await  this.projectsService.getProjects(this.pageNo,this.sortBy,"","",this.filterBy).toPromise();
    }else if(this.getBy==="assigned"){
      this.projects = await  this.projectsService.getProjects(this.pageNo,this.sortBy,this.userId,"",this.filterBy).toPromise();

    }else if(this.getBy ==="active"){
      this.projects = await  this.projectsService.getProjects(this.pageNo,this.sortBy,"","open",this.filterBy).toPromise();
    }else if(this.getBy ==="closed"){
      this.projects = await  this.projectsService.getProjects(this.pageNo,this.sortBy,"","closed",this.filterBy).toPromise();
    }
   
  }

  public changeSortBy(sortBy:string){
    this.sortBy = sortBy;
    this.loadProjects();
  }
  public changeGetBy(getBy:string){
    this.getBy = getBy;
    this.loadProjects();
  }

  public changeFilterBy(input:any){
    this.filterBy=input;
    this.loadProjects();
  }

}
