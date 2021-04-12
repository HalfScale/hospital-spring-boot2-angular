import { Component, OnInit } from '@angular/core';
import { HospitalRoomService } from 'src/app/services/hospital-room.service';

@Component({
  selector: 'app-hospital-room',
  templateUrl: './hospital-room.component.html',
  styleUrls: ['./hospital-room.component.css']
})
export class HospitalRoomComponent implements OnInit {

  config: any;
  collection = { data: [] as any };
  roomCode: String;
  roomName: String;
  requestParams: any;

  constructor(private hospitalRoomService: HospitalRoomService) {
    this.initializeConfig();
    this.roomCode = '';
    this.roomName = '';
  }

  initializeConfig(): void {
    this.config = {
      itemsPerPage: 10,
      currentPage: 1,
      totalItems: 0
    };
  }

  public search(): void {
    this.requestParams = {
      page: this.getCurrentPage(),
      size: this.getPageSize(),
      roomCode: this.roomCode,
      roomName: this.roomName,
    }
    console.log('requestParams', this.requestParams);
    this.getHospitalRooms(this.requestParams);
  }

  public clear(): void {
    this.roomCode = '';
    this.roomName = '';
  }

  getCurrentPage(): string {
    return (this.config.currentPage - 1).toString();
  }

  getPageSize(): string {
    return this.config.itemsPerPage.toString();
  }

  ngOnInit(): void {
    this.getHospitalRooms(this.requestParams);
  }

  getHospitalRooms(params: any): void {
    this.hospitalRoomService.getRoomsPageable(params).subscribe({
      next: data => {
        console.log('Query success', data);
        this.collection.data = data.content;
        this.config.totalItems = data.totalElements;
      },
      error: error => {
        console.log('Error respnse', error);

      }
    });
  }

  pageChanged(currentPage: number) {
    this.config.currentPage = currentPage;
    this.search();
  }
}
