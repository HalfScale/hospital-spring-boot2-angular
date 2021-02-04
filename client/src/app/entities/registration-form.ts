export class RegistrationForm{
    
    firstName: string;
    lastName: string;
    email: string;
    mobileNo: string;
    gender: string;
    password: string;
    confirmPassword: string;
    hospitalCode: string;

    constructor() {
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.mobileNo = "";
        this.password = "";
        this.confirmPassword = "";
        this.hospitalCode = "";
        this.gender = "1";
    }
}