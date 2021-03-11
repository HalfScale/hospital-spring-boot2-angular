import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import {Observable} from 'rxjs';
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

  constructor(private http: HttpClient, private localStorageService: LocalStorageService) { }

  public loginUser(loginForm: LoginForm): Observable<boolean>{
    return this.http.post<JwtAuthResponse>(this.apiUrl + "/auth/login", loginForm, httpOptions)
      .pipe(map(data => {
        this.localStorageService.store("authToken", data.authToken);
        this.localStorageService.store("email", data.email);
        this.localStorageService.store("refreshToken", data.refreshToken);
        this.localStorageService.store("expiresAt", data.expiresAt);
        return true;
      }));
  }

  refreshToken() {
    const refreshTokenPayLoad = {
      refreshToken: this.getRefreshToken(),
      email: this.getEmail()
    }

    return this.http.post<JwtAuthResponse>(this.apiUrl, refreshTokenPayLoad)
      .pipe(tap(response => {
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

  public isLoggedIn(): boolean {
    return this.getJwtToken() != null;
  }
}
