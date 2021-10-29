import { JsonPipe } from '@angular/common';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Project } from 'src/app/interfaces/project';
import { ProjectUserRoleLink } from 'src/app/interfaces/project-user-role-link';
import { UserStatsForProject } from 'src/app/interfaces/statInterfaces/userStatsForProject';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  public getProjects(pageNo:number,sortBy:string, userId:number| '', status: string| '', priority:string| '' ): Observable<Project[]>{
    let params = new HttpParams().set("pageNo",pageNo).set("sortBy",sortBy).set("userId",userId).set("status",status).set("priority",priority);
    return this.http.get<Project[]>(`${this.apiServerUrl}/api/projects`,{params:params})
  }
  public getProjectStats(projectId:number): Observable<number[]>{
    const userId= localStorage.getItem("user_id");
    return this.http.get<number[]>(`${this.apiServerUrl}/api/projects/${projectId}/stats/${userId}`);
  }

  public addUserToProject(userId:number,projectId:number,roleId:number): Observable<ProjectUserRoleLink>{
    //const userId= localStorage.getItem("user_id");
    let params = new HttpParams().set("userId",userId).set("roleId",roleId);
    return this.http.post<ProjectUserRoleLink>(`${this.apiServerUrl}/api/projects/${projectId}/user`,{body: ''},{params:params});
  }

  public getProjectNoOfUsers(projectId:number): Observable<number>{
    return this.http.get<number>(`${this.apiServerUrl}/api/projects/${projectId}/users/count`);
  }

  public getUserStatsForProject(projectId:number,userId:number): Observable<UserStatsForProject>{
    return this.http.get<UserStatsForProject>(`${this.apiServerUrl}/api/projects/${projectId}/users/${userId}/stats`);
  }

  public getUserProjectLink(projectId:number,userId:number): Observable<ProjectUserRoleLink>{
    return this.http.get<ProjectUserRoleLink>(`${this.apiServerUrl}/api/projects/${projectId}/users/${userId}/link`);
  }

  public updateProject(projectId:number,priorityId:number, newProject:Project): Observable<Project>{
    let params = new HttpParams().set("priorityId",priorityId);
    let header = new HttpHeaders().set('content-type','application/json')
    let body = JSON.stringify(newProject);
    return this.http.patch<Project>(`${this.apiServerUrl}/api/projects/${projectId}`,body,{params:params, headers:header});
  }

  public addProject(priorityId:number, newProject:Project,userId:number,roleId:number): Observable<Project>{
    //const userId= localStorage.getItem("user_id");

    let params = new HttpParams().set("priorityId",priorityId).set("userId",userId).set("roleId",roleId);
    let header = new HttpHeaders().set('content-type','application/json')
    let body = JSON.stringify(newProject);
    return this.http.post<Project>(`${this.apiServerUrl}/api/projects/`,body,{params:params, headers:header});
  }

  public getProjectsCountByPriority(): Observable<any[]>{
    return this.http.get<any[]>(`${this.apiServerUrl}/api/projects/priority/count`);
  }
  public getActiveTicketsCountForAllProjects(): Observable<any[]>{
    return this.http.get<any[]>(`${this.apiServerUrl}/api/projects/tickets/count`);
  }
  public getAllProjectsCount(): Observable<number>{
    return this.http.get<number>(`${this.apiServerUrl}/api/projects/count`);
  }

}
