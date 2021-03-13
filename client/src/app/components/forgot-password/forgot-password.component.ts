import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {

  email: string;
  isValid: boolean;

  constructor(private authService: AuthService,
    private toastr: ToastrService) { 
    this.email = '';
    this.isValid = true;
  }

  ngOnInit(): void {

  }

  public submit(): void {
    this.authService.sendResetPasswordNotif(this.email).subscribe(
      data => {
        this.isValid = true;
        this.email = '';
        this.toastr.success('Reset password link sent to your email.');
      },
      error => {
        this.isValid = false;
      }
    );
  }

}
