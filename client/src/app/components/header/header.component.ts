import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  isLoggedIn: boolean;
  username: string;
  role: number;

  constructor(private authService: AuthService, private router: Router) { 
    this.isLoggedIn = false;
    this.username = '';
    this.role = 0;
  }

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
    this.username = this.authService.getName();
    this.role = this.authService.getRole();
  }

  onClick() {
    this.router.navigateByUrl('users/info');
  }

  logout() {
    this.authService.logout();
    this.router.navigateByUrl('').then(() => {
      window.location.reload();
    });
  }

}
