import { Component } from '@angular/core';
import { Subscription } from 'rxjs';
import { StorageService } from './_services/storage.service';
import { AuthService } from './_services/auth.service';
import { EventBusService } from './_shared/event-bus.service';
import { TokenService } from './_services/token.service';
import { UserService } from './_services/user.service';
import { Usuario } from './_interfaces/user.type';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  private roles: string [] = [];
  isLoggedIn = false;
  showAdminBoard = false;
  showModeratorBoard = false;
  username?: string;
  user_token: string = '';
  eventBusSub?: Subscription;
  user:Usuario | null = null;
  constructor(
    private authService: AuthService,
    private userService: UserService,
    private tokenService: TokenService,
    private eventBusService: EventBusService
  ) {}

  ngOnInit(): void {
    this.isLoggedIn = this.tokenService.isLoggedIn();
    console.log(window.localStorage.length);
    console.log(window.localStorage.key(0));
    console.log("INFO: AppComponent init() ESTAS LOGEADO -> "+this.isLoggedIn);
    if (this.isLoggedIn) {
      this.user_token = this.tokenService.getToken();
      this.user = this.userService.getUser(this.user_token);
      if(this.user != null){
        this.roles.push(this.user.getRole());
        this.showAdminBoard = this.roles.includes('ROLE_ADMIN');
        this.showModeratorBoard = this.roles.includes('ROLE_MODERATOR');
        this.username = this.user.getUsername();
        this.isLoggedIn = true;
      }
    }
    this.eventBusSub = this.eventBusService.on('logout', () => {
      this.logout();
    });
  }

  logout(): void {
    console.log("INFO: AppComponent logout() ESTAS LOGEADO -> "+this.isLoggedIn);
    this.authService.logout().subscribe({
      next: res => {
        console.log("Logout success!");
        console.log(res);
        this.tokenService.clean();
        this.userService.clean();
        this.roles = [];
        this.isLoggedIn = false;//this.tokenService.isLoggedIn();
        window.location.reload();
      },
      error: err => {
        console.log("Logout error!");
        console.log(err);
      }
    });
    console.log("INFO: AppComponent logout() Logout end!");
  }
}
