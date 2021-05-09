export class ReservationRequest {
    hospitalRoomId: number;
    hasAssociatedAppointment: boolean;
    associatedAppointmentId: boolean;
    reservedDate: string;
    reservedTime: string;
    reservedEndDate: string;
    reservedEndTime: string;
}