import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Category {
    id: number;
    name: string;
    description: string;
    count: number;
}

export interface UserDto {
    idUser: number;
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    role: string;
    city?: string;
    active?: boolean;
    createdAt?: string;
}

export interface Technician extends UserDto {
    specialty: string;
    status: 'PENDING' | 'ACTIVE' | 'REJECTED'; // Frontend mapped status, or use DB 'active' + 'verified' etc
    // Backend Technician has: active (bool), identificationNumber, etc.
    // Backend doesn't strictly have a 'status' enum like PENDING/REJECTED exposed directly as string unless mapped.
    // However, backend has 'active' boolean. And 'kycVerified' maybe?
    // Let's assume for now we use 'active' boolean.
    // But frontend used 'PENDING'.
    // Logic: if !active -> PENDING? Or if !kycVerified?
    // Backend 'validateKyc' activates the tech. So inactive = PENDING.
    joinedDate: string; // from createdAt
    neighborhood?: string;
}

export interface TechnicianSkillDto {
    idSkill: number;
    name: string;
    description: string;
    level: string;
    verified: boolean;
    yearsExperience?: number;
    hourlyRate?: number;
}

export interface AdminStatistics {
    totalReservations: number;
    totalRevenue: number;
    activeTechnicians: number;
    activeClients: number;
}

@Injectable({
    providedIn: 'root'
})
export class AdminService {
    private apiUrl = 'http://localhost:8080/api';

    constructor(private http: HttpClient) { }

    // ================= DASHBOARD =================
    getGeneralStatistics(): Observable<AdminStatistics> {
        return this.http.get<AdminStatistics>(`${this.apiUrl}/admin/statistics/general`);
    }

    // ================= USERS =================
    getAllUsers(page: number = 0, size: number = 10): Observable<UserDto[]> {
        let params = new HttpParams().set('page', page.toString()).set('size', size.toString());
        return this.http.get<UserDto[]>(`${this.apiUrl}/admin/users`, { params });
    }

    searchUsers(query?: string, role?: string): Observable<UserDto[]> {
        let params = new HttpParams();
        if (query) params = params.set('query', query);
        if (role) params = params.set('role', role);
        return this.http.get<UserDto[]>(`${this.apiUrl}/admin/users/search`, { params });
    }

    suspendUser(userId: number): Observable<void> {
        return this.http.patch<void>(`${this.apiUrl}/admin/users/${userId}/suspend`, {});
    }

    activateUser(userId: number): Observable<void> {
        return this.http.patch<void>(`${this.apiUrl}/admin/users/${userId}/activate`, {});
    }

    deleteUser(userId: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/admin/users/${userId}`);
    }

    getClients(): Observable<UserDto[]> {
        return this.searchUsers(undefined, 'CLIENT');
    }

    getAdmins(): Observable<UserDto[]> {
        return this.searchUsers(undefined, 'ADMIN');
    }

    // ================= TECHNICIANS =================
    getTechnicians(): Observable<Technician[]> {
        // Use the new endpoint
        return this.http.get<Technician[]>(`${this.apiUrl}/technicians/all`);
    }

    updateTechnicianStatus(id: number, status: 'ACTIVE' | 'REJECTED'): Observable<void> {
        // Backend: validateKyc activates.
        // If status is ACTIVE -> validateKyc
        // If REJECTED -> maybe delete or custom endpoint?
        // For now, if ACTIVE -> call validate-kyc
        if (status === 'ACTIVE') {
            return this.http.put<void>(`${this.apiUrl}/technicians/${id}/validate-kyc`, {});
        } else {
            // If rejected, maybe deactivate or delete?
            // Using suspend for REJECTED/DEACTIVATE
            return this.suspendUser(id);
        }
    }

    deleteTechnician(id: number): Observable<void> {
        return this.deleteUser(id);
    }

    getTechnicianSkills(technicianId: number): Observable<TechnicianSkillDto[]> {
        return this.http.get<TechnicianSkillDto[]>(`${this.apiUrl}/technician-skills/technician/${technicianId}`);
    }

    verifySkill(skillId: number, verified: boolean): Observable<TechnicianSkillDto> {
        // Correct endpoint usage with params
        return this.http.post<TechnicianSkillDto>(`${this.apiUrl}/technician-skills/${skillId}/verify`, {}, {
            params: new HttpParams().set('verified', verified)
        });
    }

    // ================= CATEGORIES =================
    getCategories(): Observable<Category[]> {
        return this.http.get<Category[]>(`${this.apiUrl}/categories`);
    }

    addCategory(category: any): Observable<Category> {
        return this.http.post<Category>(`${this.apiUrl}/categories`, category);
    }

    updateCategory(id: number, category: any): Observable<Category> {
        return this.http.put<Category>(`${this.apiUrl}/categories/${id}`, category);
    }

    deleteCategory(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/categories/${id}`);
    }

    createAdmin(adminData: any): Observable<any> {
        return this.http.post<any>(`${this.apiUrl}/admin/users/admins`, adminData);
    }
}
