import { Component, Input, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Priority } from 'src/app/interfaces/priority';
import { PriorityService } from 'src/app/services/priority/priority.service';
import * as Notifications from 'src/app/interfaces/globalNotifications/notifications';

@Component({
  selector: 'app-priority-card',
  templateUrl: './priority-card.component.html',
  styleUrls: ['./priority-card.component.css']
})
export class PriorityCardComponent implements OnInit {

  @Input()
  priority!: Priority;

  value!: number;
  constructor(public priorityService:PriorityService) { }

  ngOnInit(): void {
    this.value = this.priority.importance;
  }

  public async onUpdatePriority(updatePriorityForm: NgForm):Promise<void>{
    const priorityname = updatePriorityForm.form.get('priority_name')?.value;

    this.priority.importance = this.value;
    this.priority.priorityType = priorityname;

    this.priority = await this.priorityService.updatePriority(this.priority).toPromise();
    //alert("updated");
    Notifications.addNewAlert(`Priority Updated With Name: ${this.priority.priorityType} `,"success")

  }

}
