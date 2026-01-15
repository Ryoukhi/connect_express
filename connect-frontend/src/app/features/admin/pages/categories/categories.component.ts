import { Component, inject, OnInit } from '@angular/core';
import { AdminService, Category } from '../../services/admin.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.css'
})
export class CategoriesComponent implements OnInit {
  private adminService = inject(AdminService);

  categories: Category[] = [];
  showModal = false;
  isEditing = false;
  currentCategory: Partial<Category> = {};

  ngOnInit() {
    this.loadCategories();
  }

  loadCategories() {
    this.adminService.getCategories().subscribe(res => {
      this.categories = res;
    });
  }

  openAddModal() {
    this.isEditing = false;
    this.currentCategory = { name: '', description: '' };
    this.showModal = true;
  }

  openEditModal(category: Category) {
    this.isEditing = true;
    this.currentCategory = { ...category };
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
  }

  saveCategory() {
    if (this.isEditing && this.currentCategory.id) {
      this.adminService.updateCategory(this.currentCategory.id, this.currentCategory).subscribe(() => {
        this.loadCategories();
        this.closeModal();
      });
    } else {
      this.adminService.addCategory(this.currentCategory as any).subscribe(() => {
        this.loadCategories();
        this.closeModal();
      });
    }
  }

  deleteCategory(id: number) {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette catégorie ?')) {
      this.adminService.deleteCategory(id).subscribe(() => {
        this.loadCategories();
      });
    }
  }
}
