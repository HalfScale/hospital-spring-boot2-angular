import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, ModalDismissReasons, NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { delay } from 'rxjs/operators';
import { HospitalRoomService } from 'src/app/services/hospital-room.service';

@Component({
  selector: 'ngbd-modal-content',
  template: `
    <div class="modal-header">
      <h4 class="modal-title">Delete Hospital Room</h4>
      <button type="button" class="close" aria-label="Close" (click)="activeModal.dismiss('Cross click')">
        <span aria-hidden="true">&times;</span>
      </button>
    </div>
    <div class="modal-body">
      <p>Are you sure you want to delete this room?</p>
    </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-outline-secondary" (click)="activeModal.dismiss()">cancel</button>
      <button type="button" class="btn btn-danger" (click)="close()">Ok</button>
    </div>
  `
})
export class NgbdModalContent {
  @Input() selectedRoom: any;

  constructor(public activeModal: NgbActiveModal,
    private hospitalRoomService: HospitalRoomService,
    private toastr: ToastrService,
    private router: Router) {
  }

  close() {
    console.log('roomIdToBeDeleted', this.selectedRoom);
    this.hospitalRoomService.deleteRoom(this.selectedRoom.id).subscribe(data => {

      this.toastr.success(`Room deleted ${this.selectedRoom.roomCode}`, undefined, {
        timeOut: 1000
      }).onHidden.subscribe(() => location.reload());

      this.activeModal.close();
    }, error => {

      this.toastr.error('Something went wrong... Try again.', undefined, {
        timeOut: 1000
      }).onHidden.subscribe(data => location.reload());
      this.activeModal.close();
    });
  }
}

@Component({
  selector: 'app-hospital-room',
  templateUrl: './hospital-room.component.html',
  styleUrls: ['./hospital-room.component.css']
})
export class HospitalRoomComponent implements OnInit {

  config: any;
  roomList = { data: [] as any };
  roomCode: String;
  roomName: String;
  requestParams: any;

  constructor(private hospitalRoomService: HospitalRoomService,
    private modalService: NgbModal) {

    this.initializeConfig();
    this.roomCode = '';
    this.roomName = '';
  }

  public open(room: any) {
    const modalRef = this.modalService.open(NgbdModalContent);
    console.log('roomId', room.id);
    this.setRoomId(room, modalRef);
  }

  initializeConfig(): void {
    this.config = {
      itemsPerPage: 10,
      currentPage: 1,
      totalItems: 0
    };
  }

  private setRoomId(room: any, modalInstance: NgbModalRef) {
    modalInstance.componentInstance.selectedRoom = room;
  }

  public search(): void {
    this.requestParams = {
      page: this.getCurrentPage(),
      size: this.getPageSize(),
      roomCode: this.roomCode,
      roomName: this.roomName,
    }
    console.log('requestParams', this.requestParams);
    this.getHospitalRooms(this.requestParams);
  }

  public clear(): void {
    this.roomCode = '';
    this.roomName = '';
  }

  getCurrentPage(): string {
    return (this.config.currentPage - 1).toString();
  }

  getPageSize(): string {
    return this.config.itemsPerPage.toString();
  }

  ngOnInit(): void {
    this.getHospitalRooms(this.requestParams);
  }

  getHospitalRooms(params: any): void {
    this.hospitalRoomService.getRoomsPageable(params).subscribe({
      next: data => {
        console.log('Query success', data);
        this.roomList.data = data.content;
        this.config.totalItems = data.totalElements;
      },
      error: error => {
        console.log('Error respnse', error);

      }
    });
  }

  pageChanged(currentPage: number) {
    this.config.currentPage = currentPage;
    this.search();
  }
}
