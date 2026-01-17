import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserControllerService } from '../../../../api/services/userController.service';
import { UserDto } from '../../../../api/models';
import { SidebarDashboardTechComponent } from '../../components/sidebar-dashboard-tech/sidebar-dashboard-tech.component';
import { HeaderDashboardTechComponent } from '../../components/header-dashboard-tech/header-dashboard-tech.component';
import { NavDashboardTechComponent } from '../../components/nav-dashboard-tech/nav-dashboard-tech.component';

@Component({
    selector: 'app-technician-profile',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        SidebarDashboardTechComponent,
        HeaderDashboardTechComponent,
        NavDashboardTechComponent
    ],
    templateUrl: './technician-profile.component.html'
})
export class TechnicianProfileComponent implements OnInit {
    user: UserDto | null = null;
    loading = false;
    submitLoading = false;
    photoLoading = false;
    errorMsg: string | null = null;
    successMsg: string | null = null;

    constructor(private userService: UserControllerService) { }

    ngOnInit() {
        this.loadProfile();
    }

    loadProfile() {
        this.loading = true;
        this.userService.getCurrentUser().subscribe({
            next: (user) => {
                this.user = user;
                this.loading = false;
            },
            error: (err) => {
                this.errorMsg = "Impossible de charger votre profil.";
                this.loading = false;
                console.error(err);
            }
        });
    }

    updateProfile() {
        if (!this.user) return;

        this.submitLoading = true;
        this.errorMsg = null;
        this.successMsg = null;

        this.userService.updateProfile(this.user).subscribe({
            next: (updatedUser) => {
                this.user = updatedUser;
                this.submitLoading = false;
                this.successMsg = "Profil mis à jour avec succès !";
                setTimeout(() => this.successMsg = null, 3000);
            },
            error: (err) => {
                this.submitLoading = false;
                this.errorMsg = "Erreur lors de la mise à jour du profil.";
                console.error(err);
            }
        });
    }

    onFileSelected(event: any) {
        const file: File = event.target.files[0];
        if (file) {
            this.uploadPhoto(file);
        }
    }

    uploadPhoto(file: File) {
        this.photoLoading = true;
        this.errorMsg = null;

        this.userService.updatePhoto(file).subscribe({
            next: (updatedUser) => {
                this.user = updatedUser;
                this.photoLoading = false;
                this.successMsg = "Photo de profil mise à jour !";
                setTimeout(() => this.successMsg = null, 3000);
            },
            error: (err) => {
                this.photoLoading = false;
                this.errorMsg = "Erreur lors de l'envoi de la photo.";
                console.error(err);
            }
        });
    }
}
