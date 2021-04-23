import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GlobalVariable } from '../globals';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  private apiUrl = GlobalVariable.BASE_API_URL;

  constructor(private http: HttpClient) { }

  public getAvailableReservationDate(): Observable<any> {
    return this.http.get<any>(this.apiUrl + "/api/reservations/available_date");
  }
}
