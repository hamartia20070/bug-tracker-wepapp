import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { SideBarComponent } from './components/side-bar/side-bar.component';
import { DashBoardComponent } from './components/dash-board/dash-board.component';
import { MiniTicketsMenuCardComponent } from './components/dash-board/mini-tickets-menu-card/mini-tickets-menu-card.component';
import { RowComponent } from './components/dash-board/mini-tickets-menu-card/row/row.component';
import { StatCardComponent } from './components/dash-board/cards/stat-card/stat-card.component';
import { GraphCardComponent } from './components/dash-board/cards/graph-card/graph-card.component';
import { ProjectsComponent } from './components/projects/projects.component';
import { ProjectCardComponent } from './components/projects/project-card/project-card.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { ProjectComponent } from './components/project/project.component';
import { TicketsCardComponent } from './components/project/tickets-card/tickets-card.component';
import { EditProjectComponent } from './components/edit-project/edit-project.component';
import { TicketsComponent } from './components/tickets/tickets.component';
import { TicketComponent } from './components/ticket/ticket.component';
import { EditTicketComponent } from './components/edit-ticket/edit-ticket.component';
import { UsersComponent } from './components/users/users.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { TicketCardComponent } from './components/tickets/ticket-card/ticket-card.component';
import { UserCardComponent } from './components/users/user-card/user-card.component';
import { TagsComponent } from './components/tags/tags.component';
import { TagCardComponent } from './components/tags/tag-card/tag-card.component';
import { RolesComponent } from './components/roles/roles.component';
import { RoleCardComponent } from './components/roles/role-card/role-card.component';
import { RegisterComponent } from './components/register/register.component';
import { UserServiceService } from './services/user/user-service.service';
import { HttpClientModule,HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AuthInterceptorService } from './services/auth/auth-interceptor.service';
import { DatePipe } from '@angular/common';
import { ProjectUserListRowComponent } from './components/project/project-user-list-row/project-user-list-row.component';
import { TicketsCardRowComponent } from './components/project/tickets-card/tickets-card-row/tickets-card-row.component';
import { AddProjectComponent } from './components/add-project/add-project.component';
import { AddTicketComponent } from './components/add-ticket/add-ticket.component';
import { PrioritiesComponent } from './components/priorities/priorities.component';
import { PriorityCardComponent } from './components/priorities/priority-card/priority-card.component';
import {NumberPickerModule} from 'ng-number-picker';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AngularNotificationModule} from 'angular-notification-alert';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    SideBarComponent,
    DashBoardComponent,
    MiniTicketsMenuCardComponent,
    RowComponent,
    StatCardComponent,
    GraphCardComponent,
    ProjectsComponent,
    ProjectCardComponent,
    UserProfileComponent,
    ProjectComponent,
    TicketsCardComponent,
    EditProjectComponent,
    TicketsComponent,
    TicketComponent,
    EditTicketComponent,
    UsersComponent,
    TicketCardComponent,
    UserCardComponent,
    TagsComponent,
    TagCardComponent,
    RolesComponent,
    RoleCardComponent,
    RegisterComponent,
    ProjectUserListRowComponent,
    TicketsCardRowComponent,
    AddProjectComponent,
    AddTicketComponent,
    PrioritiesComponent,
    PriorityCardComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    HttpClientModule, FormsModule, NumberPickerModule, NgxChartsModule, BrowserAnimationsModule,AngularNotificationModule
  ],
  providers: [ 
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true },
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
