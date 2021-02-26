import { TestBed } from '@angular/core/testing';

import { HospitalRoomService } from './hospital-room.service';

describe('HospitalRoomService', () => {
  let service: HospitalRoomService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HospitalRoomService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
