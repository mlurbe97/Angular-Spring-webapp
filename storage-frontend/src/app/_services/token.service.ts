import { Injectable } from '@angular/core';

const USER_TOKEN = 'auth-user';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  constructor() {}

  local_token:any=null;

  clean(): void {
    //window.localStorage.setItem(USER_TOKEN,"");
    //window.localStorage.removeItem(USER_TOKEN);
    //window.localStorage.clear();
    localStorage.removeItem(USER_TOKEN);
    sessionStorage.clear();
    localStorage.clear();
    //window.sessionStorage = [];
    this.local_token = null;
    console.log(localStorage.length);
    location.reload();
  }

  public saveToken(new_token: any): void {
    console.log("INFO: TokenService saveUser() GUARDO EL USUARIO -> "+new_token);
    this.clean();
    localStorage.setItem(USER_TOKEN,new_token);
    this.local_token = new_token;
  }

  public getToken(): string {
    this.local_token = localStorage.getItem(USER_TOKEN);
    if (this.local_token) {
      console.log("INFO: TokenService getToken() RECUPERO EL TOKEN DEL USUARIO -> "+this.local_token);
      return this.local_token;
    }else{
      console.log("ERROR: TokenService getToken() NO HAY TOKEN PARA EL USUARIO");
    return "Not logged in.";
    }
    
  }

  public isLoggedIn(): boolean {
    this.local_token = localStorage.getItem(USER_TOKEN);
    if (this.local_token != null) {
      console.log("INFO: TokenService isLoggedIn() local_token -> "+this.local_token);
      return true;
    }else{
      console.log("INFO: TokenService isLoggedIn() USUARIO NO LOGGEADO");
      return false;
    }
    
  }
}
