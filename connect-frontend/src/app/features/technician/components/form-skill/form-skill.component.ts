import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CompétencesTechnicienService, DocumentsTechnicienService, CategoryControllerService } from '../../../../api/services';
import { TechnicianSkillDto, TechnicianDocumentDto, CategoryDto } from '../../../../api/models';
import { downloadFile } from '../../../../api/utils/file-download';
import { StorageService } from '../../../../services/storage.service';

@Component({
  selector: 'app-form-skill',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './form-skill.component.html',
  styleUrl: './form-skill.component.css'
})
export class FormSkillComponent implements OnInit {

  skillForm!: FormGroup;
  docForm!: FormGroup;
  skills: TechnicianSkillDto[] = [];
  categories: CategoryDto[] = [];
  selectedSkill: TechnicianSkillDto | null = null;
  documents: TechnicianDocumentDto[] = [];
  editing = false;

  isUploadingDoc = false;
  uploadedDocUrl: string | null = null;
  selectedFileName: string | null = null;

  constructor(
    private fb: FormBuilder,
    private compService: CompétencesTechnicienService,
    private documentsService: DocumentsTechnicienService,
    private categoryService: CategoryControllerService,
    private storageService: StorageService
  ) { }

  ngOnInit(): void {
    this.skillForm = this.fb.group({
      idSkill: [null],
      name: ['', Validators.required],
      idCategory: [null],
      description: [''],
      level: [1, [Validators.min(1), Validators.max(5)]],
      yearsExperience: [0, [Validators.min(0)]],
      hourlyRate: [0, [Validators.min(0)]],
      availabilityStatus: ['AVAILABLE']
    });

    this.docForm = this.fb.group({
      type: ['IDENTITY_CARD', Validators.required],
      url: ['', Validators.required]
    });

    this.loadCategories();
    this.loadSkills();
  }

  loadCategories() {
    this.categoryService.getActiveCategories('body').subscribe({
      next: (data: any) => this.categories = data || [],
      error: () => this.categories = []
    });
  }

  loadSkills() {
    this.compService.getMySkills('body').subscribe({
      next: (data: any) => this.skills = Array.isArray(data) ? data : (data || []),
      error: () => this.skills = []
    });
  }

  onSubmit() {
    if (this.skillForm.invalid) {
      this.skillForm.markAllAsTouched();
      return;
    }

    const value = this.skillForm.value;
    const dto: TechnicianSkillDto = {
      idSkill: value.idSkill,
      name: value.name,
      description: value.description,
      level: value.level,
      yearsExperience: value.yearsExperience,
      hourlyRate: value.hourlyRate,
      idCategory: value.idCategory,
      availabilityStatus: value.availabilityStatus
    };

    if (this.editing && dto.idSkill) {
      this.compService.updateSkill(dto.idSkill, dto, 'body').subscribe({
        next: () => {
          this.loadSkills();
          this.resetForm();
        },
        error: () => { }
      });
    } else {
      this.compService.addSkill(dto, 'body').subscribe({
        next: () => {
          this.loadSkills();
          this.resetForm();
        },
        error: () => { }
      });
    }
  }

  editSkill(s: TechnicianSkillDto) {
    this.editing = true;
    this.skillForm.patchValue({
      idSkill: s.idSkill,
      name: s.name,
      idCategory: s.idCategory,
      description: s.description,
      level: s.level,
      yearsExperience: s.yearsExperience,
      hourlyRate: s.hourlyRate,
      availabilityStatus: s.availabilityStatus || 'AVAILABLE'
    });
    // also select it so documents can be managed
    this.selectSkill(s);
  }

  resetForm() {
    this.editing = false;
    this.skillForm.reset({
      idSkill: null,
      name: '',
      idCategory: null,
      description: '',
      level: 1,
      yearsExperience: 0,
      hourlyRate: 0,
      availabilityStatus: 'AVAILABLE'
    });
  }

  deleteSkill(id?: number) {
    if (!id) return;
    if (!confirm('Supprimer cette compétence ?')) return;
    this.compService.deleteSkill(id, 'body').subscribe({
      next: () => {
        if (this.selectedSkill && this.selectedSkill.idSkill === id) {
          this.selectedSkill = null;
          this.documents = [];
        }
        this.loadSkills();
      },
      error: () => { }
    });
  }

  selectSkill(s: TechnicianSkillDto) {
    this.selectedSkill = s;
    if (s && s.idSkill) this.loadDocuments(s.idSkill);
  }

  loadDocuments(skillId?: number) {
    if (!skillId) { this.documents = []; return; }
    this.documentsService.getBySkill(skillId, 'body').subscribe({
      next: (data: any) => this.documents = Array.isArray(data) ? data : (data || []),
      error: () => this.documents = []
    });
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      this.selectedFileName = file.name;
      this.isUploadingDoc = true;
      this.storageService.upload(file).subscribe({
        next: (response) => {
          this.isUploadingDoc = false;
          this.uploadedDocUrl = response.url;
          this.docForm.patchValue({ url: response.url });
        },
        error: (err) => {
          this.isUploadingDoc = false;
          this.selectedFileName = null;
          console.error('Upload failed', err);
        }
      });
    }
  }

  addDocument() {
    if (!this.selectedSkill || this.docForm.invalid || this.isUploadingDoc) return;
    const value = this.docForm.value;
    const dto: TechnicianDocumentDto = {
      idProfile: this.selectedSkill.idSkill,
      type: value.type,
      url: value.url
    };

    this.documentsService.addDocument(dto, 'body').subscribe({
      next: () => {
        this.loadDocuments(this.selectedSkill!.idSkill);
        this.docForm.reset({ type: 'IDENTITY_CARD', url: '' });
        this.uploadedDocUrl = null;
        this.selectedFileName = null;
      },
      error: () => { }
    });
  }

  deleteDocument(id?: number) {
    if (!id) return;
    if (!confirm('Supprimer ce document ?')) return;
    this.documentsService.deleteDocument(id, 'body').subscribe({
      next: () => this.loadDocuments(this.selectedSkill?.idSkill),
      error: () => { }
    });
  }

  downloadDocument(id?: number) {
    if (!id) return;
    // service's generated signature returns an object type but the response for this endpoint is a blob
    // cast to any to accept a Blob in the subscription callback
    (this.documentsService.getById2(id, 'body') as any).subscribe((blob: Blob) => {
      downloadFile(blob, `document-${id}`);
    }, () => {
      // ignore errors for now (could show a toast)
    });
  }

}
