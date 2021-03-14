import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { RegistrationForm } from '../entities/registration-form';
import { GlobalVariable } from '../globals';

@Injectable({
  providedIn: 'root'
})
export class UserRegistrationService {

  private apiUrl = GlobalVariable.BASE_API_URL;

  constructor(private http: HttpClient) { }

  public registerUser(registrationForm: RegistrationForm): Observable<any> {
    return this.http.post<any>(this.apiUrl + "/api/auth/registration", registrationForm);
  }
  
}
