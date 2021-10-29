import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http'
import { Observable } from 'rxjs';
import { User } from '../../interfaces/user';
import { environment } from 'src/environments/environment';
import { Ticket } from 'src/app/interfaces/ticket';
import { TicketHistory } from 'src/app/interfaces/ticket-history';
import { Comment } from 'src/app/interfaces/comment';
import { BarchartItem } from 'src/app/interfaces/statInterfaces/barchart-items';

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  private apiServerUrl = environment.apiBaseUrl;
  constructor(private http: HttpClient) { }

  public getAssignedTicketsOfUser(pageNo:number): Observable<Ticket[]>{
    const userId= localStorage.getItem("user_id");
    let params = new HttpParams().set("pageNo",pageNo);
    return this.http.get<Ticket[]>(`${this.apiServerUrl}/api/user/${userId}/tickets/assigned`,{params:params});
  }

  public getCreatedTicketsOfUser(pageNo:number): Observable<Ticket[]>{
    const userId= localStorage.getItem("user_id");
    let params = new HttpParams().set("pageNo",pageNo);
    return this.http.get<Ticket[]>(`${this.apiServerUrl}/api/user/${userId}/tickets`,{params:params});
  }

  public getTicketsOfProject(projectId:number,pageNo:number): Observable<Ticket[]>{
    let params = new HttpParams().set("pageNo",pageNo);
    return this.http.get<Ticket[]>(`${this.apiServerUrl}/api/tickets/project/${projectId}`,{params:params});
  }

  public getTicketsOfProjectsWithUser(pageNo:number,sortBy:string,getBy:string,tagId:number): Observable<Ticket[]>{
    const userId= localStorage.getItem("user_id");
    let params = new HttpParams().set("pageNo",pageNo).set("sortBy",sortBy).set("getBy",getBy).set("tagId",tagId);
    return this.http.get<Ticket[]>(`${this.apiServerUrl}/api/tickets/user/${userId}`,{params:params});
  }

  public getCommentsCountForTicket(ticketId:number): Observable<number>{
    return this.http.get<number>(`${this.apiServerUrl}/api/tickets/${ticketId}/count`);
  }

  public getTicketHistory(ticketId:number,pageNo:number): Observable<TicketHistory[]>{
    let params = new HttpParams().set("pageNo",pageNo);
    return this.http.get<TicketHistory[]>(`${this.apiServerUrl}/api/tickets/${ticketId}/history`,{params:params});
  }
  public getTicketComments(ticketId:number,pageNo:number): Observable<Comment[]>{
    let params = new HttpParams().set("pageNo",pageNo);
    return this.http.get<Comment[]>(`${this.apiServerUrl}/api/tickets/${ticketId}/comments`,{params:params});
  }
  public addComment(ticketId:number,userId:number, newComment:Comment): Observable<Comment>{
    //const userId= localStorage.getItem("user_id");

    let params = new HttpParams().set("userId",userId);
    let header = new HttpHeaders().set('content-type','application/json')
    let body = JSON.stringify(newComment);
    return this.http.post<Comment>(`${this.apiServerUrl}/api/tickets/${ticketId}/comments`,body,{params:params, headers:header});
  }

  public updateTicket(ticketId:number,priorityId:number, newTicket:Ticket): Observable<Ticket>{
    //const userId= localStorage.getItem("user_id");

    let params = new HttpParams().set("priorityId",priorityId);
    let header = new HttpHeaders().set('content-type','application/json')
    let body = JSON.stringify(newTicket);
    return this.http.patch<Ticket>(`${this.apiServerUrl}/api/tickets/${ticketId}`,body,{params:params, headers:header});
  }
  public addTicket(priorityId:number,projectId:number,userId:number, newTicket:Ticket): Observable<Ticket>{
    //const userId= localStorage.getItem("user_id");

    let params = new HttpParams().set("priorityId",priorityId).set("userId",userId);
    let header = new HttpHeaders().set('content-type','application/json')
    let body = JSON.stringify(newTicket);
    return this.http.post<Ticket>(`${this.apiServerUrl}/api/tickets/project/${projectId}`,body,{params:params, headers:header});
  }
  public assignUserToTicket(userId:number,ticketId:number): Observable<Ticket>{
    return this.http.patch<Ticket>(`${this.apiServerUrl}/api/tickets/${ticketId}/assign/${userId}`,{body: ''});
  }

  public addTagToTicket(tagId:number,ticketId:number): Observable<Ticket>{
    return this.http.post<Ticket>(`${this.apiServerUrl}/api/tickets/${ticketId}/tag/${tagId}`,{body: ''});
  }

  public getTicketById(ticketId:number): Observable<Ticket>{
    return this.http.get<Ticket>(`${this.apiServerUrl}/api/tickets/${ticketId}`);
  }

  public getTicketsCountByPriority(): Observable<any[]>{
    return this.http.get<any[]>(`${this.apiServerUrl}/api/tickets/priority/count`);
  }

  //gets a 2 item int array. was to lazy to make a dto
  public getTicketsCountAllAndFnished(): Observable<number[]>{
    return this.http.get<number[]>(`${this.apiServerUrl}/api/tickets/stats/allAndFinished`);
  }
   //gets a 2 item int array. was to lazy to make a dto tbh
  public getUserAssignedAndCreatedTicketsCount(userId:number): Observable<number[]>{
    return this.http.get<number[]>(`${this.apiServerUrl}/api/tickets/${userId}/stats`);
  }





}
