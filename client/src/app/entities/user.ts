import { UserDetail } from "./user-detail";

export class User {

    email: string;
    password: string;
    userDetail: UserDetail;
    hospitalCode: string;

    constructor() {
        this.email = "";
        this.password = "";
        this.hospitalCode = "";
        this.userDetail = new UserDetail();
    }
}