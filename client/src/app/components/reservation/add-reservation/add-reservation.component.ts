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
  reservationRequest: ReservationRequest;

  constructor(private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private hospitalRoomService: HospitalRoomService,
    private ngbCalendar: NgbCalendar,
    private reservationService: ReservationService,
    private dateParser: NgbDateParserFormatter) {

    this.timeManager = new TimeManager();
    this.timeManager.filterTime();
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

  private getAvailableReservationTime() {
    let dateToday = this.ngbCalendar.getToday();
    // get first the available time for today
    this.getReservedTime(this.createDate(dateToday)).subscribe((data: string[]) => {
      let reservedTime = data;
      this.timeManager.setTime(reservedTime);
      this.timeListTracker = this.timeManager.getTime();

      //check for the available time for the next day
      const nextDay = this.ngbCalendar.getNext(dateToday);
      this.getReservedTime(this.createDate(nextDay)).subscribe((data: string[]) => {
        // make an instance of time maanger and set a time
        let reservedEndTime = data;
        const tempTimeManager = new TimeManager();
        tempTimeManager.setTime(reservedEndTime);

        //Check the possible time for the next day
        //this is for the endTime checking
        let availableTimeForEnd = [];
        for (let time of tempTimeManager.getTime()) {
          const previousTime = tempTimeManager.getPreviousTime(time.hour);
          if ((time.isReserved && !time.isStartTime) ||
            (time.isReserved && (previousTime && previousTime.isReserved))) { break; }

          availableTimeForEnd.push(time);
        }

        //check if there is still an available time for the next day
        //if only the available hour for the star time is 23
        //true move to the next day and use this function again
        //till we get a valid time
        //else filter the possible startTime
        let availableTimeForStart = this.filterStartTime();
        if(availableTimeForStart.length == 1 && availableTimeForEnd.length == 1) {
          const selectedStartTime = availableTimeForStart[0];
          const selectedEndTime = availableTimeForEnd[0];
          if(!selectedStartTime.minutes['30'] && selectedStartTime.isReserved &&
            !selectedEndTime.minutes['00']) {
              this.reservationForm.controls['reserveDate'].setValue(nextDay);
              this.reservationForm.controls['reserveEndDate'].setValue(nextDay);
              this.minDate = nextDay;
              this.getAvailableReservationTime();
          }
        }else {
          this.timeListTrackerStart = this.filterStartTime();
        }
      });
    });
  }

  ngOnInit(): void {
    const mockTimeManager = new TimeManager();
    mockTimeManager.mockSetTargetDate('2021-05-09');
    mockTimeManager.mockSetReservedTime(this.mockGetTime());
  }

  private mockGetTime() {
    return {
      data: [
        {
          startDate: '2021-05-09', 
          endDate: '2021-05-09',
          startTime: '05:00',
          endTime: '12:00'
        },
        {
          startDate: '2021-05-09', 
          endDate: '2021-05-10',
          startTime: '12:30',
          endTime: '08:00'
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

  public onStartDateSelection(startDate: NgbDate) {
    console.log('get reserve time on this date =>', startDate);
    //1. get the reserve time for this particular $date
    //2. filter all the available start time
    //3. assign the available dates to the timeListTracker

    const endDate = this.reservationForm.controls['reserveEndDate'].value;
    if (NgbDate.from(startDate).after(endDate)) {
      const startDate = this.reservationForm.controls['reserveDate'].value;
      this.reservationForm.controls['reserveEndDate'].setValue(startDate);
    }

    let dateToday = this.ngbCalendar.getToday();
    this.getReservedTime(this.createDate(dateToday)).subscribe((data: string[]) => {
      let reservedTime = data;
      this.timeManager.setTime(reservedTime);
      this.timeListTracker = this.timeManager.getTime();

      this.timeListTrackerStart = this.filterStartTime();
    });

    // reset btn
    // reset reservation time
    // reset reservation end time
    // should track dates start time can't be greater than end time
    this.resetTime();
    this.reservationForm.controls['reserveEndTimeHour'].disable();
    this.reservationForm.controls['reserveEndTimeMinutes'].disable();
  }

  private resetTime() {
    this.reservationForm.controls['reserveTimeHour'].setValue('HH')
    this.reservationForm.controls['reserveTimeMinutes'].setValue('MM');
    this.reservationForm.controls['reserveEndTimeHour'].setValue('HH')
    this.reservationForm.controls['reserveEndTimeMinutes'].setValue('MM');
  }

  public onEndDateSelection(endDate: NgbDate) {
    console.log('on end date selection!');
    //assuming endDate is avialable
    //1. get the reserve time for this particular $date
    //2. filter all the time without consecutive entry of reserve time

    let dateToday = this.ngbCalendar.getToday();

    if (!this.isDateEqual()) {
      this.getReservedTime(this.createDate(endDate)).subscribe((data: string[]) => {
        const reservedTime = data;
        this.timeEndManager = new TimeManager();
        this.timeEndManager.filterTime();
        this.timeEndManager.setTime(reservedTime);
        this.enableEndTimeFormInputs();

        let availableTimeForEnd = [];
        for (let time of this.timeEndManager.getTime()) {
          const previousTime = this.timeEndManager.getPreviousTime(time.hour);
          if ((time.isReserved && !time.isStartTime) ||
            (time.isReserved && (previousTime && previousTime.isReserved))) { break; }

          availableTimeForEnd.push(time);
        }
        this.timeListTrackerEnd = availableTimeForEnd;
        console.log('timeListTrackerEnd', this.timeListTrackerEnd);
        const enableEndDate = availableTimeForEnd.some(time => time.hour == '23' && !time.isReserved);
        if (!enableEndDate) {
          this.maxEndDate = this.reservationForm.controls['reserveEndDate'].value;
        } else {
          this.maxEndDate = {};
        }

        this.reservationForm.controls['reserveEndTimeHour'].setValue('HH');
        this.reservationForm.controls['reserveEndTimeMinutes'].setValue('MM');
      });


    } else {
      this.reservationTimeChange();
      this.maxEndDate = {};
    }
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

  private filterStartTime() {
    return this.timeListTracker.filter((time, i, arr) => {
      return !time.isReserved || (time.isReserved && !time.isStartTime && !time.inBetween);
    });
  }

  private getHospitalRoom() {
    const roomId = this.route.snapshot.paramMap.get('roomId');
    this.hospitalRoomService.getRoomById(Number(roomId)).subscribe(data => {
      console.log('getRoomById =>', data)
    });
  }

  private populateAvailableMinutes(currentTime: Time, nextTargetTime: Time) {
    if (currentTime.isReserved && !currentTime.isStartTime) {
      if (!currentTime.minutes['30']) {
        this.setFormReserveMinutes('30');
        this.setReserveMinutesArray(['30']);
        this.enableEndTimeFormInputs();
      } else {
        this.setReserveMinutesArray(['00', '30']);
      }
    } else {
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

    // possibleEndTime for the same date
    let availableTimeForEnd: Time[] = this.setPossibleEndTime(targetHour);
    this.checkEndDateAvailability(availableTimeForEnd);

    let exceedsCurrentDay = this.timeListTracker.some(time => {
      return time.hour == '23' && (!time.isReserved || (time.isReserved && !time.isStartTime));
    });


    const dateToday = this.reservationForm.controls['reserveDate'].value;
    if (targetHour == '23') {
      const nextDay = this.ngbCalendar.getNext(dateToday);
      this.disableEndDate = false;
      this.reservationForm.controls['reserveEndDate'].setValue(nextDay);

      if (targetHour == '23') {
        this.minEndDate = nextDay;
      } else {
        this.minEndDate = dateToday;
      }
      // possible end time for different dates
      this.onEndDateSelection(nextDay);

    } else {
      this.minEndDate = dateToday;
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
    if (this.isDateEqual()) {
      const reserveTimeMinutes = this.reservationForm.controls['reserveTimeMinutes'].value;
      if (difference == 1 && endTime.isReserved) {
        console.log('endTime.isReserved');
        this.setReserveEndMinutesArray([reserveTimeMinutes]);
      } else {

        if (difference == 1) {
          // this.setReserveEndMinutesArray([reserveTimeMinutes]);
          if (reserveTimeMinutes == '00') {
            this.setReserveEndMinutesArray(['00', '30']);
          } else {
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

    } else {
      if (endTime.isReserved && endTime.isStartTime) {
        if (endTime.minutes['30'] == Default.RESERVED) {
          this.setReserveEndMinutesArray(['30']);
          this.setFormReserveEndMinutes('30');
        } else {
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

    if (!this.isDateEqual()) {
      endTime = this.timeEndManager.getHourInTime(targetEndHour);
    }

    this.populateAvailableEndMinutes(difference, startTime, endTime);

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
