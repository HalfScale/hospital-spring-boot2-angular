import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { UserDetail } from '../user-detail';
import { UserRegistrationService } from '../user-registration.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  user: User;

  constructor(private registrationService: UserRegistrationService) { 
    let userDetails = new UserDetail();
    this.user = new User();
  }

  ngOnInit(): void {
  }

  public register() {
    // async operation
    let registerResponse = this.registrationService.registerUser(this.user);
    registerResponse.subscribe((result) => console.log('registration response', result));
  }

}
