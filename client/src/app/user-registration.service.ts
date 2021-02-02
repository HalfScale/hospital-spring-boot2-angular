import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from './user';

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

  constructor(private http: HttpClient) { }

  public registerUser(user: User) {
    return this.http.post("http://localhost:8090/hospital/processRegistration", user, httpOptions);
  }
}
