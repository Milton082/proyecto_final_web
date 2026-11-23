import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { SystemService } from '../../services/system.service';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.html',
  styleUrl: './home-page.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class HomePage {
  private readonly systemService = inject(SystemService);
  protected readonly statusResource = rxResource({
    stream: () => this.systemService.getStatus()
  });
}
