import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { SystemService } from './system.service';

describe('SystemService', () => {
  let service: SystemService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(SystemService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('consulta el contrato de estado en /api/status', () => {
    const response = { application: 'reservas-laboratorios', status: 'UP', database: 'UP' };
    service.getStatus().subscribe(value => expect(value).toEqual(response));
    const request = http.expectOne('/api/status');
    expect(request.request.method).toBe('GET');
    request.flush(response);
  });

  it('convierte una falla de conexión en un mensaje para la página', () => {
    service.getStatus().subscribe({
      next: () => { throw new Error('Se esperaba un error HTTP'); },
      error: error => expect(error.message).toContain('backend esté iniciado')
    });
    http.expectOne('/api/status').flush({}, { status: 503, statusText: 'Service Unavailable' });
  });
});
