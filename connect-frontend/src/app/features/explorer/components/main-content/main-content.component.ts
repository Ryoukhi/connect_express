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
        this.technicians = data || [];
        this.loading = false;
      },
      error: (err) => {
        this.errorMsg = 'Une erreur est survenue lors de la recherche.';
        this.loading = false;
      }
    });
  }

}
