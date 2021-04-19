import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LocalStorageService } from 'ngx-webstorage';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { FileUtil, GlobalVariable } from 'src/app/globals';
import { HospitalRoomService } from 'src/app/services/hospital-room.service';

@Component({
  selector: 'app-view-edit-room',
  templateUrl: './view-edit-room.component.html',
  styleUrls: ['./view-edit-room.component.css']
})
export class ViewEditRoomComponent {

  roomData: any;
  roomImage: any;
  roomStateData: any;
  submitValid = false;
  imagePath:any = GlobalVariable.DEFAULT_PROFILE_IMG;

  constructor(private activatedRoute: ActivatedRoute,
    private hospitalRoomService: HospitalRoomService,
    private toastr: ToastrService,
    private router: Router,
    private localStorage: LocalStorageService) {

    this.getRoomPathVariableId().subscribe(data => {
      this.submitValid = true;

      if (data.roomData) {
        console.log('data from routing =>', data)
        this.roomStateData = data;
        this.setRoomData(data.roomData); // for submitting of data
        this.setRoomDataToLocalStorage(data.roomData); // for persisting data if page is refreshed
        this.setImage(data);
      } else {
        // user refreshed page, gets the data from localstorage
        const storedRoomData = JSON.parse(this.localStorage.retrieve('viewRoomData'));
        this.roomData = storedRoomData;
        this.imagePath = this.localStorage.retrieve('viewRoomImage');
        console.log('imagePath', this.imagePath);

        // if the image is base64 then its a update to the hospital image
        // else retain
        if (String(this.imagePath).includes('base64')) {
          this.roomImage = FileUtil.dataURItoBlob(this.imagePath);
        }

        this.roomStateData = {
          image: this.roomImage,
          imagePath: this.imagePath,
          roomData: this.roomData
        }

        console.log('data from localstorage =>', this.roomData);
      }
    });
  }

  private setRoomData(data: any) {
    this.roomData = {
      id: data.id,
      roomCode: data.roomCode,
      roomName: data.roomName,
      description: data.description
    }
  }

  private setRoomDataToLocalStorage(data: any) {
    this.localStorage.store('viewRoomData', JSON.stringify(data));
  }

  private setImage(data: any) {

    if (data.image && (data.image instanceof File || data.image instanceof Blob)) {
      this.roomImage = data.image; // for request payload
      this.convertFileToDataURL(this.roomImage);
    } else {
      this.imagePath = data.imagePath;
      this.localStorage.store('viewRoomImage', this.imagePath); // for image preview
    }

  }

  private convertFileToDataURL(file: any) {
    const reader = new FileReader();
    reader.onload = e => {
      this.imagePath = reader.result;
      this.localStorage.store('viewRoomImage', this.imagePath);
    };
    reader.readAsDataURL(file);
  }

  private getRoomPathVariableId(): Observable<any> {
    return this.activatedRoute.paramMap.pipe(
      map(() => window.history.state)
    );
  }

  public submit() {
    const formData = new FormData();
    const payloadData = JSON.stringify(this.roomData);

    formData.append('hospitalRoomDto', payloadData);
    formData.append('file', this.roomImage);

    this.hospitalRoomService.updateRoom(this.roomData.id, formData)
      .subscribe(data => {
        console.log('updateRoom result', data);
        this.toastr.success('Hospital Room Successfully Updated!');
        this.router.navigate(['hospital_rooms']);
      }, error => {
        console.log('updateRoom error', error);
        let errorMessage = 'Something went wrong.. Please try again';

        if (error.error && error.status == 409) {
          errorMessage = error.error
        }

        this.toastr.error(errorMessage);
      });
  }

  public back() {
    this.localStorage.clear('viewRoomData');
    this.localStorage.clear('viewRoomImage');
    this.router.navigateByUrl(`/hospital_rooms/edit/${this.roomData.id}`, {
      state: this.roomStateData
    })
  }

}
