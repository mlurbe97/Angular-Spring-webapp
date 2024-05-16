import { Component, OnInit } from '@angular/core';
import { AuthService } from '../_services/auth.service';
import { UserService } from '../_services/user.service';
import { TokenService } from '../_services/token.service'; 
import { Router } from '@angular/router';
import { Usuario } from '../_interfaces/user.type';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form: any = {
    username: null,
    password: null
  };
  user: any = {
    username: ""
  }

  currentUser:Usuario | null = null;
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  user_token = '';
  roles: string[] = [];
  

  constructor(private router:Router, private authService: AuthService, private tokenService: TokenService, private userService: UserService) { }

  ngOnInit(): void {
    if (this.tokenService.isLoggedIn()) {
      this.isLoggedIn = true;
      this.user_token = this.tokenService.getToken();
      //this.roles = this.userService.getUser().roles;
    } else {
      this.isLoggedIn = false;
    }
    console.log("INFO: LoginComponent init() ¿El usuario esta logeado? "+this.isLoggedIn);
    console.log("INFO: LoginComponent init() ¿Roles del usuario? "+this.roles);
  }

  onSubmit(): void {
    const { username, password } = this.form;
    console.log("INFO: LoginComponent onSubmit() username: %s password: %s",username, password);
    this.authService.login(username, password).subscribe({
      next: data => {
        console.log(data);
        console.log(data.access_token);
        this.user_token = data.access_token;
        this.tokenService.saveToken(this.user_token);
        console.log("INFO: LoginComponent onSubmit() Login completado");
        console.log("INFO: LoginComponent onSubmit() Token "+ this.user_token);
        this.currentUser = this.userService.getUser(this.user_token);
        console.log(this.currentUser);
        if(this.currentUser == null){
          this.isLoginFailed = true;
          this.isLoggedIn = false;
        }else{
          this.isLoginFailed = false;
          this.isLoggedIn = true;
        }
        //var user = this.storageService.getUser();
        //console.log("INFO: onSubmit() username: %s password: %s",user.username, user.password);
        
        //this.roles = this.userService.getUser().roles;
        //this.reloadPage();
        //this.router.navigate(["/home"]);
      },
      error: err => {
        this.errorMessage = err.error.message;
        this.isLoginFailed = true;
        this.isLoggedIn = false;
        console.log("ERROR: LoginComponent onSubmit() error al hacer login");
        console.log(this.errorMessage);
      }
    });
  }

  /*reloadPage(): void {
    console.log("ERROR: LoginComponent reloadPage() error al hacer login");
    window.location.reload();
  }*/
}
