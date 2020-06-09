import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../test.module';
import { ApiKeyUpdateComponent } from 'app/entities/api-key/api-key-update.component';
import { ApiKeyService } from 'app/entities/api-key/api-key.service';
import { ApiKey } from 'app/shared/model/api-key.model';

describe('Component Tests', () => {
  describe('ApiKey Management Update Component', () => {
    let comp: ApiKeyUpdateComponent;
    let fixture: ComponentFixture<ApiKeyUpdateComponent>;
    let service: ApiKeyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [ApiKeyUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ApiKeyUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApiKeyUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ApiKeyService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ApiKey(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ApiKey();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
