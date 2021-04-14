import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GlobalVariable } from 'src/app/globals';
import { HospitalRoomService } from 'src/app/services/hospital-room.service';
import { HospitalRoomRequest } from '../model/hospital-room.request';

@Component({
  selector: 'app-add-edit-room',
  templateUrl: './add-edit-room.component.html',
  styleUrls: ['./add-edit-room.component.css']
})
export class AddEditRoomComponent implements OnInit {

  imagePath: any;
  hospitalImage: any;
  hospitalForm: FormGroup;
  hospitalRoomRequest: HospitalRoomRequest;

  constructor(private fb: FormBuilder,
    private hospitalRoomService: HospitalRoomService,
    private toastr: ToastrService,
    private router: Router) { 

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
  }

  ngOnInit(): void {
  }

  public selectImg(event: any) {
    if(event.target.files && event.target.files[0]) {
      const file = event.target.files[0];

      const reader = new FileReader();
      reader.onload = e => this.imagePath = reader.result;
      reader.readAsDataURL(file);
      this.hospitalImage = file;
    }
  }

  public submit() {

    this.setRequestData();
    const payloadData = JSON.stringify(this.hospitalRoomRequest);

    const formData = new FormData();
    formData.append('hospitalRoomDto', payloadData);
    formData.append('file', this.hospitalImage);

    this.printFormData(formData);

    this.hospitalRoomService.addRoom(formData)
      .subscribe(data => {
        console.log('result', data);
        this.toastr.success('Hospital Room Successfully Added!');
        this.router.navigate(['hospital_rooms']);
    }, error => {
      console.log('error', error);
      let errorMessage = 'Something went wrong.. Please try again';

      if(error.error && error.status == 409) {
        errorMessage = error.error
      }

      this.toastr.error(errorMessage);
    });
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
