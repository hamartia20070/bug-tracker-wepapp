import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { Injectable, Injector } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import {delay, first, map, retry, switchMap} from 'rxjs/operators';
import { DatePipe } from '@angular/common'
import { AuthService } from './auth.service';
import { jwtTokens } from 'src/app/interfaces/jwtTokens';
import { Variable } from '@angular/compiler/src/render3/r3_ast';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export  class AuthInterceptorService implements HttpInterceptor {

  constructor(public datepipe: DatePipe, public authService: AuthService,private injector: Injector, private router: Router) {}
  token!: jwtTokens;






    intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const accessToken = localStorage.getItem('access_token');
    const refreshToken = localStorage.getItem('refresh_token');
    const accessTokenExpiry =localStorage.getItem("access_token_expires");
    const refreshTokenExpiry =localStorage.getItem("refresh_token_expires");

    let accessTokenExpiryDate = this.datepipe.transform(accessTokenExpiry,'MM/dd/yyyy HH:mm:ss');
    let refreshTokenExpiryDate = this.datepipe.transform(refreshTokenExpiry,'MM/dd/yyyy HH:mm:ss');
    let dateNow = this.datepipe.transform(Date.now(),'MM/dd/yyyy HH:mm:ss');

    console.warn("Following access token found: \n \n"+accessToken);
    console.warn("@auth-interceptor Access_token expires at: "+accessTokenExpiryDate);
    console.warn("@auth-interceptor Refresh_token expires at: "+refreshTokenExpiryDate);
    console.warn("@auth-interceptor Day right now: "+dateNow);
    console.warn("REQUEST BODY IS: \n"+req.body);

    if (accessToken!==null) {
      console.warn("Incoming url in @auth-interceptor: "+req.urlWithParams);
      if( accessTokenExpiryDate!==null && dateNow!==null &&  dateNow<accessTokenExpiryDate && req.url!==`${environment.apiBaseUrl}/api/token/refresh`){
        const cloned = req.clone({
          headers: req.headers.set('Authorization', 'Bearer ' + accessToken),
        });
  
        return next.handle(cloned)/*.pipe(map((event: HttpEvent<any>) => {
          if (event instanceof HttpResponse) {
              let body = JSON.parse(JSON.stringify(event)).body;
              if(body.error_message){
                console.warn("access token expired")
  
  
  
              }
              //event = event.clone({body: this.modifyBody(event.body)});
          }
          return event;
        }));*/ //<<<<<<<<<<<<<<<<<<<<<<<<< Code in case I also want to handle the response

          //if its refresh token path
      }else if( req.url===`${environment.apiBaseUrl}/api/token/refresh`){
        console.warn("refresh token path detected @auth-interceptor");
        const cloned = req.clone({
          headers: req.headers.set('Authorization', 'Bearer ' + refreshToken),
        });
  
        return next.handle(cloned)

      }
      else{//if token expired, get new token and then process request else if both tokens expired, reroute to login         
        if(refreshToken!==null && refreshTokenExpiryDate!==null&& dateNow!==null && dateNow < refreshTokenExpiryDate ){
          return this.authService.refreshToken(refreshToken).pipe(
            switchMap((response: jwtTokens)=>{
              console.warn("In the {getting new access token using refresh token} process @auth-interceptor");
              if(response.access_token && response.refresh_token && response.access_token_expires&&response.refresh_token_expires){
                localStorage.setItem('access_token',response.access_token);
                localStorage.setItem('refresh_token',response.refresh_token);
                localStorage.setItem('access_token_expires',response.access_token_expires);
                localStorage.setItem('refresh_token_expires',response.refresh_token_expires);
              }
              console.warn("saved new token @auth-interceptor");
             return next.handle(this.updateHeader(req));
            } ) 
          )


        }else{//redirect to login
          console.warn("redirect to login");
          this.router.navigate(["register"])
          throw console.error("Refresh token expired");

        }
         
      }
      //if user is trying to login/register, let it through
    } else if(req.url===`${environment.apiBaseUrl}/api/register` ||req.url===`${environment.apiBaseUrl}/api/login` ){
      return next.handle(req);
    }
    
    else {
      console.warn("redirect to login");
      this.router.navigate(["register"])
      throw console.error("Tokens Not found");
    }


    
  }
 
  updateHeader(req: HttpRequest<any>) {
    const authToken = localStorage.getItem('access_token');
    req = req.clone({
     headers: req.headers.set("Authorization", `Bearer ${authToken}`)
    });
    return req;
   }





























































  /*console.warn(req.url);
      if( accessTokenExpiryDate!==null && dateNow!==null &&  dateNow<accessTokenExpiryDate && req.url!=='http://localhost:8080/api/token/refresh'){
        const cloned = req.clone({
          headers: req.headers.set('Authorization', 'Bearer ' + accessToken),
        });
  
        return next.handle(cloned)/*.pipe(map((event: HttpEvent<any>) => {
          if (event instanceof HttpResponse) {
              let body = JSON.parse(JSON.stringify(event)).body;
              if(body.error_message){
                console.warn("access token expired")
  
  
  
              }
              //event = event.clone({body: this.modifyBody(event.body)});
          }
          return event;
        }));*/ //<<<<<<<<<<<<<<<<<<<<<<<<< Code in case I also want to see the response


     /* }else if( req.url==='http://localhost:8080/api/token/refresh'){
        console.warn("refresh token path detected");
        const cloned = req.clone({
          headers: req.headers.set('Authorization', 'Bearer ' + refreshToken),
        });
  
        return next.handle(cloned)

      }
      else{//if token expired, get new token and then process request
        
        if(refreshToken!==null && refreshTokenExpiryDate!==null&& dateNow!==null && dateNow < refreshTokenExpiryDate ){
          this.authService.refreshToken(refreshToken).subscribe(
            (response: jwtTokens)=>{
              this.token=response;
              localStorage.setItem('access_token',this.token.access_token);
              localStorage.setItem('refresh_token',this.token.refresh_token);
              localStorage.setItem('access_token_expires',this.token.access_token_expires);
              localStorage.setItem('refresh_token_expires',this.token.refresh_token_expires);
              console.warn("This is access token for new request: "+this.token.access_token);
             
            },
            (error: HttpErrorResponse) =>{
              console.warn(error.message);
            }     
          )
          console.warn("poggg");

          console.warn("request should be processed");

           const cloned = req.clone({
            headers: req.headers.set('Authorization', 'Bearer ' +this.token.access_token),
          });
          console.warn("request should be process");

          
          return next.handle( req.clone({
            headers: req.headers.set('Authorization', 'Bearer ' +this.token.access_token),
          })).pipe(retry(100));

        }else{//redirect to login
          console.warn("redirect to login");
          return next.handle(req);///poop
        }
         
      }*/
      
}
