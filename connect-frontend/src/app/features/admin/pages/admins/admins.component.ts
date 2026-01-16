import { Component, inject, OnInit } from '@angular/core';
import { AdminService, UserDto } from '../../services/admin.service';
import { CommonModule, DatePipe } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-admins',
  standalone: true,
  imports: [CommonModule, DatePipe, ReactiveFormsModule],
  templateUrl: './admins.component.html',
  styleUrl: './admins.component.css'
})
export class AdminsComponent implements OnInit {
  private adminService = inject(AdminService);
  private fb = inject(FormBuilder);

  admins: UserDto[] = [];
  showCreateForm = false;
  createForm: FormGroup;
  errorMsg = '';

  constructor() {
    this.createForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      phone: ['']
    });
  }

  ngOnInit() {
    this.loadAdmins();
  }

  loadAdmins() {
    this.adminService.getAdmins().subscribe(res => {
      this.admins = res;
    });
  }

  deleteAdmin(id: number) {
    if (confirm('Supprimer cet administrateur ?')) {
      this.adminService.deleteUser(id).subscribe(() => this.loadAdmins());
    }
  }

  createAdmin() {
    if (this.createForm.invalid) return;

    this.adminService.createAdmin(this.createForm.value).subscribe({
      next: () => {
        this.showCreateForm = false;
        this.createForm.reset();
        this.loadAdmins();
      },
      error: (err) => {
        this.errorMsg = 'Erreur lors de la création';
        console.error(err);
      }
    });
  }

  toggleCreateForm() {
    this.showCreateForm = !this.showCreateForm;
    this.errorMsg = '';
  }
}
