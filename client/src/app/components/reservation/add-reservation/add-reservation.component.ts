import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { GlobalVariable } from 'src/app/globals';
import { HospitalRoomService } from 'src/app/services/hospital-room.service';
import { NgbCalendar, NgbDate, NgbDateParserFormatter, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { ReservationService } from 'src/app/services/reservation.service';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { TimeManager } from '../models/time-manager';
import { Time } from '../models/time';
import { NgbDateStructAdapter } from '@ng-bootstrap/ng-bootstrap/datepicker/adapters/ngb-date-adapter';
import { ReservationRequest } from '../models/reservation-request';

const Default = Object.freeze({
  HOUR: 'HH',
  MINUTES: 'MM',
  RESERVED: false,
  AVAILABLE: true
});

@Component({
  selector: 'app-add-reservation',
  templateUrl: './add-reservation.component.html',
  styleUrls: ['./add-reservation.component.css']
})
export class AddReservationComponent implements OnInit {

  timeListTrackerFromUnequalDates: Time[];
  timeListTracker: Time[];
  timeListTrackerStart: Time[];
  timeListTrackerEnd: Time[];
  reserveTimeAvailableMins: string[] = ['00', '30'];
  reserveEndTimeAvailableMins: string[] = ['00', '30'];
  timeManager: TimeManager;
  timeEndManager: TimeManager;
  disableEndDate = true;
  maxEndDate: {};
  minEndDate: {};

  imagePath: any = GlobalVariable.DEFAULT_PROFILE_IMG;
  reservationForm: FormGroup;
  minDate: any;
  reservationRequest: ReservationRequest;

  constructor(private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private hospitalRoomService: HospitalRoomService,
    private ngbCalendar: NgbCalendar,
    private reservationService: ReservationService,
    private dateParser: NgbDateParserFormatter) {

    this.timeManager = new TimeManager();
    let dateToday = ngbCalendar.getToday();

    this.minDate = dateToday;
    this.reservationForm = this.formBuilder.group({
      hasAssociatedAppointment: [''],
      associatedAppointmentId: [''],
      reserveDate: [dateToday],
      reserveEndDate: [dateToday],
      reserveTimeHour: ['HH'],
      reserveTimeMinutes: ['MM'],
      reserveEndTimeHour: [{
        value: 'HH',
        disabled: true
      }],
      reserveEndTimeMinutes: [{
        value: 'MM',
        disabled: true
      }]
    });
  }

  ngOnInit(): void {
    this.timeManager.mockSetTargetDate('2021-09-27');
    this.timeManager.mockSetReservedTime(this.mockGetTime());
    this.timeListTrackerStart = this.timeManager.filterStartTime();
    console.log('available reservation time =>', this.timeListTrackerStart);
    console.log('unfiltered time =>', this.timeManager.getUnfilteredTime());
    
  }

  private mockGetTime() {
    return {
      data: [
        {
          startDate: '2021-09-27', 
          endDate: '2021-09-27',
          startTime: '10:00',
          endTime: '12:00'
        },
        {
          startDate: '2021-09-27', 
          endDate: '2021-09-27',
          startTime: '13:00',
          endTime: '17:00'
        }
      ]
    }
  }

  private createDate(date: NgbDate) {
    console.log('createDate', date);
    let year = String(date.year);
    let month = this.prefixAppender(String(date.month));
    let day = this.prefixAppender(String(date.day));
    return `${year}-${month}-${day}`;
  }

  private prefixAppender(data: string) {
    return data.length < 2 ? `0${data}` : data;
  }

  private getAvailableReservationDate(): Observable<any> {
    return this.reservationService.getAvailableReservationDate();
  }

  private getReservedTime(date: string): Observable<any> {
    return this.reservationService.getReservedTime(date);
  }

  private getStartTimeHourFromForm() {
    return this.reservationForm.controls['reserveTimeHour'].value;
  }

  public onStartDateSelection(startDate: NgbDate) {
    //1. get the reserve time for this particular $date
    //2. filter all the available start time
    //3. assign the available dates to the timeListTracker
  }

  private resetTime() {
    this.reservationForm.controls['reserveTimeHour'].setValue('HH')
    this.reservationForm.controls['reserveTimeMinutes'].setValue('MM');
    this.reservationForm.controls['reserveEndTimeHour'].setValue('HH')
    this.reservationForm.controls['reserveEndTimeMinutes'].setValue('MM');
  }

  public onEndDateSelection(endDate: NgbDate) {
    //assuming endDate is avialable
    //1. get the reserve time for this particular $date
    //2. filter all the time without consecutive entry of reserve time
  }

  private isDateEqual(): boolean {
    let reserveDate = this.reservationForm.controls['reserveDate'].value;
    let reserveEndDate = this.reservationForm.controls['reserveEndDate'].value;
    return NgbDate.from(reserveDate).equals(reserveEndDate);
  }

  private isStartDateBeforeEndDate() {
    let reserveDate = this.reservationForm.controls['reserveDate'].value;
    let reserveEndDate = this.reservationForm.controls['reserveEndDate'].value;
    return NgbDate.from(reserveDate).before(reserveEndDate);
  }

  private getHospitalRoom() {
    const roomId = this.route.snapshot.paramMap.get('roomId');
    this.hospitalRoomService.getRoomById(Number(roomId)).subscribe(data => {
      console.log('getRoomById =>', data)
    });
  }

  public reservationTimeChange() {
    console.log('reservation hour change =>');
    // reset the start_time_mins field
    // populate the start_time mins with available mins based on the start_time hour
    const hour = this.getStartTimeHourFromForm();
    console.log('reservationTimeChange => hour', hour);
    const availMins = this.timeManager.getAvailableMins(hour);
    console.log('reservationTimeChange => availMins', availMins);
    this.reservationForm.controls['reserveTimeMinutes'].setValue('MM');
    this.reserveTimeAvailableMins = availMins;
    this.reservationForm.get('reserveEndTimeHour').enable();

    console.log("selected start hour", this.reservationForm.controls['reserveTimeHour'].value);

    // upon selected of start time or hour, get unfiltered time
    // filter through the time, and only return back that is
    // exclude the current selected start hour, if the next
    // time into the iteration is a start time end the loop
    const selectedStartHour = this.reservationForm.controls['reserveTimeHour'].value;
    const selectedStartHourIndex = this.timeManager.findHourIndex(selectedStartHour);
    const validEndTime = [];

    // exclude the selected start time
    const slicedListForEndTime = this.timeManager.getUnfilteredTime().slice(selectedStartHourIndex + 1);

    slicedListForEndTime.some((time: Time, index) => {
      if(!time.isStartTime) {
        validEndTime.push(time);
        return false;
      }

      return true;
    });

    console.log('result for available end time =>', validEndTime);
    this.timeListTrackerEnd = validEndTime;
  }

  public reservationTimeMinsChange() {
    console.log('reservation mins change =>');
    
  }

  public reservationEndTimeChange() {
    
  }

  private createTime(hours: string, mins: string) {
    return `${hours}:${mins}`
  }

  public submit() {
    this.reservationRequest = new ReservationRequest();
    const startTime = this.createTime(this.reservationForm.controls['reserveTimeHour'].value, this.reservationForm.controls['reserveTimeMinutes'].value);
    const endTime = this.createTime(this.reservationForm.controls['reserveEndTimeHour'].value, this.reservationForm.controls['reserveEndTimeMinutes'].value);
    this.reservationRequest.associatedAppointmentId = this.reservationForm.controls['associatedAppointmentId'].value;
    this.reservationRequest.reservedDate = this.createDate(this.reservationForm.controls['reserveDate'].value);
    this.reservationRequest.reservedTime = startTime;
    this.reservationRequest.reservedEndDate = this.createDate(this.reservationForm.controls['reserveEndDate'].value);
    this.reservationRequest.reservedEndTime = endTime;
    console.log('data before submit', this.reservationRequest);
  }

}
