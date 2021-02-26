import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HospitalRoomComponent } from './hospital-room.component';

describe('HospitalRoomComponent', () => {
  let component: HospitalRoomComponent;
  let fixture: ComponentFixture<HospitalRoomComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HospitalRoomComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HospitalRoomComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
