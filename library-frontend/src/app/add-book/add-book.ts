import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Book } from '../services/book';

@Component({
  selector: 'app-add-book',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './add-book.html',
  styleUrl: './add-book.css'
})
export class AddBook implements OnChanges {

  @Input() bookToEdit: any = null;
  @Output() bookSaved = new EventEmitter<void>();
  editBookId: number | null = null;

  title = '';
  author = '';
  isbn = '';
  availableCopies = 1 ;

  constructor(private bookService: Book) {}


 ngOnChanges(changes: SimpleChanges): void {

  if (changes['bookToEdit'] && this.bookToEdit) {

    this.editBookId = this.bookToEdit.id;

    this.title = this.bookToEdit.title;
    this.author = this.bookToEdit.author;
    this.isbn = this.bookToEdit.isbn;
    this.availableCopies = this.bookToEdit.availableCopies;

  }

}



  addBook() {

    const book = {

      title: this.title,
      author: this.author,
      isbn: this.isbn,
      available: true,
      availableCopies: this.availableCopies

    };

    this.bookService.addBook(book)
      .subscribe({

        next: (data) => {

          console.log('Book Added', data);

          alert('Book Added Successfully');

          this.bookSaved.emit();

          this.title = '';
          this.author = '';
          this.isbn = '';
          this.availableCopies = 1;

        },

        error: (err) => {

          console.error(err);

          alert('Failed to Add Book');

        }

      });

  }




 updateBook() {

  const book = {

    title: this.title,
    author: this.author,
    isbn: this.isbn,
    available: true,
    availableCopies: this.availableCopies

  };

  this.bookService.updateBook(this.editBookId!, book)
    .subscribe({

      next: () => {

        alert("Book Updated Successfully!");

        this.bookSaved.emit();

        this.cancelEdit();

      },

      error: (err) => {

        console.error(err);

        alert("Failed to Update Book");

      }

    });

}



  saveBook() {

  if (this.editBookId) {

    this.updateBook();

  } else {

    this.addBook();

  }

}


cancelEdit() {

  this.editBookId = null;

  this.title = '';
  this.author = '';
  this.isbn = '';
  this.availableCopies = 1;

}



}