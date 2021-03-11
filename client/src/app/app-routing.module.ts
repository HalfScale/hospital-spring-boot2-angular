import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ConfirmRegistrationComponent } from './components/confirm-registration/confirm-registration.component';
import { ErrorPageComponent } from './components/error-page/error-page.component';
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password.component';
import { HomeComponent } from './components/home/home.component';
import { HospitalRoomComponent } from './components/hospital-room/hospital-room.component';
import { LoginComponent } from './components/login/login.component';
import { RegistrationComponent } from './components/registration/registration.component';
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
  {path: 'password/reset', component: ForgotPasswordComponent},
  {path: 'update-forgot-password', component: UpdateForgotPasswordComponent},
  {path: 'hospitalroom', component: HospitalRoomComponent},
  {path: '**', component: ErrorPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
