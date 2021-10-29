import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/interfaces/user';
import { UserServiceService } from 'src/app/services/user/user-service.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {


  users!: User[];
  pageNo=0;

  constructor(public userService:UserServiceService) { }

  async ngOnInit(): Promise<void> {
    this.users = await this.userService.getUsers(this.pageNo).toPromise();
  }

  public async nextUsers(){       
    if(this.users.length!=0){
      console.warn("click called");
      this.pageNo+=1;
      this.users = await this.userService.getUsers(this.pageNo).toPromise();
    }
  }
  public async previousUsers(){
    if(this.pageNo!==0){
      this.pageNo-=1;
      console.warn("click called");
      this.users = await this.userService.getUsers(this.pageNo).toPromise();
    }
  }

}
