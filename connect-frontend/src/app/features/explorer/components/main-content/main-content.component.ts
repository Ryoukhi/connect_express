import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TechnicianProfileResponseDto, CategoryDto } from '../../../../api/models';
import { TechnicianControllerService } from '../../../../api/services/technicianController.service';
import { CategoryControllerService } from '../../../../api/services/categoryController.service';

@Component({
  selector: 'app-main-content',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './main-content.component.html',
  styleUrl: './main-content.component.css'
})
export class MainContentComponent {
  filters: {
    city?: string;
    district?: string;
    category?: string | null;
    availabilityStatus?: string;
    minRating?: number | null;
    maxPrice?: number | null;
  } = {
    city: '',
    district: '',
    category: null,
    availabilityStatus: '',
    minRating: null,
    maxPrice: 50000
  };

  categories: CategoryDto[] = [];
  technicians: TechnicianProfileResponseDto[] = [];
  loading = false;
  errorMsg: string | null = null;
  currentPage: number = 1;
  pageSize: number = 9; // 3 colonnes x 3 lignes
  totalTechnicians: number = 0;

  constructor(
    private technicianService: TechnicianControllerService,
    private categoryService: CategoryControllerService
  ) {
    // initial load
    this.loadCategories();
    this.search();
  }

  loadCategories() {
    this.categoryService.getActiveCategories('body').subscribe({
      next: (data) => this.categories = data || [],
      error: () => this.categories = []
    });
  }

  getCategoryName(idCategory?: number | null): string {
    if (!idCategory) return 'Service';
    const cat = this.categories.find(c => c.idCategory === idCategory);
    return cat?.name ?? 'Service';
  }

  getTechnicianDisplayName(tech: any): string {
    if (!tech) return 'Technicien';
    const first = tech.firstName || tech.technician?.firstName;
    const last = tech.lastName || tech.technician?.lastName;
    if (first || last) return `${first || ''} ${last || ''}`.trim();
    if (tech.fullName) return tech.fullName;
    if (tech.technicianId) return `Technicien #${tech.technicianId}`;
    return 'Technicien';
  }

  resetFilters() {
    this.filters = {
      city: '',
      district: '',
      category: null,
      availabilityStatus: '',
      minRating: null,
      maxPrice: 50000
    };
    this.search();
  }

  selectCategory(category: CategoryDto | null) {
    this.filters.category = category?.name || null;
    this.search();
  }

  search() {
    this.loading = true;
    this.errorMsg = null;
    this.currentPage = 1; // Reset to first page on new search

    const availability = this.filters.availabilityStatus === 'OFFLINE' ? 'UNAVAILABLE' : this.filters.availabilityStatus || undefined;

    this.technicianService.search(
      this.filters.city || undefined,
      this.filters.district || undefined,
      this.filters.category || undefined,
      availability as any,
      this.filters.minRating || undefined,
      undefined,
      this.filters.maxPrice || undefined,
      'body'
    ).subscribe({
      next: (data) => {
        const allTechnicians = data || [];
        this.totalTechnicians = allTechnicians.length;
        this.technicians = this.getPaginatedTechnicians(allTechnicians);
        this.loading = false;
      },
      error: (err) => {
        this.errorMsg = 'Une erreur est survenue lors de la recherche.';
        this.loading = false;
      }
    });
  }

  getPaginatedTechnicians(allTechnicians: TechnicianProfileResponseDto[]): TechnicianProfileResponseDto[] {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    return allTechnicians.slice(startIndex, endIndex);
  }

  getTotalPages(): number {
    return Math.ceil(this.totalTechnicians / this.pageSize) || 1;
  }

  nextPage(): void {
    if (this.currentPage < this.getTotalPages()) {
      this.currentPage++;
      this.loadPageResults();
    }
  }

  previousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.loadPageResults();
    }
  }

  private loadPageResults(): void {
    // Re-fetch all results and apply pagination
    // This is a simplified approach; ideally, backend would support offset/limit
    this.loading = true;
    const availability = this.filters.availabilityStatus === 'OFFLINE' ? 'UNAVAILABLE' : this.filters.availabilityStatus || undefined;

    this.technicianService.search(
      this.filters.city || undefined,
      this.filters.district || undefined,
      this.filters.category || undefined,
      availability as any,
      this.filters.minRating || undefined,
      undefined,
      this.filters.maxPrice || undefined,
      'body'
    ).subscribe({
      next: (data) => {
        const allTechnicians = data || [];
        this.technicians = this.getPaginatedTechnicians(allTechnicians);
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

}
