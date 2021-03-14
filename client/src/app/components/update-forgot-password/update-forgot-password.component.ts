import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-update-forgot-password',
  templateUrl: './update-forgot-password.component.html',
  styleUrls: ['./update-forgot-password.component.css']
})
export class UpdateForgotPasswordComponent implements OnInit {

  requestPayload: any;
  formError: any;

  constructor(private route: ActivatedRoute,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService) {
    this.requestPayload = {
      resetToken: null,
      password: null,
      confirmPassword: null
    };

    this.formError = {};
  }

  ngOnInit(): void {
  }

  private initialize() {
    this.formError = {};
  }

  public submit(): void {
    this.initialize();

    const token = this.route.snapshot.paramMap.get('token');
    this.requestPayload.resetToken = token;

    console.log('POST request payload', this.requestPayload);
    this.authService.updatePassword(this.requestPayload).subscribe({
      next: data => {
        this.router.navigateByUrl('users/login');
        this.toastr.success('Password reset success!');
      },
      error: error => {
        console.log('error status', error);
        const errorData = error.error ? error.error.data : null;
        if (errorData && error.status == 400) {
          Object.keys(errorData).forEach(key => {
            this.formError[key] = errorData[key];
          });
        } else {
          this.router.navigateByUrl('token-notfound');
        }
      }
    });
  }

}
