export class JwtAuthResponse {
    authToken: string;
    email: string;

    constructor() {
        this.authToken = "";
        this.email = "";
    }
}