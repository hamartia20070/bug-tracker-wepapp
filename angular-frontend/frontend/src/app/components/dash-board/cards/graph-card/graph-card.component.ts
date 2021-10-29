import { Component, Input, OnInit } from '@angular/core';
import { NgxChartsModule, LegendPosition } from '@swimlane/ngx-charts';
import { BarchartItem } from 'src/app/interfaces/statInterfaces/barchart-items';
import { TicketService } from 'src/app/services/ticket/ticket.service';
import {BrowserModule} from '@angular/platform-browser';

@Component({
  selector: 'app-graph-card',
  templateUrl: './graph-card.component.html',
  styleUrls: ['./graph-card.component.scss']
})
export class GraphCardComponent implements OnInit {

  @Input()
  items!: BarchartItem[];

  view: [number,number] = [400, 200];

  @Input()
  cardTitle!:string;

  // options
  showXAxis = false;
  legendPosition = LegendPosition.Below;
  showYAxis = true;
  gradient = false;
  showLegend = false;
  showXAxisLabel = false;
  showYAxisLabel = false;

  constructor(public ticketService:TicketService) { 
  }

  async ngOnInit(): Promise<void> {

  }

  onSelect(event: any) {
    console.log(event);
  }

}
