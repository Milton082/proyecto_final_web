import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { ApiStatus } from '../interfaces/api-status';

@Injectable({ providedIn: 'root' })
export class SystemService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = environment.apiUrl;

  getStatus(): Observable<ApiStatus> {
    return this.http.get<ApiStatus>(`${this.baseUrl}/status`).pipe(
      catchError(() => throwError(() => new Error(
        'No se pudo comprobar la conexión. Verifica que el backend esté iniciado.'
      )))
    );
  }
}
