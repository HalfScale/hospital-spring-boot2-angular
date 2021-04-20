import { Route } from '@angular/compiler/src/core';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LocalStorageService } from 'ngx-webstorage';
import { GlobalVariable } from 'src/app/globals';
import { HospitalRoomService } from 'src/app/services/hospital-room.service';

@Component({
  selector: 'app-details-room',
  templateUrl: './details-room.component.html',
  styleUrls: ['./details-room.component.css']
})
export class DetailsRoomComponent implements OnInit {

  roomData: any;
  roomImagePath: any = GlobalVariable.DEFAULT_PROFILE_IMG;

  constructor(private activatedRoute: ActivatedRoute,
    private hospitalRoomService: HospitalRoomService,
    private toastr: ToastrService,
    private router: Router) {

    const roomId = this.getRoomIdFromPathVariable();
    hospitalRoomService.getRoomById(Number(roomId))
      .subscribe(data => {
        console.log('getRoomById =>', data);
        this.setRoomData(data);
      });
  }

  private getRoomIdFromPathVariable() {
    return this.activatedRoute.snapshot.paramMap.get('roomId');
  }

  private setRoomData(data: any) {
    this.roomData = {
      id: data.id,
      roomCode: data.roomCode,
      roomName: data.roomName,
      description: data.description
    }

    if(data.roomImage) {
      this.roomImagePath = data.roomImage;
    }
  }

  public editRoom() {
    this.router.navigateByUrl(`/hospital_rooms/edit/${this.roomData.id}`)
  }

  ngOnInit(): void {
  }

}
