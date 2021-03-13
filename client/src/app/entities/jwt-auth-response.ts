export class JwtAuthResponse {
    authToken: string;
    email: string;
    refreshToken: string;
    expiresAt: Date;
    role: number;
    name: string;

    constructor() {
        this.authToken = "";
        this.email = "";
        this.refreshToken = "";
        this.expiresAt = new Date();
        this.role = 0;
        this.name = "";
    }
}