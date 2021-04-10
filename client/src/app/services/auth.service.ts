import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import {Observable , throwError} from 'rxjs';
import { map, tap  } from 'rxjs/operators';
import { JwtAuthResponse } from '../entities/jwt-auth-response';
import { LoginForm } from '../entities/login-form';
import { GlobalVariable } from '../globals'


const httpOptions = {
  headers: new HttpHeaders({
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  })
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  private apiUrl = GlobalVariable.BASE_API_URL;

  refreshTokenPayload = {
    refreshToken: this.getRefreshToken(),
    email: this.getEmail()
  }

  constructor(private http: HttpClient, private localStorageService: LocalStorageService) { }

  public loginUser(loginForm: LoginForm): Observable<boolean>{
    return this.http.post<JwtAuthResponse>(this.apiUrl + "/api/auth/login", loginForm, httpOptions)
      .pipe(map(data => {
        console.log('login data', data);
        this.localStorageService.store("authToken", data.authToken);
        this.localStorageService.store("email", data.email);
        this.localStorageService.store("refreshToken", data.refreshToken);
        this.localStorageService.store("expiresAt", data.expiresAt);
        this.localStorageService.store("role", data.role);
        this.localStorageService.store("name", data.name);
        return true;
      }));
  }

  public sendResetPasswordNotif(emailString: string): Observable<any> {
    const requestBody = {
      email: emailString
    };

    return this.http.post(this.apiUrl + '/api/auth/password/reset/notification', requestBody);
  }

  public updatePassword(payload: any) : Observable<any> {
    return this.http.post(this.apiUrl + '/api/auth/password/reset', payload);
  }

  public logout() {
    this.http.post(this.apiUrl + '/logout', this.refreshTokenPayload)
      .subscribe(data => {
        console.log('data', data);
      }, error => {
        throwError(error);
      });
    this.localStorageService.clear('authToken');
    this.localStorageService.clear('email');
    this.localStorageService.clear('expiresAt');
    this.localStorageService.clear('name');
    this.localStorageService.clear('refreshToken');
    this.localStorageService.clear('role');
  }

  refreshToken() {

    return this.http.post<JwtAuthResponse>(this.apiUrl + '/api/auth/refresh/token', this.refreshTokenPayload)
      .pipe(tap(response => {
        this.localStorageService.clear('authToken');
        this.localStorageService.clear('expiresAt');

        this.localStorageService.store('authToken', response.authToken)
        this.localStorageService.store('expiresAt', response.expiresAt);
      }));
  }

  public getJwtToken() {
    return this.localStorageService.retrieve('authToken');
  }

  public getRefreshToken() {
    return this.localStorageService.retrieve('refreshToken');
  }

  public getEmail() {
    return this.localStorageService.retrieve('email');
  }

  public getExpirationTime() {
    return this.localStorageService.retrieve('expiresAt');
  }

  public getName() {
    return this.localStorageService.retrieve('name');
  }

  public getRole() {
    return this.localStorageService.retrieve('role');
  }

  public isLoggedIn(): boolean {
    return this.getJwtToken() != null;
  }
}
