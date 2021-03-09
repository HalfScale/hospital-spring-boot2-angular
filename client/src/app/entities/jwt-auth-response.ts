export class JwtAuthResponse {
    authToken: string;
    email: string;
    refreshToken: string;
    expiresAt: Date;

    constructor() {
        this.authToken = "";
        this.email = "";
        this.refreshToken = "";
        this.expiresAt = new Date();
    }
}