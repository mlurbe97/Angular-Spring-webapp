import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const AUTH_API = 'http://localhost:8080/api/auth/';
const URL_API = 'http://localhost:8080/api/';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
    })

};

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    console.log("INFO: AuthService login()");
    return this.http.post(
      AUTH_API + 'login',
      {
        "username":username,
        "password":password,
      },
      httpOptions
    );
    
  }

  register(username: string, email: string, password: string): Observable<any> {
    var actual_date = Date.now(); 
    var out_res = this.http.post(
      URL_API + 'register',
      {
          "username": username,
          "password":password,
          "email":email,
          "enabled":"true",
          "lastPasswordResetDate":actual_date,
          "create_time":actual_date,
          "role":"USER"
      },
      httpOptions
    );
    console.log("INFO: AuthService register()");
    console.log(out_res);
    return out_res;
  }

  logout(): Observable<any> {
    console.log("INFO: AuthService logout()");
    return this.http.post(URL_API + 'logout', { }, httpOptions);
  }
}
