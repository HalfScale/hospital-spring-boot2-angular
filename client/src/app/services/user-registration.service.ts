import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { RegistrationForm } from '../entities/registration-form';
import { User } from '../entities/user';

const httpOptions = {
  headers: new HttpHeaders({ 
    'Accept':'application/json',
    'Content-Type': 'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})
export class UserRegistrationService {

  private apiUrl = "http://localhost:8090/hospital/processRegistration";

  constructor(private http: HttpClient) { }

  public registerUser(registrationForm: RegistrationForm): Observable<any> {
    return this.http.post<any>(this.apiUrl, registrationForm, httpOptions);
  }
  
}
