import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AppComponent } from 'src/app/app.component';
import * as Notifications from 'src/app/interfaces/globalNotifications/notifications'; 

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private router: Router) { }

  alerts = Notifications.alerts;

  username!: any;
  ngOnInit(): void {
    this.username = localStorage.getItem("username")?.toString() ;
    
  }

  onLogoutClick(): void {
    localStorage.removeItem("username");
    localStorage.removeItem("user_id");
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    localStorage.removeItem("access_token_expires");
    localStorage.removeItem("refresh_token_expires");
    this.router.navigate(["register"]);
  }
  
  close(alert: Notifications.Alert) {
    this.alerts.splice(this.alerts.indexOf(alert), 1);
  }

}
