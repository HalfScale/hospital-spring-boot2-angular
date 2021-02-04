// import {
//     HttpEvent,
//     HttpInterceptor,
//     HttpHandler,
//     HttpRequest,
//     HttpErrorResponse
// } from '@angular/common/http';
// import { Observable, throwError } from 'rxjs';
// import { retry, catchError } from 'rxjs/operators';

// export class HttpErrorInterceptor implements HttpInterceptor {
//     intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
//         return next.handle(request)
//             .pipe(
//                 retry(1),
//                 catchError((error: HttpErrorResponse) => {
//                     let errorMessage = '';
//                     console.log('error response', error);
//                     if (error.error instanceof ErrorEvent) {
//                         // client-side error
//                         errorMessage = `Error: ${error.error.message}`;
//                     } else {
//                         // server-side error
//                         errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
//                     }
//                     window.alert(errorMessage);
//                     return throwError(errorMessage);
//                 })
//             )
//     }
// }