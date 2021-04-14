import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { UserUpdateRequest } from 'src/app/entities/user-update';
import { GlobalVariable } from 'src/app/globals';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  userUpdateRequest: UserUpdateRequest;
  userUpdateForm: FormGroup;
  profileImage: any;
  imagePath: any;
  userRole: number;
  disableSubmit: String = '';

  constructor(private userService: UserService,
    private authService: AuthService,
    private toastr: ToastrService,
    private fb: FormBuilder) {

    this.imagePath = GlobalVariable.DEFAULT_PROFILE_IMG;
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

    this.userUpdateForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName:  ['', Validators.required],
      mobileNo:  ['', Validators.required],
      address:  [''],
      birthDate:  [''],
      gender:  ['1'],
      specialization:  [''],
      noOfYearsExperience:  [0],
      description:  [''],
      education:  ['']
    });

    this.userRole = authService.getRole();
  }

  ngOnInit(): void {
    this.userService.getLoggedUser().subscribe(data => {
      console.log('logged user =>', data);
      this.imagePath = data.profileImage ? data.profileImage : GlobalVariable.DEFAULT_PROFILE_IMG;
      this.userUpdateForm.setValue({
        firstName: data.firstName,
        lastName: data.lastName,
        mobileNo: data.mobileNo,
        address: data.address,
        birthDate: data.birthDate,
        gender: data.gender,
        specialization: data.specialization,
        noOfYearsExperience: data.noOfYearsExperience,
        description: data.description,
        education: data.education
      });
    }, error => {
      console.log('error', error);
    });
  }

  public update(): void {

    this.setRequestData();
    const payloadData = JSON.stringify(this.userUpdateRequest);

    const formData = new FormData();
    formData.append('updateData', payloadData);
    formData.append('file', this.profileImage);

    formData.forEach((val, key) => console.log('val', val, "key", key));
    
    console.log('formData', formData);

    this.userService.updateProfile(formData)
      .subscribe(data => {
        console.log('success', data);
        this.toastr.success('Profile updated successfully!');
      },error => {
          console.log('error', error);
      })
  }

  private setRequestData() {
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
  }

  public selectImg(event: any) {
    if(event.target.files && event.target.files[0]) {
      const file = event.target.files[0];

      const reader = new FileReader();
      reader.onload = e => this.imagePath = reader.result;
      reader.readAsDataURL(file);
      this.profileImage = file;
    }
  }
}
