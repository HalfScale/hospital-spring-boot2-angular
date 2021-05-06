export class Time {
    hour: string;
    minutes: any = {
        '00': true,
        '30': true
    }

    isReserved: boolean = false;
    isStartTime: boolean = false;

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