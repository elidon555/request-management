import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { HelloComponent } from './hello.component';
import {AppRoutingModule, routingComponents} from './app-routing.module';
import { CreateListComponent } from './create-list/create-list.component';
import { EditListComponent } from './edit-list/edit-list.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { RequestListComponent } from './request-list/request-list.component';
import {HttpClientModule} from "@angular/common/http";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {NgxPaginationModule} from "ngx-pagination";
import { RegisterComponent } from './register/register.component';

import {ProfileComponent} from "./profile/profile.component";
import {LoginComponent} from "./login/login.component";
import {authInterceptorProviders} from "./_helpers/auth.interceptor";

@NgModule({
  imports:      [ BrowserModule, FormsModule, ReactiveFormsModule, AppRoutingModule,HttpClientModule,FontAwesomeModule,NgxPaginationModule ],

  declarations:
      [ AppComponent, HelloComponent, CreateListComponent, EditListComponent,
    DashboardComponent, RequestListComponent,routingComponents, RegisterComponent,
 ProfileComponent,LoginComponent ],
  providers: [authInterceptorProviders],
  bootstrap:    [ AppComponent ]

})

export class AppModule { }
