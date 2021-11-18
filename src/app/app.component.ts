import {Component, OnDestroy, OnInit} from '@angular/core';
import {TokenStorageService} from "./_services/token-storage.service";
import {EventBusService} from "./_shared/event-bus.service";
import {Subscription} from "rxjs";
import {LoginComponent} from "./login/login.component";

@Component({
  selector: 'my-app',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit, OnDestroy {
  private roles: string[] = [];
  isLoggedIn = false;
  showAdminBoard = false;
  showUserBoard = false;
  username?: string;

  eventBusSub?: Subscription;

  constructor(private tokenStorageService: TokenStorageService, private eventBusService: EventBusService) { }

  ngOnInit(): void {

    this.isLoggedIn = !!this.tokenStorageService.getToken();

    if (this.isLoggedIn) {
      const user = this.tokenStorageService.getUser();
      this.roles = user.roles;

      this.showAdminBoard = this.roles.includes('ROLE_ADMIN');
      localStorage.setItem("ifAdmin",String(this.showAdminBoard))


      this.showUserBoard = this.roles.includes('ROLE_USER');
      localStorage.setItem("ifUser",String(this.showUserBoard))

      this.username = user.username;
    }

    this.eventBusSub = this.eventBusService.on('logout', () => {
      this.logout();
    });

  }

  ngOnDestroy(): void {
    if (this.eventBusSub)
      this.eventBusSub.unsubscribe();
  }

  logout(): void {
    this.tokenStorageService.signOut();

    this.isLoggedIn = false;
    this.roles = [];
    this.showAdminBoard = false;
    this.showUserBoard = false;
  }


}


