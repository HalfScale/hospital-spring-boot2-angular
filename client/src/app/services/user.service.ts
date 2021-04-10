import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserUpdateRequest } from '../entities/user-update';
import { GlobalVariable } from '../globals';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = GlobalVariable.BASE_API_URL;

  constructor(private http: HttpClient) { }

  public updateProfile(userUpdateRequest: FormData): Observable<any> {
    return this.http.put<any>(this.apiUrl + '/api/users', userUpdateRequest);
  }

  public getLoggedUser(): Observable<any> {
    return this.http.get<any>(this.apiUrl + '/api/users');
  }
}
