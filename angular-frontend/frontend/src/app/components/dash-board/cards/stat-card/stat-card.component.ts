import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-stat-card',
  templateUrl: './stat-card.component.html',
  styleUrls: ['./stat-card.component.css']
})
export class StatCardComponent implements OnInit {

  @Input()
  cardIcon!: string;
  @Input()
  cardCategory!: string;
  @Input()
  cardTitle!: string;


  constructor() { }

  ngOnInit(): void {
  }

}
