import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable, throwError } from "rxjs";
import { catchError, map, switchMap } from "rxjs/operators";
import { JwtAuthResponse } from "../entities/jwt-auth-response";
import { AuthService } from "../services/auth.service";

@Injectable({
    providedIn: 'root'
})
export class TokenInterceptor implements HttpInterceptor{

    isTokenRefreshing = false;
    refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject(null);

    constructor(public authService: AuthService) {
        
    }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        const jwtToken = this.authService.getJwtToken();
        if(jwtToken) {
            this.addToken(req, jwtToken);
        }

        return next.handle(req).pipe(catchError(error => {
            if(error instanceof HttpErrorResponse && error.status == 403) {
                return this.handleAuthErrors(req, next); // Will perform another call for refreshing tokens
            }else {
                return throwError(error);
            }
        }))
    }

    // error handling to request a call again in the back end
    private handleAuthErrors(req: HttpRequest<any>, next: HttpHandler)
        : Observable<HttpEvent<any>> {

            if(!this.isTokenRefreshing) {
                this.isTokenRefreshing = true;
                this.refreshTokenSubject.next(null);

                return this.authService.refreshToken()
                    .pipe(map(response => response),
                    switchMap((response) => {
                        this.isTokenRefreshing = false;
                        this.refreshTokenSubject.next(response.authToken);

                        return next.handle(this.addToken(req, response.authToken));
                    }));
            }
            
        return new Observable();
    }

    addToken(req: HttpRequest<any>, jwtToken: any) {
        return req.clone({
            headers: req.headers.set('Authorization', 'Bearer ' + jwtToken)
        });
    }
}