import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthControllerService } from '../../../api/services/authController.service';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { RegisterDto } from '../../../api/models';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/auth.service';
import { StorageService } from '../../../services/storage.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, RouterModule],
  templateUrl: './register.component.html'
})
export class RegisterComponent implements OnInit {

  registerForm!: FormGroup;
  submitting = false;
  serverError: string | null = null;

  // Liste des villes du Cameroun
  villes: string[] = [
    'Douala', 'Yaoundé', 'Garoua', 'Kousseri', 'Bamenda',
    'Nkongsamba', 'Bafoussam', 'Maroua', 'Bertoua', 'Ebolowa',
    'Loum', 'Kribi', 'Limbe', 'Mbouda', 'Foumban',
    'Foumbot', 'Ngaoundéré', 'Edéa', 'Mbalmayo', 'Buea', 'Yagoua', 'Kumba', 'Bangangté', 'Dschang', 'Bafang'
  ];

  isUploading = false;
  previewUrl: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authControllerService: AuthControllerService,
    private authService: AuthService,
    private storageService: StorageService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern('^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).+$')
      ]],
      phone: ['', [Validators.required, Validators.pattern('^[0-9]{8,15}$')]],
      city: ['', Validators.required],        // <-- valeur choisie dans select
      neighborhood: ['', Validators.required],
      profilePhotoUrl: ['']
    });
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      this.isUploading = true;
      this.storageService.upload(file).subscribe({
        next: (response) => {
          this.isUploading = false;
          this.registerForm.patchValue({ profilePhotoUrl: response.url });
          this.previewUrl = response.url;
        },
        error: (err) => {
          this.isUploading = false;
          console.error('Upload failed', err);
          // Optional: Show error to user
        }
      });
    }
  }

  get f() { return this.registerForm.controls; }

  onSubmit() {
    this.serverError = null;

    if (this.isUploading) {
      return;
    }

    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    this.submitting = true;

    // Création explicite du RegisterDto pour éviter les problèmes d'index signature
    const formValue = this.registerForm.value;
    const registerDto: RegisterDto = {
      firstName: formValue['firstName'],
      lastName: formValue['lastName'],
      email: formValue['email'],
      password: formValue['password'],
      phone: formValue['phone'],
      city: formValue['city'],
      neighborhood: formValue['neighborhood'],
      profilePhotoUrl: formValue['profilePhotoUrl'] || null, // optionnel
    };

    this.authControllerService.register(registerDto, 'body').subscribe({
      next: (response) => {
        this.submitting = false;

        this.authService.storeSession(response)

        // Redirection après inscription
        this.router.navigate(['/catalogue']);
      },
      error: (err) => {
        this.submitting = false;
        this.serverError = err?.error?.message || 'Une erreur est survenue';
      }
    });
  }
}
