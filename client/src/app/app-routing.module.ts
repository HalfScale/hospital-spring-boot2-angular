import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ConfirmRegistrationComponent } from './components/confirm-registration/confirm-registration.component';
import { ErrorPageComponent } from './components/error-page/error-page.component';
import { ForgotPasswordSuccessComponent } from './components/forgot-password-success/forgot-password-success.component';
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password.component';
import { HomeComponent } from './components/home/home.component';
import { AddEditRoomComponent } from './components/hospital-room/add-edit-room/add-edit-room.component';
import { DetailsRoomComponent } from './components/hospital-room/details-room/details-room.component';
import { EditRoomComponent } from './components/hospital-room/edit-room/edit-room.component';
import { HospitalRoomComponent } from './components/hospital-room/hospital-room.component';
import { ViewEditRoomComponent } from './components/hospital-room/view-edit-room/view-edit-room.component';
import { ViewRoomComponent } from './components/hospital-room/view-room/view-room.component';
import { LoginComponent } from './components/login/login.component';
import { ProfileComponent } from './components/profile/profile.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { AddReservationComponent } from './components/reservation/add-reservation/add-reservation.component';
import { ReservationComponent } from './components/reservation/reservation.component';
import { UpdateForgotPasswordComponent } from './components/update-forgot-password/update-forgot-password.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'home', redirectTo: '/', pathMatch: 'full'},
  {path: 'registration', component: RegistrationComponent},
  {
    path: 'registration/confirm', 
    children: [
      {
        path: "**", component: ConfirmRegistrationComponent
      }
    ],
    component: ConfirmRegistrationComponent,
  },
  {path: 'users/login', component: LoginComponent},
  {path: 'users/info', component: ProfileComponent},
  {path: 'password/reset', component: ForgotPasswordComponent},
  {path: 'password/reset/success', component: ForgotPasswordSuccessComponent},
  {path: 'password/update/:token', component: UpdateForgotPasswordComponent},
  {path: 'reservations', component: ReservationComponent},
  {path: 'hospital_rooms', component: HospitalRoomComponent},
  {path: 'hospital_rooms/add', component: AddEditRoomComponent},
  {path: 'hospital_rooms/edit/:roomId', component: EditRoomComponent},
  {path: 'hospital_rooms/view', component: ViewRoomComponent},
  {path: 'hospital_rooms/view/edit', component: ViewEditRoomComponent},
  {path: 'hospital_rooms/details/:roomId', component: DetailsRoomComponent},
  {path: 'reservations/add/:roomId', component: AddReservationComponent},
  {path: '**', component: ErrorPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
