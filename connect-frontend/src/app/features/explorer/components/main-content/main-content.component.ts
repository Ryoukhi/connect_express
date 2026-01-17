import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TechnicianResultSearchDto, CategoryDto } from '../../../../api/models';
import { TechniciensService } from '../../../../api/services/techniciens.service';
import { CategoryControllerService } from '../../../../api/services/categoryController.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main-content',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './main-content.component.html',
  styleUrl: './main-content.component.css'
})
export class MainContentComponent implements OnInit {
  filters: {
    city?: string;
    district?: string;
    category?: string | null;
    availabilityStatus?: string;
    minRating?: number | null;
    minPrice?: number | null;
    maxPrice?: number | null;
  } = {
      city: '',
      district: '',
      category: null,
      availabilityStatus: '',
      minRating: null,
      minPrice: 0,
      maxPrice: 50000
    };
  showMobileFilters = false;

  categories: CategoryDto[] = [];
  categoriesLoading = false;
  technicians: TechnicianResultSearchDto[] = [];
  loading = false;
  errorMsg: string | null = null;
  currentPage: number = 1;
  pageSize: number = 9; // 3 colonnes x 3 lignes
  totalTechnicians: number = 0;

  availableCities: string[] = [];
  availableNeighborhoods: string[] = [];

  ngOnInit(): void {
    this.loadCategories();
    this.search;
  }

  constructor(
    private techniciensService: TechniciensService,

    private categoryService: CategoryControllerService,
    private router: Router
  ) {
    // initial load
    this.loadCategories();
    this.loadCities();
    this.search();
  }

  loadCategories() {
    this.categoriesLoading = true;
    this.categoryService.getActiveCategories('body').subscribe({
      next: (data) => {
        console.log('Categories from API:', data);
        this.categories = data || [];
        this.categoriesLoading = false;
      },
      error: (err) => {
        console.error('Error loading categories', err);
        this.categories = [];
        this.categoriesLoading = false;
      }
    });
  }

  getCategoryName(idCategory?: number | null): string {
    if (!idCategory) return 'Service';
    const cat = this.categories.find(c => c.idCategory === idCategory);
    return cat?.name ?? 'Service';
  }

  getTechnicianDisplayName(tech: any): string {
    if (!tech) return 'Technicien';
    if (tech.name) return tech.name;
    if (tech.fullName) return tech.fullName;
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
    this.availableNeighborhoods = [];
    this.search();
  }

  loadCities() {
    this.techniciensService.getAvailableCities('body').subscribe({
      next: (cities) => {
        this.availableCities = cities || [];
      },
      error: (err) => {
        console.error('Error loading cities', err);
        this.availableCities = [];
      }
    });
  }

  onCityChange() {
    // Reset neighborhood when city changes
    this.filters.district = '';
    this.availableNeighborhoods = [];

    if (this.filters.city) {
      this.techniciensService.getAvailableNeighborhoods(this.filters.city, 'body').subscribe({
        next: (neighborhoods) => {
          this.availableNeighborhoods = neighborhoods || [];
        },
        error: (err) => {
          console.error('Error loading neighborhoods', err);
          this.availableNeighborhoods = [];
        }
      });
    }
  }

  selectCategory(category: CategoryDto | null) {
    this.filters.category = category?.name || null;
    this.search();
  }

  toggleMobileFilters() {
    this.showMobileFilters = !this.showMobileFilters;
  }

  search() {
    this.loading = true;
    this.errorMsg = null;
    this.currentPage = 1; // Reset to first page on new search
    this.showMobileFilters = false; // Close mobile filters on search

    const availability = this.filters.availabilityStatus || undefined;

    this.techniciensService.searchTechnicians(
      this.filters.city || undefined,
      this.filters.district || undefined,
      this.filters.category || undefined,
      availability as any,
      this.filters.minRating || undefined,
      this.filters.minPrice || undefined,
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

  getPaginatedTechnicians(allTechnicians: TechnicianResultSearchDto[]): TechnicianResultSearchDto[] {
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
    this.loading = true;
    const availability = this.filters.availabilityStatus || undefined;

    this.techniciensService.searchTechnicians(
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

  bookTechnician(tech: TechnicianResultSearchDto) {
    this.router.navigate(['/client/book', (tech as any).id]);
  }

  trackByCategory(index: number, category: CategoryDto): number {
    return category.idCategory || index;
  }
}
