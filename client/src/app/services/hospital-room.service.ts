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
    return this.http.get<any>(this.baseApiUrl + '/api/rooms/pageable', httpOptions);
  }

  public addRoom(formData: FormData): Observable<any> {
    return this.http.post<any>(this.baseApiUrl + '/api/rooms', formData);
  }

  public updateRoom(formData: FormData): Observable<any> {
    return this.http.put<any>(this.baseApiUrl + '/api/rooms', formData);
  }

  public deleteRoom(roomId: number): Observable<any>{
    return this.http.delete(this.baseApiUrl + '/api/rooms/' + roomId);
  }
}
