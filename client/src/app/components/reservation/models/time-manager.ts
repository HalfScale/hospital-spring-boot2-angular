import * as moment from "moment";
import { Time } from "./time";

export class TimeManager {

    timeListInString: string[] = [
        '00', '01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11',
        '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23'
    ];
    timeList: Time[] = [];

    private targetDate: string;

    constructor() {
        this.timeListInString.forEach(val => {
            this.timeList.push(new Time(val));
        });
    }

    public mockSetReservedTime(result: any) {
        console.log('result data', result);
        result.data.forEach(data => {
            console.log('data', data);
            //1.traverse through the data
            //2.only set the time that is within the target date
            //3. get the start and end time
            const startHour = this.getHourInString(data.startTime);
            const startMinutes = this.getMinutesInString(data.startTime);
            const endHour = this.getHourInString(data.endTime);
            const endMinutes = this.getMinutesInString(data.endTime);

            const startTime = this.getHourInTime(startHour);
            startTime.isReserved = true;
            startTime.isStartTime = true;
            startTime.minutes[startMinutes].isStartTime = true;
            const endTime = this.getHourInTime(endHour);
            endTime.isReserved = true;
            endTime.minutes[endMinutes].isEndTime = true;
            console.log('startHour', startHour);
            console.log('endHour', endHour);

            if (data.startDate == this.targetDate && data.endDate == this.targetDate) {
                this.timeList.forEach((time, i) => {
                    //check if its a start_time, in between or end_time
                    //
                    if (i > this.findHourIndex(startHour) && i < this.findHourIndex(endHour)) {
                        time.inBetween = true;
                        time.isReserved = true;
                    }
                });
            }
        });

        // console.log('timeList', this.timeList);
        this.getAvailableTime();
        this.filterStartTime();
    }

    private getAvailableTime(): Time[] {
        //iterate through the array
        //filter the time that is not reserved, or reserved but is a start time,
        const result = this.timeList.filter(time => {
            return !time.isReserved || time.isReserved && time.isStartTime
        });
        console.log('getAvailableTime result', result);
        return result;
    }

    private filterStartTime() {
        //if hour is reserved
        //check the available minutes
        //if any of the mins is labeled as end time, then display
        //it as available start time
        const reuslt = this.getAvailableTime().filter(time => {
            const minsFlag = Object.keys(time.minutes).some(key => time.minutes[key].isEndTime);
            return !time.isReserved || (time.isReserved && !time.isStartTime) ||
                time.isReserved && minsFlag;
        });
        console.log('filterStartTime result', reuslt);
    }

    private getHourInString(timeString: string) {
        return timeString.split(':')[0];
    }

    private getMinutesInString(timeString: string) {
        return timeString.split(':')[1];
    }

    public mockSetTargetDate(date: string) {
        this.targetDate = date;
    }

    public getHourInTime(hour: string): Time {
        return this.timeList.find(time => time.hour == hour);
    }

    public setTime(reservedTime: string[]) {

        reservedTime.forEach((val) => {
            const splittedTime = val.split('-');
            const startTime = splittedTime[0];
            const startTimeHourPart = startTime.split(':')[0];
            const startTimeMinutePart = startTime.split(':')[1];
            const endTime = splittedTime[1];
            const endTimeHourPart = endTime.split(':')[0];
            const endTimeMinutePart = endTime.split(':')[1];

            // traverse and get the first and last index
            // traverse and get the in between
            this.timeList.forEach((val, i) => {
                if (val.hour == startTimeHourPart) {
                    val.isStartTime = true;
                    val.isReserved = true;
                    Object.keys(val.minutes).forEach(key => {
                        if (key == startTimeMinutePart) {
                            val.minutes[key] = false;
                        }
                    });
                }

                const startIndex = this.findHourIndex(startTimeHourPart);
                const endIndex = this.findHourIndex(endTimeHourPart);
                if (i > startIndex && i < endIndex) {
                    val.isReserved = true;
                    val.inBetween = true;
                }

                if (val.hour == endTimeHourPart) {
                    Object.keys(val.minutes).forEach(key => {
                        if (key == endTimeMinutePart) {
                            val.minutes[key] = false;
                            val.isReserved = true;
                        }
                    });
                }
            });
        });
    }

    public getTime() {
        return this.timeList;
    }

    public getNextTime(hour: string) {
        const targetTimeIndex = this.timeList.findIndex(val => {
            return val.hour == hour;
        });

        return this.timeList[targetTimeIndex + 1];
    }

    public getPreviousTime(hour: string) {
        const targetTimeIndex = this.timeList.findIndex(val => {
            return val.hour == hour;
        });

        return this.timeList[targetTimeIndex - 1];
    }

    public getAvailableMins(hour: string) {
        const time = this.timeList.find(time => {
            return hour == time.hour;
        });

        return time.minutes;
    }

    public filterTime() {
        this.timeList = this.timeList.filter(time => {
            const currentHour = moment().format('HH');
            return time.getHourInInteger() > this.getCurrentHour();
        });
    }

    public findHourIndex(hour: string) {
        return this.timeList.findIndex(val => val.hour == hour);
    }

    private getCurrentHour(): Number {
        return Number(moment().format('HH'));
    }
}