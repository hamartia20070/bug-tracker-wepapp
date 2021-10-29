import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http'
import { Observable } from 'rxjs';
import { User } from '../../interfaces/user';
import { environment } from 'src/environments/environment';
import { jwtTokens } from '../../interfaces/jwtTokens';
import { UserStats } from 'src/app/interfaces/statInterfaces/userStats';

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }


  public getUsers(pageNo:number): Observable<User[]>{
    let params = new HttpParams().set("pageNo",pageNo);
    return this.http.get<User[]>(`${this.apiServerUrl}/api/users`,{params:params})
  }

  public getUserByName(username: string): Observable<User>{
    return this.http.get<User>(`${this.apiServerUrl}/api/user/${username}`);
  }

  public registerUser(user: User): Observable<User>{
    return this.http.post<User>(`${this.apiServerUrl}/api/register`, user);
  }

  public getUsersOfProject(projectId: number, pageNo:number): Observable<User[]>{
    let params = new HttpParams().set("pageNo",pageNo);
    return this.http.get<User[]>(`${this.apiServerUrl}/api/projects/${projectId}/users`,{params:params});
  }
  public getUserStats(userId: number): Observable<UserStats>{
    return this.http.get<UserStats>(`${this.apiServerUrl}/api/user/${userId}/stats`);
  }
  public updateUser(newUser:User): Observable<User>{
    let header = new HttpHeaders().set('content-type','application/json')
    let body = JSON.stringify(newUser);
    return this.http.patch<User>(`${this.apiServerUrl}/api/user/`,body,{headers:header});
  }





}
