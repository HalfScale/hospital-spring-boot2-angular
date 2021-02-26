import { Component, OnInit } from '@angular/core';
import { LoginForm } from 'src/app/entities/login-form';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  loginForm: LoginForm;
  loginFormError: any;

  constructor(private loginService: LoginService) { 
    this.loginForm = new LoginForm();
    this.loginFormError = {};
  }

  private initializeForm() {
    this.loginFormError = {};
  }

  public login() {
    this.initializeForm();
    console.log('login data', this.loginForm);
    let loginResponse = this.loginService.loginUser(this.loginForm);

    loginResponse.subscribe({
      next: data => {
        if(data) {
          console.log('success login', data);
        }else {
          console.log('error login', data);
        }
      },
      error: error => {
        console.log('error response', error);
        const errorData = error.error.data;
        Object.keys(errorData).forEach(key => {
          this.loginFormError[key] = errorData[key];
        });
      }
    });
  }

}
