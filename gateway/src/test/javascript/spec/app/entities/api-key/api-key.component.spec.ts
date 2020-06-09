import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GatewayTestModule } from '../../../test.module';
import { ApiKeyComponent } from 'app/entities/api-key/api-key.component';
import { ApiKeyService } from 'app/entities/api-key/api-key.service';
import { ApiKey } from 'app/shared/model/api-key.model';

describe('Component Tests', () => {
  describe('ApiKey Management Component', () => {
    let comp: ApiKeyComponent;
    let fixture: ComponentFixture<ApiKeyComponent>;
    let service: ApiKeyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [ApiKeyComponent],
      })
        .overrideTemplate(ApiKeyComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApiKeyComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ApiKeyService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ApiKey(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.apiKeys && comp.apiKeys[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
