import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Priority } from 'src/app/interfaces/priority';
import { PriorityService } from 'src/app/services/priority/priority.service';
import * as Notifications from 'src/app/interfaces/globalNotifications/notifications'; 

@Component({
  selector: 'app-priorities',
  templateUrl: './priorities.component.html',
  styleUrls: ['./priorities.component.css']
})
export class PrioritiesComponent implements OnInit {

  priorities!: Priority[];
  value: number = 1;
  constructor(public prioritiesService: PriorityService) { }

  async ngOnInit(): Promise<void> {
    this.priorities = await this.prioritiesService.getPriorities().toPromise();
  }
  public async onAddPriority(addPriorityForm: NgForm):Promise<void>{
    const priorityname = addPriorityForm.form.get('priority_name')?.value;

    let newPriority: Priority = {
      id:0,
      importance: this.value,
      priorityType: priorityname,
      projects: [],
      tickets:[]
    }

    this.prioritiesService.addPriority(newPriority).subscribe(
      (response: Priority)=>  {
        //alert("Priority Added")
        Notifications.addNewAlert(`New Priority Created With Name: ${response.priorityType} `,"success")
        this.priorities.push(response);
      },
      (error: HttpErrorResponse) =>{
        alert(error.message);
      }    
    )

  }

}
