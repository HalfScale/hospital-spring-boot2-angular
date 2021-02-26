import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import {Observable} from 'rxjs';
import { map } from 'rxjs/operators';
import { JwtAuthResponse } from '../entities/jwt-auth-response';
import { LoginForm } from '../entities/login-form';


const httpOptions = {
  headers: new HttpHeaders({
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  })
}

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private apiUrl = "http://localhost:8070/hospital";

  constructor(private http: HttpClient, private localStorageService: LocalStorageService) { }

  public loginUser(loginForm: LoginForm): Observable<boolean>{
    return this.http.post<JwtAuthResponse>(this.apiUrl + "/auth/login", loginForm, httpOptions)
      .pipe(map(data => {
        this.localStorageService.store("authToken", data.authToken);
        this.localStorageService.store("email", data.email);
        return true;
      }));
  }
}
