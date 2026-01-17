/* @ts-nocheck */
/* eslint-disable */
/* @noformat */
/* @formatter:off */
/**
* Generated Angular service for UserController
*/
import { HttpClient, HttpContext, HttpContextToken, HttpEvent, HttpHeaders, HttpParams, HttpResponse } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { BASE_PATH_DEFAULT, CLIENT_CONTEXT_TOKEN_DEFAULT } from "../tokens";
import { RequestOptions, UserDto } from "../models";

@Injectable({ providedIn: "root" })
export class UserControllerService {
    private readonly httpClient: HttpClient = inject(HttpClient);
    private readonly basePath: string = inject(BASE_PATH_DEFAULT);
    private readonly clientContextToken: HttpContextToken<string> = CLIENT_CONTEXT_TOKEN_DEFAULT;

    private createContextWithClientId(existingContext?: HttpContext): HttpContext {
        const context = existingContext || new HttpContext();
        return context.set(this.clientContextToken, 'default');
    }

    getCurrentUser(observe?: 'body', options?: RequestOptions<'json'>): Observable<UserDto>;
    getCurrentUser(observe?: 'response', options?: RequestOptions<'json'>): Observable<HttpResponse<UserDto>>;
    getCurrentUser(observe?: 'events', options?: RequestOptions<'json'>): Observable<HttpEvent<UserDto>>;
    getCurrentUser(observe?: 'body' | 'events' | 'response', options?: RequestOptions<'arraybuffer' | 'blob' | 'json' | 'text'>): Observable<any> {
        const url = `${this.basePath}/api/users/me`;

        const requestOptions: any = {
            observe: observe as any,
            responseType: 'json',
            reportProgress: options?.reportProgress,
            withCredentials: options?.withCredentials,
            context: this.createContextWithClientId(options?.context)
        };

        return this.httpClient.get(url, requestOptions);
    }

    getUserById(id: number, observe?: 'body', options?: RequestOptions<'json'>): Observable<UserDto>;
    getUserById(id: number, observe?: 'response', options?: RequestOptions<'json'>): Observable<HttpResponse<UserDto>>;
    getUserById(id: number, observe?: 'events', options?: RequestOptions<'json'>): Observable<HttpEvent<UserDto>>;
    getUserById(id: number, observe?: 'body' | 'events' | 'response', options?: RequestOptions<'arraybuffer' | 'blob' | 'json' | 'text'>): Observable<any> {
        const url = `${this.basePath}/api/users/${id}`;

        const requestOptions: any = {
            observe: observe as any,
            responseType: 'json',
            reportProgress: options?.reportProgress,
            withCredentials: options?.withCredentials,
            context: this.createContextWithClientId(options?.context)
        };

        return this.httpClient.get(url, requestOptions);
    }

    updateProfile(userDto: UserDto, observe?: 'body', options?: RequestOptions<'json'>): Observable<UserDto>;
    updateProfile(userDto: UserDto, observe?: 'response', options?: RequestOptions<'json'>): Observable<HttpResponse<UserDto>>;
    updateProfile(userDto: UserDto, observe?: 'events', options?: RequestOptions<'json'>): Observable<HttpEvent<UserDto>>;
    updateProfile(userDto: UserDto, observe?: 'body' | 'events' | 'response', options?: RequestOptions<'arraybuffer' | 'blob' | 'json' | 'text'>): Observable<any> {
        const url = `${this.basePath}/api/users/me`;

        const requestOptions: any = {
            observe: observe as any,
            responseType: 'json',
            reportProgress: options?.reportProgress,
            withCredentials: options?.withCredentials,
            context: this.createContextWithClientId(options?.context)
        };

        return this.httpClient.put(url, userDto, requestOptions);
    }

    updatePhoto(file: File, observe?: 'body', options?: RequestOptions<'json'>): Observable<UserDto>;
    updatePhoto(file: File, observe?: 'response', options?: RequestOptions<'json'>): Observable<HttpResponse<UserDto>>;
    updatePhoto(file: File, observe?: 'events', options?: RequestOptions<'json'>): Observable<HttpEvent<UserDto>>;
    updatePhoto(file: File, observe?: 'body' | 'events' | 'response', options?: RequestOptions<'arraybuffer' | 'blob' | 'json' | 'text'>): Observable<any> {
        const url = `${this.basePath}/api/users/me/photo`;

        const formData = new FormData();
        formData.append('file', file);

        const requestOptions: any = {
            observe: observe as any,
            responseType: 'json',
            reportProgress: options?.reportProgress,
            withCredentials: options?.withCredentials,
            context: this.createContextWithClientId(options?.context)
        };

        return this.httpClient.post(url, formData, requestOptions);
    }
}
