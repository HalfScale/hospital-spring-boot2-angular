import * as moment from "moment";
import { Time } from "./time";

export class TimeManager {

    timeListInString: string[] = [
        '00', '01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', 
        '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23'
    ];

    timeList: Time[] = [];

    constructor() {
        this.timeListInString.forEach(val => {
            this.timeList.push(new Time(val));
        });
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
                if(val.hour == startTimeHourPart) {
                    val.isStartTime = true;
                    val.isReserved = true;
                    Object.keys(val.minutes).forEach(key => {
                        if(key == startTimeMinutePart) {
                            val.minutes[key] = false;
                        }
                    });
                }

                const startIndex = this.findHourIndex(startTimeHourPart);
                const endIndex = this.findHourIndex(endTimeHourPart);
                if(i > startIndex && i < endIndex) {
                    console.log('time in between', this.timeList[i]);
                    val.isReserved = true;
                    val.inBetween = true;
                }

                if(val.hour == endTimeHourPart) {
                    Object.keys(val.minutes).forEach(key => {
                        if(key == endTimeMinutePart) {
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