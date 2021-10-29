import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from '@angular/common/http'
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { jwtTokens } from '../../interfaces/jwtTokens';
import { UserServiceService } from '../user/user-service.service';
import { User } from 'src/app/interfaces/user';
import * as Notifications from 'src/app/interfaces/globalNotifications/notifications'; 
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private token!: jwtTokens; 
  private user!: User;
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient, private userService:UserServiceService, private router: Router) { }


  public setSession(username:string,password:string){
    this.loginUser(username,password).subscribe(
        (response: jwtTokens)=>{
          this.token=response;
          localStorage.setItem('access_token',this.token.access_token);
          localStorage.setItem('refresh_token',this.token.refresh_token);
          localStorage.setItem('access_token_expires',this.token.access_token_expires);
          localStorage.setItem('refresh_token_expires',this.token.refresh_token_expires);
          localStorage.setItem('username',this.token.username);
          //alert(this.token.access_token+"\n username: "+this.token.username);
          this.userService.getUserByName(this.token.username).subscribe(
            (response: User)=>  {
              if(response.id)
              localStorage.setItem("user_id",response.id.toString());
              console.warn("user ID: "+response.id);
              Notifications.addNewAlert(`You are now logged in as ${response.userName}`,"success");
              this.router.navigateByUrl("dashboard");
            },
            (error: HttpErrorResponse) =>{
              alert(error.message);
            }    

          )

        },
        (error: HttpErrorResponse) =>{
          console.warn(error.message);
          alert("User Not Found")
        }        
    )  

  }



  public refreshToken(refreshToken: string):Observable<jwtTokens>{
    return this.http.get<jwtTokens>(`${this.apiServerUrl}/api/token/refresh`);
  }


  public loginUser(username: string,password:string):Observable<jwtTokens>{
    //alert(username+"   "+password)
    let params = new HttpParams().set("username",username).set("password", password);
    return this.http.get<jwtTokens>(`${this.apiServerUrl}/api/login`, {params: params});
  }

  public registerUser(newUser: User):Observable<User>{
    let header = new HttpHeaders().set('content-type','application/json')
    let body = JSON.stringify(newUser);
    return this.http.post<User>(`${this.apiServerUrl}/api/register`,body,{headers:header});
  }


}
