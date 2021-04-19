import { ParsedEvent } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LocalStorageService } from 'ngx-webstorage';
import { map } from 'rxjs/operators';
import { FileUtil, GlobalVariable } from 'src/app/globals';
import { HospitalRoomService } from 'src/app/services/hospital-room.service';

@Component({
  selector: 'app-view-room',
  templateUrl: './view-room.component.html',
  styleUrls: ['./view-room.component.css']
})
export class ViewRoomComponent implements OnInit {

  roomData: any;
  roomImage: any;
  roomStateData: any;
  submitValid = false;
  imagePath: any = GlobalVariable.DEFAULT_PROFILE_IMG;

  constructor(private activatedRoute: ActivatedRoute,
    private hospitalRoomService: HospitalRoomService,
    private toastr: ToastrService,
    private router: Router,
    private localStorage: LocalStorageService) {

    this.activatedRoute.paramMap
      .pipe(map(() => window.history.state))
      .subscribe(data => {
        console.log('data', data);
        this.roomStateData = data;
        this.submitValid = true;

        if (data.roomData) {
          this.roomData = {
            roomCode: data.roomData.roomCode,
            roomName: data.roomData.roomName,
            description: data.roomData.description,
          }

          this.localStorage.store('viewRoomData', JSON.stringify(this.roomData));

          if (data.image && (data.image instanceof File || data.image instanceof Blob)) {
            this.roomImage = data.image;
            this.imageToUrl(this.roomImage);
          } else {
            this.imagePath = data.imagePath;
          }


          console.log('retrieve from routing', this.roomStateData);
        } else {
          this.retrieveDataFromLocalStorage();
        }
      });
  }

  private retrieveDataFromLocalStorage() {
    const storedRoomData = JSON.parse(this.localStorage.retrieve('viewRoomData'));
    this.roomData = storedRoomData;
    this.imagePath = this.localStorage.retrieve('viewRoomImage');

    if (String(this.imagePath).includes('base64')) {
      this.roomImage = FileUtil.dataURItoBlob(this.imagePath);
    }

    this.roomStateData = {
      image: this.roomImage,
      roomData: this.roomData,
      imagePath: this.imagePath
    }

    console.log('retrieve from localStorage', this.roomImage);
  }

  public back() {

    this.router.navigateByUrl('/hospital_rooms/add', {
      state: this.roomStateData
    });

    this.clearRoomData();
  }

  private clearRoomData() {
    this.localStorage.clear('viewRoomData');
    this.localStorage.clear('viewRoomImage');
  }

  public submit() {
    const formData = new FormData();
    const payloadData = JSON.stringify(this.roomData);

    formData.append('hospitalRoomDto', payloadData);
    formData.append('file', this.roomImage);


    this.hospitalRoomService.addRoom(formData)
      .subscribe(data => {
        console.log('addRoom result', data);
        this.toastr.success('Hospital Room Successfully Added!');
        this.router.navigate(['hospital_rooms']);
      }, error => {
        console.log('addRoom error', error);
        let errorMessage = 'Something went wrong.. Please try again';

        if (error.error && error.status == 409) {
          errorMessage = error.error
        }

        this.toastr.error(errorMessage);
      })
  }

  private imageToUrl(image: any) {
    console.log('image to url', image);
    const reader = new FileReader();
    reader.onload = e => {
      this.imagePath = reader.result
      this.localStorage.store('viewRoomImage', this.imagePath);
    };
    reader.readAsDataURL(image);
  }

  ngOnInit(): void {

  }

}
