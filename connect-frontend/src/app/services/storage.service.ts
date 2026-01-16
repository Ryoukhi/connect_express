import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BASE_PATH_DEFAULT } from '../api/tokens';

@Injectable({
    providedIn: 'root'
})
export class StorageService {

    private readonly httpClient: HttpClient = inject(HttpClient);
    private readonly basePath: string = inject(BASE_PATH_DEFAULT);

    constructor() { }

    /**
     * Upload a file to the server
     * @param file The file to upload
     * @returns Observable containing the uploaded file URL
     */
    upload(file: File): Observable<{ url: string }> {
        const formData = new FormData();
        formData.append('file', file);

        const url = `${this.basePath}/api/storage/upload`;
        return this.httpClient.post<{ url: string }>(url, formData);
    }
}
