import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { HomePage } from './home-page';

describe('HomePage', () => {
  it('muestra error y permite recuperar la conexión al reintentar', async () => {
    TestBed.configureTestingModule({
      imports: [HomePage],
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });
    const http = TestBed.inject(HttpTestingController);
    const fixture = TestBed.createComponent(HomePage);
    fixture.detectChanges();
    expect(fixture.nativeElement.textContent).toContain('Comprobando conexión');
    http.expectOne('/api/status').flush({}, { status: 503, statusText: 'Service Unavailable' });
    await fixture.whenStable();
    expect(fixture.nativeElement.textContent).toContain('No se pudo comprobar');
    fixture.nativeElement.querySelector('button').click();
    fixture.detectChanges();
    http.expectOne('/api/status').flush({ application: 'reservas-laboratorios', status: 'UP', database: 'UP' });
    await fixture.whenStable();
    expect(fixture.nativeElement.textContent).toContain('Backend conectado');
    http.verify();
  });
});
