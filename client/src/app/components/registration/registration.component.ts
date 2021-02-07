import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RegistrationForm } from 'src/app/entities/registration-form';
import { UserRegistrationService } from '../../services/user-registration.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {

  registrationForm: RegistrationForm;
  registrationFormError: any;
  termsAndAgreementFlag: boolean;
  hasValidationError: boolean;
  termsAndAgreementMessage: string;

  constructor(private registrationService: UserRegistrationService,
    private router: Router) { 
    this.registrationForm = new RegistrationForm();
    this.registrationFormError = {};
    this.hasValidationError = false;
    this.termsAndAgreementFlag = false;
    this.termsAndAgreementMessage = "";
  }

  private initializeForm() {
    this.registrationFormError = {};
    this.hasValidationError = false;
    this.termsAndAgreementMessage = "";
    console.log('Form Initialized!');
  }

  public register() {
    this.initializeForm();
    let registrationResponse = this.registrationService.registerUser(this.registrationForm);

    if(!this.termsAndAgreementFlag) { 
      this.termsAndAgreementMessage = "Please check Terms and Agreement to Register";
      return;
    }

    registrationResponse.subscribe({
      next: data => {
        console.log('successful redirection');
        this.router.navigate(['home']);
      },
      error: error => {
        this.hasValidationError = true;
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

  public tickTermsAndAgreement() {
    // console.log('setting terms and agreement')
    this.termsAndAgreementFlag = !this.termsAndAgreementFlag;
  }
}
