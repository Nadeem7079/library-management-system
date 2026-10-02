
import { Component } from '@angular/core';
import { Book } from '../services/book';
import { CommonModule } from '@angular/common';
import {AddBook} from '../add-book/add-book';
import { FormsModule } from '@angular/forms';
import {Auth} from '../services/auth';
import { User } from '../services/user';
import { Reservation } from '../services/reservation';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, AddBook],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})


export class Dashboard {

  constructor(
    private bookService: Book,
    private authService: Auth,
    private userService: User,
    private reservationService: Reservation,
    private cdr: ChangeDetectorRef

  ){}

  reservations: any[] = [];
  user: any = null;
  role = '';
  books: any[] = [];
  selectedBook: any = null;
  search = '';
  filteredBooks: any[] = [];
  allReservations: any[] = [];
  users: any[] = [];


  getAvailableBooksCount(): number {
  return this.books.filter(
    book => book.availableCopies > 0
  ).length;
}

  loadBooks() {
    console.log("Calling API,..");

    this.bookService.getBooks().subscribe({

      next: (data) => {
        console.log("Books received");
        console.log(data);
;
        this.books = data;
        this.filteredBooks = data;
        this.cdr.detectChanges();

        console.log('books:', this.books);
        console.log('filteredBooks:', this.filteredBooks);

        console.log("length = ", this.books.length);


      },

      error: (err) => {

        console.error(err);

      }

    });

  }

  ngOnInit() {


    // this.loadBooks();
    this.role = this.authService.getRole();
    console.log("Role:", this.role);
    console.log("Dashboard Loaded");

    const email = this.authService.getUserEmail();
    console.log("Logged in email:", email);

    this.userService.getUserByEmail(email).subscribe({
      next: (data) => { 
        console.log("LoggedIn user:", data);
          this.user = data; 
        },
        error:(err)=>{
          console.error("fAILED TO GET USER:", err);
        }

    });

    this.loadBooks();

  }

  logout() {

  localStorage.removeItem('token');

  window.location.reload();

}

  deleteBook(id: number) {

    this.bookService.deleteBook(id)
      .subscribe({

        next: () => {

          alert('Book Deleted');

          this.loadBooks();

        },

        error: (err) => {

          console.error(err);

        }

      });

  }



  editBook(book: any){
    console.log("Edit Clicked: ", book);

    this.selectedBook = book;

  }

  onBookSaved(){
    this.loadBooks();
    this.selectedBook = null;
  }


  searchBooks() {

  this.filteredBooks = this.books.filter(book =>

    book.title.toLowerCase().includes(this.search.toLowerCase()) ||

    book.author.toLowerCase().includes(this.search.toLowerCase())

  );

}


reserveBook(bookId: number) {

  if (!this.user) {
    alert('User information not loaded yet');
    return;
  }

  this.reservationService
    .reserveBook(this.user.id, bookId)
    .subscribe({

      next: (data) => {

        console.log('Reservation successful:', data);

        alert('Book Reserved Successfully!');

        this.loadBooks();

      },

      error: (err) => {

        console.error('Reservation failed:', err);

        alert('Reservation failed');

      }

    });

}


getReservations() {

  if (!this.user) {
    alert('User information not loaded yet');
    return;
  }

  this.reservationService
    .getUserReservations(this.user.id)
    .subscribe({

      next: (data) => {

        console.log('My Reservations:', data);

        this.reservations = [...data];
        this.cdr.detectChanges();

      },

      error: (err) => {

        console.error('Failed to fetch reservations:', err);

        alert('Failed to load reservations');

      }

    });

}




returnBook(reservationId: number) {

  this.reservationService
    .returnBook(reservationId)
    .subscribe({

      next: (data) => {

        console.log('Book returned:', data);

        alert('Book Returned Successfully!');

        // Refresh reservations
        this.getReservations();

        // Refresh books
        this.loadBooks();

        this.cdr.detectChanges();

      },

      error: (err) => {

        console.error('Return failed:', err);

        alert('Failed to return book');

      }

    });

}




getAllReservations() {

  this.reservationService
    .getAllReservations()
    .subscribe({

      next: (data: any) => {

        console.log("All Reservations:", data);

        this.allReservations = [...data];

        this.cdr.detectChanges();

      },

      error: (err) => {

        console.error(err);

        alert("Failed to load reservations");

      }

    });

}



getUsers() {

  this.userService.getUsers().subscribe({

    next: (data: any) => {

      console.log("Users:", data);

      this.users = [...data];

      this.cdr.detectChanges();

    },

    error: (err) => {

      console.error(err);

      alert("Failed to load users");

    }

  });

}



deleteUser(id: number) {

  if (!confirm("Are you sure you want to delete this user?")) {
    return;
  }

  this.userService.deleteUser(id)
    .subscribe({

      next: () => {

        alert("User deleted successfully");

        this.getUsers();

      },

      error: (err) => {

        console.error(err);

        alert("Failed to delete user");

      }

    });

}

}