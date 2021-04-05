import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UserUpdateRequest } from 'src/app/entities/user-update';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  userUpdateRequest: UserUpdateRequest;
  userUpdateForm: FormGroup;

  constructor(private userService: UserService) {
    this.userUpdateRequest = {
      firstName: '',
      lastName: '',
      mobileNo: '',
      address: '',
      birthDate: new Date,
      gender: 0,
      specialization: '',
      noOfYearsExperience: 0,
      description: '',
      education: ''
    };

    this.userUpdateForm = new FormGroup({
      firstName: new FormControl('', Validators.required),
      lastName: new FormControl('', Validators.required),
      mobileNo: new FormControl('', Validators.required),
      address: new FormControl('', Validators.required),
      birthDate: new FormControl(new Date, Validators.required),
      gender: new FormControl(0, Validators.required),
      specialization: new FormControl('', Validators.required),
      noOfYearsExperience: new FormControl(0, Validators.required),
      description: new FormControl('', Validators.required),
      education: new FormControl('', Validators.required)
    });
  }

  ngOnInit(): void {
  }

  public update(): void {

    this.userUpdateRequest.firstName = this.userUpdateForm.get('firstName')?.value;
    this.userUpdateRequest.lastName = this.userUpdateForm.get('lastName')?.value;
    this.userUpdateRequest.mobileNo = this.userUpdateForm.get('mobileNo')?.value;
    this.userUpdateRequest.address = this.userUpdateForm.get('address')?.value;
    this.userUpdateRequest.birthDate = this.userUpdateForm.get('birthDate')?.value;
    this.userUpdateRequest.gender = this.userUpdateForm.get('gender')?.value;
    this.userUpdateRequest.specialization = this.userUpdateForm.get('specialization')?.value;
    this.userUpdateRequest.noOfYearsExperience = this.userUpdateForm.get('noOfYearsExperience')?.value;
    this.userUpdateRequest.description = this.userUpdateForm.get('description')?.value;
    this.userUpdateRequest.education = this.userUpdateForm.get('education')?.value;

    console.log('update payload', this.userUpdateRequest);

    // this.userService.updateProfile(this.userUpdateRequest)
    //   .subscribe(data => {
    //     console.log('success', data);
    //   },
    //     error => {
    //       console.log('error', error);
    //     })
  }
}
