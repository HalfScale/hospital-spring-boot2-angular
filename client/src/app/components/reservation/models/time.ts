export class Time {
    hour: string;
    minutes: any = {
        '00': {
            isStartTime: false,
            isInBetween: false,
            isEndTime: false
        },
        '30':{
            isStartTime: false,
            isInBetween: false,
            isEndTime: false
        }
    }

    isReserved: boolean = false;
    isStartTime: boolean = false;
    inBetween: boolean = false;

    constructor(hour: string) {
        this.hour = hour;
        
    }

    public getHourInInteger(): number {
        return Number(this.hour);
    }

    public getMinsInInteger(): number {
        return Number(this.minutes)
    }
}