import { Component, Input, OnInit } from '@angular/core';
import { Project } from 'src/app/interfaces/project';
import { ProjectUserRoleLink } from 'src/app/interfaces/project-user-role-link';
import { UserStatsForProject } from 'src/app/interfaces/statInterfaces/userStatsForProject';
import { User } from 'src/app/interfaces/user';
import { ProjectService } from 'src/app/services/project/project.service';

@Component({
  selector: '[app-project-user-list-row]',
  templateUrl: './project-user-list-row.component.html',
  styleUrls: ['./project-user-list-row.component.css']
})
export class ProjectUserListRowComponent implements OnInit {

  constructor(public projectService: ProjectService) { }

  @Input()
  user!:User;
  @Input()
  project!: Project;

  userProjectLink!: ProjectUserRoleLink

  userStats!: UserStatsForProject;

  async ngOnInit(): Promise<void> {
    this.userStats = await this.projectService.getUserStatsForProject(this.project.id,this.user.id).toPromise();
    this.userProjectLink = await this.projectService.getUserProjectLink(this.project.id,this.user.id).toPromise();
    
  }

}
