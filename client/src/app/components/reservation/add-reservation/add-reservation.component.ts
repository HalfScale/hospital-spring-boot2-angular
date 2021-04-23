import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { GlobalVariable } from 'src/app/globals';
import { HospitalRoomService } from 'src/app/services/hospital-room.service';
import {NgbCalendar, NgbDateParserFormatter, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import { ReservationService } from 'src/app/services/reservation.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-add-reservation',
  templateUrl: './add-reservation.component.html',
  styleUrls: ['./add-reservation.component.css']
})
export class AddReservationComponent implements OnInit {

  imagePath: any = GlobalVariable.DEFAULT_PROFILE_IMG;
  reservationForm: FormGroup;
  minDate: any;

  constructor(private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private hospitalRoomService: HospitalRoomService,
    private ngbCalendar: NgbCalendar,
    private reservationService: ReservationService,
    private dateParser: NgbDateParserFormatter) { 
    
    let dateToday = ngbCalendar.getToday();
    console.log('date today', dateToday);
    

    this.getAvailableReservationDate().subscribe(data => {
      this.minDate = dateParser.parse(data);
    });
    
    this.minDate = dateToday;
    this.reservationForm = this.formBuilder.group({
      hasAssociatedAppointment: [''],
      associatedAppointmentId: [''],
      reserveDate: [dateToday],
      reserveEndDate: [dateToday]
    });

    // this.getHospitalRoom();
  }

  private getAvailableReservationDate(): Observable<any> {
    return this.reservationService.getAvailableReservationDate();
  }

  private getHospitalRoom() {
    const roomId = this.route.snapshot.paramMap.get('roomId');
    this.hospitalRoomService.getRoomById(Number(roomId)).subscribe(data => {
      console.log('getRoomById =>', data)
    });
  }

  ngOnInit(): void {
  }

  public submit() {

  }

}
