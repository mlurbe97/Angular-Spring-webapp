import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenService } from './token.service';
import { Usuario } from '../_interfaces/user.type';

const API_URL = 'http://localhost:8080/api';
const USERS_API = 'http://localhost:8080/api/users';
const USER_KEY = 'local-user-json';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  })
};

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient, private tokenService: TokenService) {}
  tokenType  = 'Bearer ';

  local_user:Usuario | null = null;

  clean(): void {
    localStorage.clear();
  }

  public saveUser(user: Usuario): void {
    console.log("INFO: UserService saveUser() GUARDO EL USUARIO");
    localStorage.removeItem(USER_KEY);
    console.log(JSON.stringify(user))
    localStorage.setItem(USER_KEY, JSON.stringify(user));
    this.local_user = user;
  }

  public getUser(user_token:string): Usuario | null {
    this.getUserWeb(user_token).subscribe(
      data => {
        this.local_user = new Usuario(data.userId,data.username,data.role,data.email);
        this.local_user.setUserToken(user_token);
        this.saveUser(this.local_user);
        return this.local_user;
      },
      err => { console.log(err) }
    );
    return this.local_user;
  }

  public getUserWeb(user_token:string): Observable<any> {
    const header = new HttpHeaders()
    .set( 'Content-Type' , 'json' )
    .set('Authorization', this.tokenType+user_token);
    const headers = { headers: header };
    console.log(headers);
    return this.http.get(API_URL + '/users/me',headers);
    // return 
   /* if (this.local_user) {
      console.log("INFO: UserService getUser() DEVUELVO EL USUARIO");
      return JSON.parse(this.local_user);
    }else{
      console.log("ERROR: UserService getUser() NO HAY EL USUARIO");
      return null;
    } */
  }

  getUserBoard(): Observable<any> {
    const header = new HttpHeaders()
    .set('Content-Type', 'application/json')
    .set('Authorization', this.tokenService.getToken());
    const headers = { headers: header };
    console.log("INFO: UserService getUserBoard()");
    return this.http.get(API_URL + '/users/me', headers);
  }
  
  getModeratorBoard(): Observable<any> {
    console.log("INFO: UserService getModeratorBoard()");
    return this.http.get(API_URL + '/users/mod', { responseType: 'json' });
  }

  getAdminBoard(): Observable<any> {
    console.log("INFO: UserService getAdminBoard()");
    return this.http.get(API_URL + '/admin/me', { responseType: 'json' });
  }
}
