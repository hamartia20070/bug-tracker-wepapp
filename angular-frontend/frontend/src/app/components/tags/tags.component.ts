import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Tag } from 'src/app/interfaces/tag';
import { TagService } from 'src/app/services/tag/tag.service';
import * as Notifications from 'src/app/interfaces/globalNotifications/notifications'; 

@Component({
  selector: 'app-tags',
  templateUrl: './tags.component.html',
  styleUrls: ['./tags.component.css']
})
export class TagsComponent implements OnInit {

  tags!: Tag[];


  constructor(public tagService:TagService) { }

  async ngOnInit(): Promise<void> {
    this.tags = await this.tagService.getTags().toPromise();
  }

  public async onAddTag(addTagForm: NgForm):Promise<void>{
    const tag = addTagForm.form.get('tag_name')?.value;
    let newTag: Tag = {
      id:0,
      tag : tag,
      tickets: []
    }
    this.tagService.addTag(newTag).subscribe(
      (response: Tag)=>  {
        //alert("Tag Added")
        Notifications.addNewAlert("New Tag Created with Name: "+response.tag,"success")
        this.tags.push(response);
      },
      (error: HttpErrorResponse) =>{
        alert(error.message);
      }    
    )
  }

}
