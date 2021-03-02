import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ConfirmRegistrationService } from 'src/app/services/confirm-registration.service';

@Component({
  selector: 'app-confirm-registration',
  templateUrl: './confirm-registration.component.html',
  styleUrls: ['./confirm-registration.component.css']
})
export class ConfirmRegistrationComponent implements OnInit {

  token: string;

  constructor(private route: ActivatedRoute, 
    private confirmRegistrationService: ConfirmRegistrationService,
    private router: Router) { 
      this.token = '';
    }

  ngOnInit(): void {
    
    this.route.url.subscribe(segments => {
      segments.forEach((segment, i) => {
        if(i > 1) {
          this.token += segment.path + '/'
        }
      });
    });
    // const routeParams = this.route.snapshot.paramMap;
    // const token = routeParams.get('token');

    // console.log('tokenFromRoute', token);
    // let confirmationResponse = this.confirmRegistrationService.validateToken(token);

    // confirmationResponse.subscribe({
    //   next: data => {
    //     console.log('success page');
    //   },
    //   error: error => {
    //     this.router.navigate(['404-page-not-found']);
    //   }
    // });
  }

}
