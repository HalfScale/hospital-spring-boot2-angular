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

  constructor(private hospitalRoomService: HospitalRoomService) {
    this.initializeConfig();
  }

  initializeConfig(): void {
    this.config = {
      itemsPerPage: 10,
      currentPage: 1,
      totalItems: 0
    };
  }

  getCurrentPage(): string {
    return (this.config.currentPage - 1).toString();
  }

  getPageSize(): string {
    return this.config.itemsPerPage.toString();
  }

  ngOnInit(): void {
    this.getHospitalRooms();
  }

  getHospitalRooms(): void {
    this.hospitalRoomService.getRooms(this.getCurrentPage(), this.getPageSize()).subscribe({
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
    this.getHospitalRooms();
  }
}
