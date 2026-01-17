import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink, RouterModule } from "@angular/router";
import { AuthControllerService, AuthenticationService, RegisterDto } from '../../../../api';
import { AuthService } from '../../../../services/auth.service';
import { StorageService } from '../../../../services/storage.service';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-form-tech',
  imports: [RouterLink, CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './form-tech.component.html',
  styleUrl: './form-tech.component.css'
})
export class FormTechComponent implements OnInit {

  registerForm!: FormGroup;
  submitting = false;
  serverError: string | null = null;
  isUploading = false;
  previewUrl: string | null = null;

  // Liste des villes du Cameroun
  villes: string[] = [
    'Douala', 'Yaoundé', 'Garoua', 'Kousseri', 'Bamenda',
    'Nkongsamba', 'Bafoussam', 'Maroua', 'Bertoua', 'Ebolowa',
    'Loum', 'Kribi', 'Limbe', 'Mbouda', 'Foumban',
    'Foumbot', 'Ngaoundéré', 'Edéa', 'Mbalmayo', 'Buea', 'Yagoua', 'Kumba', 'Bangangté', 'Dschang', 'Bafang'
  ];

  constructor(
    private fb: FormBuilder,
    private authentificationService: AuthenticationService,
    private authService: AuthService,
    private storageService: StorageService,
    private router: Router
  ) {}


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

  get f() { return this.registerForm.controls; }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.isUploading = true;
      this.storageService.upload(file).subscribe({
        next: (response) => {
          this.previewUrl = response.url;
          this.registerForm.patchValue({ profilePhotoUrl: response.url });
          this.isUploading = false;
        },
        error: (err) => {
          console.error('Upload error', err);
          this.serverError = "Erreur lors de l'upload de la photo";
          this.isUploading = false;
        }
      });
    }
  }

  onSubmit() {
    this.serverError = null;

    if (this.registerForm.invalid || this.isUploading) {
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

    this.authentificationService.registerTechnician1(registerDto, 'body').subscribe({
      next: (response) => {
        this.submitting = false;

        this.authService.storeSession(response)

        // Redirection après inscription
        this.router.navigate(['/dashboard-technicien']);
      },
      error: (err) => {
        this.submitting = false;
        this.serverError = err?.error?.message || 'Une erreur est survenue';
      }
    });
  }

}
