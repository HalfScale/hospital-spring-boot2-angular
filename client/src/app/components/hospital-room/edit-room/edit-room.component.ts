import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { map } from 'rxjs/operators';
import { FileUtil, GlobalVariable } from 'src/app/globals';
import { HospitalRoomService } from 'src/app/services/hospital-room.service';
import { HospitalRoomRequest } from '../model/hospital-room.request';

@Component({
  selector: 'app-edit-room',
  templateUrl: './edit-room.component.html',
  styleUrls: ['./edit-room.component.css']
})
export class EditRoomComponent implements OnInit {

  imagePath: any;
  hospitalImage: any;
  hospitalForm: FormGroup;
  hospitalRoomId = 0;
  hospitalRoomRequest: HospitalRoomRequest;
  viewRoomData: any;

  constructor(private fb: FormBuilder,
    private hospitalRoomService: HospitalRoomService,
    private toastr: ToastrService,
    private router: Router,
    private route: ActivatedRoute) {

    this.imagePath = GlobalVariable.DEFAULT_PROFILE_IMG;

    this.hospitalRoomRequest = {
      roomCode: '',
      roomName: '',
      description: ''
    }

    this.hospitalForm = this.fb.group({
      roomCode: ['', Validators.required],
      roomName: ['', Validators.required],
      description: ['', Validators.required]
    });


    const roomId = this.route.snapshot.paramMap.get('roomId');
    console.log('roomId', roomId);

    this.route.paramMap
      .pipe(map(() => window.history.state))
      .subscribe(data => {

        if (data.roomData) {
          this.setFormData(data);
          this.hospitalRoomId = data.roomData.id
          if(data.image) {
            this.hospitalImage = data.image
          }

          if(data.imagePath) {
            this.imagePath = data.imagePath
          }
        } else {
          hospitalRoomService.getRoomById(Number(roomId))
            .subscribe(data => {
              console.log('getRoomById data', data);
              this.hospitalForm.get('roomCode')?.setValue(data.roomCode);
              this.hospitalForm.get('roomName')?.setValue(data.roomName);
              this.hospitalForm.get('description')?.setValue(data.description);
              this.hospitalRoomId = data.id;
              if (data.roomImage) {
                this.imagePath = data.roomImage;
              }
            }, error => {
              console.log('getRoomById error', error);
            });
        }
      });




  }

  ngOnInit(): void {
  }

  public selectImg(event: any) {
    if (event.target.files && event.target.files[0]) {

      const file = event.target.files[0];
      this.setImage(file);
    }
  }

  public removeImage() {
    this.imagePath = GlobalVariable.DEFAULT_PROFILE_IMG;
    this.hospitalImage = undefined;
  }

  private setImage(file: any) {
    const reader = new FileReader();
    reader.onload = e => {
      this.imagePath = reader.result
      console.log(reader.result);
    };
    reader.readAsDataURL(file);
    this.hospitalImage = file;
  }

  public submit() {

    this.setRequestData();
    const payloadData = JSON.stringify(this.hospitalRoomRequest);

    const formData = new FormData();
    formData.append('hospitalRoomDto', payloadData);
    formData.append('file', this.hospitalImage);

    this.printFormData(formData);

    this.hospitalRoomService.updateRoom(this.hospitalRoomId, formData)
      .subscribe(data => {
        console.log('result', data);
        this.toastr.success('Hospital Room Successfully Added!');
        this.router.navigate(['hospital_rooms']);
      }, error => {
        console.log('error', error);
        let errorMessage = 'Something went wrong.. Please try again';

        if (error.error && error.status == 409) {
          errorMessage = error.error
        }

        this.toastr.error(errorMessage);
      });
  }

  public view() {
    this.viewRoomData = {
      state: {
        roomData: {
          id: this.hospitalRoomId,
          roomCode: this.hospitalForm.get('roomCode')?.value,
          roomName: this.hospitalForm.get('roomName')?.value,
          description: this.hospitalForm.get('description')?.value
        },
        image: this.hospitalImage,
        imagePath: this.imagePath
      }
    }
    this.router.navigateByUrl('/hospital_rooms/view/edit', this.viewRoomData);
  }

  private setFormData(data: any) {
    this.hospitalForm.get('roomCode')?.setValue(data.roomData.roomCode);
    this.hospitalForm.get('roomName')?.setValue(data.roomData.roomName);
    this.hospitalForm.get('description')?.setValue(data.roomData.description);

    if (data.image) {
      this.setImage(data.image);
    }
  }

  private setRequestData() {
    this.hospitalRoomRequest.roomCode = this.hospitalForm.get('roomCode')?.value;
    this.hospitalRoomRequest.roomName = this.hospitalForm.get('roomName')?.value;
    this.hospitalRoomRequest.description = this.hospitalForm.get('description')?.value;
  }

  private printFormData(formData: FormData) {
    console.log('payload request', formData);
    formData.forEach((val, key) => console.log('val', val, "key", key));
  }

}
