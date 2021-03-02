import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GlobalVariable } from '../globals';

@Injectable({
  providedIn: 'root'
})
export class ConfirmRegistrationService {
private baseApiUrl = GlobalVariable.BASE_API_URL;

  constructor(private http: HttpClient) { }

  public validateToken(token: any): Observable<any>{

    return this.http.get<any>(this.baseApiUrl + '/registration/confirm/' + token);
  }
}
