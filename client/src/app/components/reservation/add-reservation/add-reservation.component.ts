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
  reserveTimeAvailableMins: string[] = ['00', '30'];
  timeListTrackerEnd: Time[];
  reserveEndTimeAvailableMins: string[] = ['00', '30'];
  timeManager: TimeManager;
  timeEndManager: TimeManager;
  disableEndDate = true;
  maxEndDate: {};
  minEndDate: {};

  imagePath: any = GlobalVariable.DEFAULT_PROFILE_IMG;
  reservationForm: FormGroup;
  minDate: any;

  constructor(private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private hospitalRoomService: HospitalRoomService,
    private ngbCalendar: NgbCalendar,
    private reservationService: ReservationService,
    private dateParser: NgbDateParserFormatter) {

    this.timeManager = new TimeManager();

    let dateToday = ngbCalendar.getToday();
    console.log('ngbCalendar', this.ngbCalendar.getNext(dateToday));


    // this.getAvailableReservationDate().subscribe(data => {
    //   this.minDate = dateParser.parse(data);
    // });

    // this.getReservedTime(this.createDate(dateToday)).subscribe((data: string[]) => {
    // console.log('getReservedTime =>', data);
    let reservedTime = ['14:00-15:00', '15:30-16:30'];
    console.log('reservedTime', reservedTime);
    this.timeManager.setTime(reservedTime);
    // this.timeManager.filterTime();
    this.timeListTracker = this.timeManager.getTime();
    console.log('timeManager', this.timeManager.getTime());
    // });

    // filtering the data for possible start_time
    this.timeListTrackerStart = this.filterStartTime();

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

  public onStartDateSelection(date: NgbDate) {
    console.log('get reserve time on this date =>', date);
    //1. get the reserve time for this particular $date
    //2. filter all the available start time
    //3. assign the available dates to the timeListTracker
  }

  private isDateEqual(): boolean {
    let reserveDate = this.reservationForm.controls['reserveDate'].value;
    let reserveEndDate = this.reservationForm.controls['reserveEndDate'].value;
    return NgbDate.from(reserveDate).equals(reserveEndDate);
  }

  public onEndDateSelection(date: NgbDate) {
    console.log('on end date selection!');
    //assuming endDate is avialable
    //1. get the reserve time for this particular $date
    //2. filter all the time without consecutive entry of reserve time

    if (!this.isDateEqual()) {
      this.timeEndManager = new TimeManager();
      this.timeEndManager.setTime([]);
      this.enableEndTimeFormInputs();

      let availableTimeForEnd = [];
      for (let time of this.timeEndManager.getTime()) {
        const previousTime = this.timeEndManager.getPreviousTime(time.hour);
        if ((time.isReserved && !time.isStartTime) ||
          (time.isReserved && previousTime.isReserved)) { break; }

        availableTimeForEnd.push(time);
      }

      this.timeListTrackerEnd = availableTimeForEnd;
      const enableEndDate = availableTimeForEnd.some(time => time.hour == '23' && !time.isReserved);
      console.log('timeListTrackerEnd', this.timeListTrackerEnd);
      if (!enableEndDate) {
        this.maxEndDate = this.reservationForm.controls['reserveEndDate'].value;
      } else {
        this.maxEndDate = {};
      }

      this.reservationForm.controls['reserveEndTimeHour'].setValue('HH');
      this.reservationForm.controls['reserveEndTimeMinutes'].setValue('MM');
    }else {
      this.reservationTimeChange();
      this.maxEndDate = {};
    }
  }

  private filterStartTime() {
    return this.timeListTracker.filter((time, i, arr) => {
      return !time.isReserved || (time.isReserved && !time.isStartTime);
    });
  }

  private getHospitalRoom() {
    const roomId = this.route.snapshot.paramMap.get('roomId');
    this.hospitalRoomService.getRoomById(Number(roomId)).subscribe(data => {
      console.log('getRoomById =>', data)
    });
  }

  private populateAvailableMinutes(currentTime: Time, nextTargetTime: Time) {
    if(currentTime.isReserved && !currentTime.isStartTime) {
      if(!currentTime.minutes['30']) {
        this.setFormReserveMinutes('30');
        this.setReserveMinutesArray(['30']);
        this.enableEndTimeFormInputs();
      }else {
        this.setReserveMinutesArray(['00', '30']);
      }
    }else {
      this.setReserveMinutesArray(['00', '30']);
    }

    if (nextTargetTime && (nextTargetTime.isStartTime && nextTargetTime.isReserved)) {
      if (!nextTargetTime.minutes['00']) {
        this.setReserveMinutesArray(['00']);
        this.setFormReserveMinutes('00');
        this.enableEndTimeFormInputs();
      } else {
        this.setReserveMinutesArray(['30']);
        this.setFormReserveMinutes('30');
      }
    }
  }

  private setFormReserveMinutes(mins: string) {
    this.reservationForm.controls['reserveTimeMinutes'].setValue(mins);
  }

  private setReserveMinutesArray(minutesArr: string[]) {
    this.reserveTimeAvailableMins = minutesArr;
  }

  private enableEndTimeFormInputs() {
    this.reservationForm.controls['reserveEndTimeHour'].enable();
    this.reservationForm.controls['reserveEndTimeMinutes'].enable();
  }

  private findIndexFromTimeList(timeList: Time[], targetHour: string) {
    return timeList.findIndex((val) => {
      return val.getHourInInteger() == Number(targetHour);
    });
  }

  private setPossibleEndTime(targetHour: string): Time[] {

    let availableForEndTime: Time[] = [];
    let timeToRemoveIndex = -1;
    let tempArray = [];

    timeToRemoveIndex = this.findIndexFromTimeList(this.timeListTracker, targetHour);
    tempArray = this.timeListTracker.slice(timeToRemoveIndex + 1, this.timeListTracker.length);

    for (let time of tempArray) {
      const previousTime = this.timeManager.getPreviousTime(time.hour);
      if ((time.isReserved && !time.isStartTime) ||
        (time.isReserved && previousTime.isReserved)) { break; }

      const target = Number(targetHour);
      if (time.getHourInInteger() > target) {
        availableForEndTime.push(time);
      }
    }

    return availableForEndTime;
  }

  private checkEndDateAvailability(availableEndTime: Time[]) {
    const enableEndDate = availableEndTime.some(time => time.hour == '23' && !time.isReserved);
    if (enableEndDate) {
      this.disableEndDate = false;
    } else {
      let dateToday = this.ngbCalendar.getToday();
      this.reservationForm.controls['reserveEndDate'].setValue(dateToday);
      this.disableEndDate = true;
    }
  }

  public reservationTimeChange() {
    const targetHour = this.reservationForm.controls['reserveTimeHour'].value;
    const targetMins = this.reservationForm.controls['reserveTimeMinutes'].value;

    const targetHourInTime = this.timeManager.getHourInTime(targetHour);
    const nextTargetHourInTime = this.timeManager.getNextTime(targetHour);

    this.populateAvailableMinutes(targetHourInTime, nextTargetHourInTime);

    if (targetHour !== Default.HOUR && targetMins !== Default.MINUTES) {
      this.enableEndTimeFormInputs();
    }

    let availableTimeForEnd: Time[] = this.setPossibleEndTime(targetHour);
    this.checkEndDateAvailability(availableTimeForEnd);

    let exceedsCurrentDay = this.timeListTracker.some(time => {
      return time.hour == '23' && (!time.isReserved || (time.isReserved && !time.isStartTime));
    });

    const dateToday = this.reservationForm.controls['reserveDate'].value;
    if(targetHour == '23') {
      const nextDay = this.ngbCalendar.getNext(dateToday);
      this.disableEndDate = false;
      this.reservationForm.controls['reserveEndDate'].setValue(nextDay);

      if(targetHour == '23') {
        this.minDate = nextDay;
      }else {
        this.minDate = dateToday;
      }
      this.onEndDateSelection(nextDay);

    }else {
      this.minDate = dateToday;
      this.reservationForm.controls['reserveEndDate'].setValue(dateToday);
      this.timeListTrackerEnd = availableTimeForEnd;
    }


    // force user to pick end time when start time changes
    // set endtime programatically to default. 
    this.reservationForm.controls['reserveEndTimeHour'].setValue('HH');
    this.reservationForm.controls['reserveEndTimeMinutes'].setValue('MM');
  }

  private populateAvailableEndMinutes(difference: number, startTime: Time, endTime: Time) {
    //the population of available mins differ when dates are not equal.
    if(this.isDateEqual()) {
      const reserveTimeMinutes = this.reservationForm.controls['reserveTimeMinutes'].value;
      if (difference == 1 && endTime.isReserved) {
        console.log('endTime.isReserved');
        this.setReserveEndMinutesArray([reserveTimeMinutes]);
      } else {
  
        if (difference == 1) {
          // this.setReserveEndMinutesArray([reserveTimeMinutes]);
          if(reserveTimeMinutes == '00') {
            this.setReserveEndMinutesArray(['00', '30']);
          }else {
            this.setReserveEndMinutesArray(['30']);
            this.setFormReserveEndMinutes('30');
          }
        } else {
          this.setReserveEndMinutesArray(['00', '30']);
          
          if (endTime.isReserved && endTime.isStartTime) {
            if (endTime.minutes['30'] == Default.RESERVED) {
              this.setReserveEndMinutesArray(['30']);
              this.setFormReserveEndMinutes('30');
            } else {
              this.setReserveEndMinutesArray(['00']);
              this.setFormReserveEndMinutes('00');
            }
          }
        }
      }
      //if equal to an hour
      //if equal greater than one hour.
      if (startTime.isReserved) {
        if (startTime.minutes['30'] == Default.RESERVED && difference == 1) {
          this.setReserveEndMinutesArray(['30']);
          this.setFormReserveEndMinutes('30');
        } else {
          this.setReserveEndMinutesArray(['00', '30']);
        }
      }

    }else {
      if(endTime.isReserved && endTime.isStartTime) {
        if(endTime.minutes['30'] == Default.RESERVED) {
          this.setReserveEndMinutesArray(['30']);
          this.setFormReserveEndMinutes('30');
        }else {
          this.setReserveEndMinutesArray(['00']);
          this.setFormReserveEndMinutes('00');
        }
      }
      this.setReserveEndMinutesArray(['00', '30']);
    }

  }
  
  private setFormReserveEndMinutes(mins: string) {
    this.reservationForm.controls['reserveEndTimeMinutes'].setValue(mins);
  }

  private setReserveEndMinutesArray(minutesArr: string[]) {
    this.reserveEndTimeAvailableMins = minutesArr;
  }

  public reservationEndTimeChange() {
    const targetStartHour = this.reservationForm.controls['reserveTimeHour'].value;
    const targetEndHour = this.reservationForm.controls['reserveEndTimeHour'].value;

    let startTime = this.timeManager.getHourInTime(targetStartHour);
    let endTime = this.timeManager.getHourInTime(targetEndHour);
    let difference = endTime.getHourInInteger() - startTime.getHourInInteger();
    
    if(!this.isDateEqual()) {
      endTime = this.timeEndManager.getHourInTime(targetEndHour);
    }

    this.populateAvailableEndMinutes(difference, startTime, endTime);

  }

  public submit() {

  }

}
