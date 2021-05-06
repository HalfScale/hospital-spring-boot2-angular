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

        reservedTime.forEach(val => {
            const splittedTime = val.split('-');
            const startTime = splittedTime[0];
            const endTime = splittedTime[1];

            this.timeList.forEach(val => {
                if(val.hour == startTime.split(':')[0]) {
                    val.isStartTime = true;
                    val.isReserved = true;
                    Object.keys(val.minutes).forEach(key => {
                        if(key == startTime.split(':')[1]) {
                            val.minutes[key] = false;
                        }
                    });
                }

                if(val.hour == endTime.split(':')[0]) {
                    Object.keys(val.minutes).forEach(key => {
                        if(key == endTime.split(':')[1]) {
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

    private getCurrentHour(): Number {
        return Number(moment().format('HH'));
    }
}