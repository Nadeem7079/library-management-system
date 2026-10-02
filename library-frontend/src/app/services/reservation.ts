import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class Reservation {

  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  private getHeaders() {

    const token = localStorage.getItem('token');

    return new HttpHeaders({
      Authorization: 'Bearer ' + token
    });

  }

  reserveBook(userId: number, bookId: number) {

    return this.http.post(
      `${this.apiUrl}/reservations?userId=${userId}&bookId=${bookId}`,
      {},
      {
        headers: this.getHeaders()
      }
    );

  }

  getUserReservations(userId: number) {

    return this.http.get<any[]>(
      `${this.apiUrl}/users/${userId}/reservations`,
      {
        headers: this.getHeaders()
      }
    );

  }

  returnBook(reservationId: number) {

    return this.http.put(
      `${this.apiUrl}/reservations/${reservationId}/return`,
      {},
      {
        headers: this.getHeaders()
      }
    );

  }

  getAllReservations() {

    return this.http.get(
      `${this.apiUrl}/admin/reservations`,
      {
        headers: this.getHeaders()
      }
    );

  }

}