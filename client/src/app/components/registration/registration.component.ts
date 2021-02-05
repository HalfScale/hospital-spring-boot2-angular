import { Component, OnInit } from '@angular/core';
import { RegistrationForm } from 'src/app/entities/registration-form';
import { UserRegistrationService } from '../../services/user-registration.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  registrationForm: RegistrationForm;
  registrationFormError: any;

  constructor(private registrationService: UserRegistrationService) { 
    this.registrationForm = new RegistrationForm();
    this.registrationFormError = {};
  }

  ngOnInit(): void {
  }

  public register() {
    this.registrationFormError = {}; // set errors as empty
    console.log('registrationForm', this.registrationForm);
    let registerResponse = this.registrationService.registerUser(this.registrationForm);
    registerResponse.subscribe({
      next: data => {
        console.log('response', data);
      },
      error: error => {
        console.log('error', error.error.data);
        const errorData = error.error.data;
        Object.keys(errorData).forEach(key => {
          this.registrationFormError[key] = errorData[key];
        });
      },
    });
    
  }

  public setGender(gender: string) {
    this.registrationForm.gender = gender;
  }

}
