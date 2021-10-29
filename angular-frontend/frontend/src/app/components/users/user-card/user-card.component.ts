import { Component, Input, OnInit } from '@angular/core';
import { UserStats } from 'src/app/interfaces/statInterfaces/userStats';
import { User } from 'src/app/interfaces/user';
import { UserServiceService } from 'src/app/services/user/user-service.service';

@Component({
  selector: 'app-user-card',
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.css']
})
export class UserCardComponent implements OnInit {

  @Input()
  user!: User;

  userStats!: UserStats;

  constructor(public userService:UserServiceService) { }

  async ngOnInit(): Promise<void> {
    this.userStats = await this.userService.getUserStats(this.user.id).toPromise();
  }

}
