import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Role } from 'src/app/interfaces/role';
import { RoleService } from 'src/app/services/role/role.service';
import * as Notifications from 'src/app/interfaces/globalNotifications/notifications'; 

@Component({
  selector: 'app-roles',
  templateUrl: './roles.component.html',
  styleUrls: ['./roles.component.css']
})
export class RolesComponent implements OnInit {

  roles!: Role[];

  constructor(public roleService:RoleService) { }

  async ngOnInit(): Promise<void> {
    this.roles = await this.roleService.getRoles().toPromise();
  }

  public async onRoleClick(accessLevel:number,addRoleForm: NgForm):Promise<void>{
    const roleName = addRoleForm.form.get('role_name')?.value;
    //alert(`access level: ${accessLevel} and roleName: ${roleName}`);

    let newRole: Role = {
      id:0,
      roleType : roleName,
      accessLevel : accessLevel,
      projectUserRoleLinks: []
    }


    this.roleService.addRole(newRole).subscribe(
      (response: Role)=>  {
        //alert("Role Added")
        Notifications.addNewAlert(`New Role Created With Name: ${response.roleType} `,"success")
        this.roles.push(response);
      },
      (error: HttpErrorResponse) =>{
        alert(error.message);
      }    
    )

    
  }

}
