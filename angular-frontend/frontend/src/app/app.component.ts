import { Component,OnInit, ViewChild, ViewContainerRef, ComponentFactoryResolver, ComponentRef } from '@angular/core';
import * as DarkReader from 'darkreader';
import { AngularNotificationService, NotifComponent  } from 'angular-notification-alert';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title: string = 'frontend';


  constructor(){
    DarkReader.enable({
   });
   
    
  }

  ngAfterViewInit() {
  }
}
