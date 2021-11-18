import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {CreateListComponent} from "./create-list/create-list.component";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {EditListComponent} from "./edit-list/edit-list.component";
import {RequestListComponent} from "./request-list/request-list.component";

import {ProfileComponent} from "./profile/profile.component";
import {RegisterComponent} from "./register/register.component";
import {LoginComponent} from "./login/login.component";



const routes: Routes = [
  { path: 'create-list', component: CreateListComponent},
  {path: 'dashboard', component: DashboardComponent},
  { path: 'edit-list/:id', component: EditListComponent},
    {path: 'request-list', component: RequestListComponent},
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', component: ProfileComponent },

  {path: '', component: RequestListComponent}

];

@NgModule({
  imports: [
    [RouterModule.forRoot(routes)] ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
export const routingComponents=[CreateListComponent,EditListComponent,DashboardComponent,RequestListComponent,LoginComponent,RegisterComponent  ]
