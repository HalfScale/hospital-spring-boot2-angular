import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LoginForm } from 'src/app/entities/login-form';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  loginForm: LoginForm;
  loginFormError: any;
  hasValidationError: boolean;

  constructor(private authService: AuthService,
    private toastr: ToastrService,
    private router: Router) { 
    this.loginForm = new LoginForm();
    this.loginFormError = {};
    this.hasValidationError = false;
  }

  private initializeForm() {
    this.loginFormError = {};
    this.hasValidationError = false;
  }

  public login() {
    this.initializeForm();
    console.log('login data', this.loginForm);
    let loginResponse = this.authService.loginUser(this.loginForm);

    loginResponse.subscribe({
      next: data => {
        if(data) {
          this.router.navigateByUrl('/');
          this.toastr.success('Login Successful!');
        }else {
          console.log('error login', data);
        }
      },
      error: error => {
        console.log('error response', error);
        this.hasValidationError = true;
        const errorData = error.error.data;
        Object.keys(errorData).forEach(key => {
          this.loginFormError[key] = errorData[key];
        });
      }
    });
  }

}
