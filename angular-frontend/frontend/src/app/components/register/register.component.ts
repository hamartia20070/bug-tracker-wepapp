import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { User } from 'src/app/interfaces/user';
import { AuthInterceptorService } from 'src/app/services/auth/auth-interceptor.service';
import { AuthService } from 'src/app/services/auth/auth.service';
import { UserServiceService } from 'src/app/services/user/user-service.service';
import * as Notifications from 'src/app/interfaces/globalNotifications/notifications'; 

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})


export class RegisterComponent implements OnInit {


  //private user: User;


  constructor(private userService: UserServiceService, private authService: AuthService, private authFilter: AuthInterceptorService,
    private router:Router) { }

  


  ngOnInit(): void {
  }

  public onLogin(loginForm: NgForm,loginType:string):void{

    if(loginType=="normal"){
      this.authService.setSession(loginForm.form.get('username')?.value,loginForm.form.get('password')?.value)
    }else if (loginType=="demo"){

      this.authService.setSession("DemoUser","123")
    }


  
  }

  public onRegister(regForm: NgForm):void{
    const username = regForm.form.get('username')?.value;
    const email = regForm.form.get('email')?.value;
    const phone = regForm.form.get('phone_no')?.value;
    const password = regForm.form.get('password')?.value;


    let newUser: User = {
      id:0,
      userName:username,
      userEmail:email,
      userPhoneNo:phone,
      password:password,
    };

    this.authService.registerUser(newUser).subscribe(
      (response: User)=>  {
        alert("User Registered");
        this.authService.setSession(response.userName,password);
        
      },
      (error: HttpErrorResponse) =>{
        alert("Username Taken");
      }    
    )

  }
}

