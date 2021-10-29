import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { SideBarComponent } from './components/side-bar/side-bar.component';
import { DashBoardComponent } from './components/dash-board/dash-board.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { ProjectsComponent } from './components/projects/projects.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { ProjectComponent } from './components/project/project.component';
import { EditProjectComponent } from './components/edit-project/edit-project.component';
import { TicketsComponent } from './components/tickets/tickets.component';
import { TicketComponent } from './components/ticket/ticket.component';
import { EditTicketComponent } from './components/edit-ticket/edit-ticket.component';
import { UsersComponent } from './components/users/users.component';
import { TagsComponent } from './components/tags/tags.component';
import { RolesComponent } from './components/roles/roles.component';
import { RegisterComponent } from './components/register/register.component';
import { AddProjectComponent } from './components/add-project/add-project.component';
import { AddTicketComponent } from './components/add-ticket/add-ticket.component';
import { PrioritiesComponent } from './components/priorities/priorities.component';

const routes: Routes = [
  
  ////////////////////////////////////////////////
  {
    path: 'register',
    children: [
      {
        path: '',
        component: HeaderComponent,
        outlet: 'header',
      },
      {
        path: '',
        component: RegisterComponent,
      },
      {
        path: '',
        component: FooterComponent,
        outlet: 'footer',
      },
    ],
  },
  {
    path: '',
    children: [
      {
        path: '',
        component: HeaderComponent,
        outlet: 'header',
      },
      {
        path: '',
        component: RegisterComponent,
      },
      {
        path: '',
        component: FooterComponent,
        outlet: 'footer',
      },
    ],
  },



  ////////////////////////////////////////////////
  {
    path: 'dashboard',
    children: [
      {
        path: '',
        component: HeaderComponent,
        outlet: 'header',
      },
      {
        path: '',
        component: DashBoardComponent,
      },
      {
        path: '',
        component: FooterComponent,
        outlet: 'footer',
      },
    ],
  },

  ////////////////////////////////////////////////
  {
    path: 'projects',
    children: [
      {
        path: '',
        component: HeaderComponent,
        outlet: 'header',
      },
      {
        path: '',
        component: ProjectsComponent,
      },
      {
        path: 'project',
        component: ProjectComponent,
      },
      {
        //could make this a child of project later if needed
        path: 'project/edit',
        component: EditProjectComponent,
      },
      {
        path: 'add',
        component: AddProjectComponent,
      },
      {
        path: '',
        component: FooterComponent,
        outlet: 'footer',
      },
    ],
  },
  ////////////////////////////////////////////////
  {
    path: 'profile',
    children: [
      {
        path: '',
        component: HeaderComponent,
        outlet: 'header',
      },
      {
        path: '',
        component: UserProfileComponent,
      },
      {
        path: '',
        component: FooterComponent,
        outlet: 'footer',
      },
    ],
  },
  ////////////////////////////////////////////////
  {
    path: 'tickets',
    children: [
      {
        path: '',
        component: HeaderComponent,
        outlet: 'header',
      },
      {
        path: '',
        component: TicketsComponent,
      },
      {
        path: '',
        component: FooterComponent,
        outlet: 'footer',
      },
      {
        path: 'ticket',
        component: TicketComponent,
      },
      {
        path: 'ticket/edit',
        component: EditTicketComponent,
      },
      {
        path: 'add',
        component: AddTicketComponent,
      },
    ],
  },

  ////////////////////////////////////////////////
  {
    path: 'users',
    children: [
      {
        path: '',
        component: HeaderComponent,
        outlet: 'header',
      },
      {
        path: '',
        component: UsersComponent,
      },
      {
        path: '',
        component: FooterComponent,
        outlet: 'footer',
      },
    ],
  },
  ////////////////////////////////////////////////
  {
    path: 'tags',
    children: [
      {
        path: '',
        component: HeaderComponent,
        outlet: 'header',
      },
      {
        path: '',
        component: TagsComponent,
      },
      {
        path: '',
        component: FooterComponent,
        outlet: 'footer',
      },
    ],
  },
  ////////////////////////////////////////////////
  {
    path: 'roles',
    children: [
      {
        path: '',
        component: HeaderComponent,
        outlet: 'header',
      },
      {
        path: '',
        component: RolesComponent,
      },
      {
        path: '',
        component: FooterComponent,
        outlet: 'footer',
      },
    ],
  },
  ////////////////////////////////////////////////
  {
    path: 'priorities',
    children: [
      {
        path: '',
        component: HeaderComponent,
        outlet: 'header',
      },
      {
        path: '',
        component: PrioritiesComponent,
      },
      {
        path: '',
        component: FooterComponent,
        outlet: 'footer',
      },
    ],
  },
  {
    path: '',
    outlet: 'side-bar',
    component: SideBarComponent
  },
  {
    path: '**',
    redirectTo: 'dashboard'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
