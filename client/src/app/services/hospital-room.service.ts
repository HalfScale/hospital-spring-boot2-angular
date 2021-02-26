import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GlobalVariable } from '../globals';



@Injectable({
  providedIn: 'root'
})
export class HospitalRoomService {

  private baseApiUrl = GlobalVariable.BASE_API_URL;

  constructor(private http: HttpClient) { }

  public getRooms(page: string, size: string) : Observable<any>{
    const httpOptions = {
      headers: new HttpHeaders({
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }),
      params: new HttpParams({
        fromObject: {
          page: page,
          size: size
        }
      })
    };
    return this.http.get<any>(this.baseApiUrl + '/api/test/rooms', httpOptions);
  }
}
