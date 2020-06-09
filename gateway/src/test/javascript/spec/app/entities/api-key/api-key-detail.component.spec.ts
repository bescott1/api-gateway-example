import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../test.module';
import { ApiKeyDetailComponent } from 'app/entities/api-key/api-key-detail.component';
import { ApiKey } from 'app/shared/model/api-key.model';

describe('Component Tests', () => {
  describe('ApiKey Management Detail Component', () => {
    let comp: ApiKeyDetailComponent;
    let fixture: ComponentFixture<ApiKeyDetailComponent>;
    const route = ({ data: of({ apiKey: new ApiKey(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [ApiKeyDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ApiKeyDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ApiKeyDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load apiKey on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.apiKey).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
