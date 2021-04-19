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

    const roomId = this.activatedRoute.snapshot.paramMap.get('roomId');
    hospitalRoomService.getRoomById(Number(roomId))
      .subscribe(data => {
        console.log('getRoomById =>', data);
        this.setRoomData(data);
      });
  }

  private setRoomData(data: any) {
    this.roomData = {
      roomCode: data.roomCode,
      roomName: data.roomName,
      description: data.description
    }

    if(data.roomImage) {
      this.roomImagePath = data.roomImage;
    }
  }

  ngOnInit(): void {
  }

}
