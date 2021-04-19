import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GlobalVariable } from '../globals';

@Injectable({
  providedIn: 'root'
})
export class HospitalRoomService {

  private baseApiUrl = GlobalVariable.BASE_API_URL + '/api/rooms';

  constructor(private http: HttpClient) { }

  public getRoomsPageable(requestParams: any) : Observable<any>{
    const httpOptions = {
      headers: new HttpHeaders({
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }),
      params: new HttpParams({
        fromObject: requestParams
      })
    };
    return this.http.get<any>(this.baseApiUrl + '/pageable', httpOptions);
  }

  public getRoomById(roomId: number): Observable<any> {
    return this.http.get(this.baseApiUrl + `/${roomId}`);
  }

  public addRoom(formData: FormData): Observable<any> {
    return this.http.post<any>(this.baseApiUrl, formData);
  }

  public updateRoom(roomId: number, formData: FormData): Observable<any> {
    return this.http.put<any>(this.baseApiUrl + `/${roomId}`, formData);
  }

  public deleteRoom(roomId: number): Observable<any>{
    return this.http.delete(this.baseApiUrl + `/${roomId}`);
  }
}
