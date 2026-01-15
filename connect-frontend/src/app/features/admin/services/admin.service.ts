import { Injectable } from '@angular/core';
import { of } from 'rxjs';

export interface Category {
    id: number;
    name: string;
    description: string;
    count: number; // For demo: number of services/technicians
}

export interface Technician {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    specialty: string;
    status: 'PENDING' | 'ACTIVE' | 'REJECTED';
    joinedDate: Date;
}

export interface Client {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    joinedDate: Date;
}

export interface AdminUser {
    id: number;
    email: string;
    role: 'SUPER_ADMIN' | 'ADMIN';
    lastLogin: Date;
}

@Injectable({
    providedIn: 'root'
})
export class AdminService {
    // Mock data
    private categories: Category[] = [
        { id: 1, name: 'Plomberie', description: 'Réparations et installations sanitaires', count: 12 },
        { id: 2, name: 'Électricité', description: 'Installations électriques et dépannage', count: 8 },
        { id: 3, name: 'Jardinage', description: 'Entretien des espaces verts', count: 5 },
        { id: 4, name: 'Ménage', description: 'Nettoyage domestique et industriel', count: 20 },
    ];

    private technicians: Technician[] = [
        { id: 1, firstName: 'Jean', lastName: 'Dupont', email: 'jean.dupont@email.com', specialty: 'Plomberie', status: 'ACTIVE', joinedDate: new Date('2023-01-15') },
        { id: 2, firstName: 'Marie', lastName: 'Curie', email: 'marie.curie@email.com', specialty: 'Électricité', status: 'PENDING', joinedDate: new Date('2023-11-20') },
        { id: 3, firstName: 'Paul', lastName: 'Martin', email: 'paul.martin@email.com', specialty: 'Jardinage', status: 'REJECTED', joinedDate: new Date('2023-10-05') },
    ];

    private clients: Client[] = [
        { id: 1, firstName: 'Sophie', lastName: 'Lefebvre', email: 'sophie.l@email.com', phone: '0612345678', joinedDate: new Date('2023-05-12') },
        { id: 2, firstName: 'Lucas', lastName: 'Bernard', email: 'lucas.b@email.com', phone: '0687654321', joinedDate: new Date('2023-09-01') },
    ];

    private admins: AdminUser[] = [
        { id: 1, email: 'admin@connect.com', role: 'SUPER_ADMIN', lastLogin: new Date() },
        { id: 2, email: 'moderator@connect.com', role: 'ADMIN', lastLogin: new Date('2023-12-25') },
    ];

    constructor() { }

    // ... existing getters ...

    getClients() { return of(this.clients); }
    deleteClient(id: number) { this.clients = this.clients.filter(c => c.id !== id); return of(true); }

    getAdmins() { return of(this.admins); }
    deleteAdmin(id: number) { this.admins = this.admins.filter(a => a.id !== id); return of(true); }

    getTechnicians() {
        return of(this.technicians);
    }

    updateTechnicianStatus(id: number, status: 'ACTIVE' | 'REJECTED') {
        const tech = this.technicians.find(t => t.id === id);
        if (tech) {
            tech.status = status;
        }
        return of(tech);
    }

    deleteTechnician(id: number) {
        this.technicians = this.technicians.filter(t => t.id !== id);
        return of(true);
    }

    getCategories() {
        return of(this.categories);
    }

    addCategory(category: Omit<Category, 'id' | 'count'>) {
        const newId = Math.max(...this.categories.map(c => c.id)) + 1;
        const newCat = { ...category, id: newId, count: 0 };
        this.categories.push(newCat);
        return of(newCat);
    }

    updateCategory(id: number, category: Partial<Category>) {
        const index = this.categories.findIndex(c => c.id === id);
        if (index !== -1) {
            this.categories[index] = { ...this.categories[index], ...category };
            return of(this.categories[index]);
        }

        return of(null as any); // Cast to any to avoid strict Observable<null> issues, or better typing
    }

    deleteCategory(id: number) {
        this.categories = this.categories.filter(c => c.id !== id);
        return of(true);
    }
}
