import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { ConfirmRegistrationComponent } from './components/confirm-registration/confirm-registration.component';
import {NgxWebstorageModule} from 'ngx-webstorage';
import { NgxPaginationModule } from 'ngx-pagination';
import { HospitalRoomComponent } from './components/hospital-room/hospital-room.component';
import { ErrorPageComponent } from './components/error-page/error-page.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { HeaderComponent } from './components/header/header.component';
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password.component';
import { UpdateForgotPasswordComponent } from './components/update-forgot-password/update-forgot-password.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ReservationComponent } from './components/reservation/reservation.component';
import { ForgotPasswordSuccessComponent } from './components/forgot-password-success/forgot-password-success.component';


@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    LoginComponent,
    HomeComponent,
    ConfirmRegistrationComponent,
    HospitalRoomComponent,
    ErrorPageComponent,
    HeaderComponent,
    ForgotPasswordComponent,
    UpdateForgotPasswordComponent,
    ReservationComponent,
    ForgotPasswordSuccessComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    NgxWebstorageModule.forRoot(),
    NgxPaginationModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    NgbModule
  ],
  providers: [
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
