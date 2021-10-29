import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/interfaces/user';
import { UserServiceService } from 'src/app/services/user/user-service.service';
import * as Notifications from 'src/app/interfaces/globalNotifications/notifications'; 


@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {


  user!: User;

  constructor(public userService:UserServiceService, private router:Router) { 
  }

  async ngOnInit(): Promise<void> {
    let username = localStorage.getItem("username")? localStorage.getItem("username") : null;
    if(username){
      this.user = await this.userService.getUserByName(username).toPromise();
    }else{ this.router.navigate(["register"]);}
    
  }

  public async onUpdateUser(updateUserForm: NgForm):Promise<void>{
    const username = updateUserForm.form.get('user_username')?.value;
    const userEmail = updateUserForm.form.get('user_email')?.value;
    const userPhone = updateUserForm.form.get('user_phone')?.value;

    this.user = {
      userName:username,
      userEmail:userEmail,
      userPhoneNo: userPhone,
      id:this.user.id,
      password:this.user.password
    }
    this.userService.updateUser(this.user).subscribe(
      (response: User)=>  {
        Notifications.addNewAlert("Your Profile has been updated","success");
        localStorage.setItem("username",response.userName);
      },
      (error: HttpErrorResponse) =>{
        alert("Username already exists");
      }    
    )



  }


}
