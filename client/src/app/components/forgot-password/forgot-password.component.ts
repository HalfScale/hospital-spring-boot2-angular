import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
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
    private router: Router) { 
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
        this.router.navigateByUrl('password/reset/success');
      },
      error => {
        this.isValid = false;
      }
    );
  }

}
