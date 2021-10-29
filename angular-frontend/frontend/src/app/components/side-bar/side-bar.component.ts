import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.css']
})
export class SideBarComponent implements OnInit {

  constructor(public router:Router) { }

  //sets css class of button to active if it's set as active button
  activeButton: any = null;

  ngOnInit(): void {
  }

  onClick(button: any) {
    this.activeButton= button;
  }

}
