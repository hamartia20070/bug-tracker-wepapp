import { Component, Input, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Tag } from 'src/app/interfaces/tag';
import { TagService } from 'src/app/services/tag/tag.service';
import * as Notifications from 'src/app/interfaces/globalNotifications/notifications'; 

@Component({
  selector: 'app-tag-card',
  templateUrl: './tag-card.component.html',
  styleUrls: ['./tag-card.component.css']
})
export class TagCardComponent implements OnInit {


  @Input()
  tag!: Tag;

  constructor(public tagService: TagService) { }

  ngOnInit(): void {
  }

  public async onUpdateTag(updateTagForm: NgForm):Promise<void>{
    const tag = updateTagForm.form.get('tag_name')?.value;
    
    this.tag.tag=tag
    this.tagService.updateTag(this.tag).toPromise();
    Notifications.addNewAlert("Tag Updated with Name: "+this.tag.tag,"success")
  }
  

}
