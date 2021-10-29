import { Component, Input, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Role } from 'src/app/interfaces/role';
import { RoleService } from 'src/app/services/role/role.service';
import * as Notifications from 'src/app/interfaces/globalNotifications/notifications'; 

@Component({
  selector: 'app-role-card',
  templateUrl: './role-card.component.html',
  styleUrls: ['./role-card.component.css']
})
export class RoleCardComponent implements OnInit {

  @Input()
  role!: Role;

  constructor(public roleService:RoleService) { }

  ngOnInit(): void {
  }

  public async onRoleClick(accessLevel:number,updateRoleForm: NgForm):Promise<void>{
    const roleName = updateRoleForm.form.get('role_name')?.value;
    //alert(`access level: ${accessLevel} and roleName: ${roleName}`);

    this.role.accessLevel=accessLevel; this.role.roleType=roleName;
    this.roleService.updateRole(this.role).toPromise();
    Notifications.addNewAlert(`Role Updated With Name: ${this.role.roleType} `,"success")
    
  }

}
