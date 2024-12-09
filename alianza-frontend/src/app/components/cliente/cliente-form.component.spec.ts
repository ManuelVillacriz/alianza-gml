import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { ClienteFormComponent } from './cliente-form.component';
import { ClienteService } from '../../services/cliente.service';

// Mock del servicio ClienteService
const mockClienteService = {
  crear: jasmine.createSpy('crear').and.returnValue(of({ id: '123', name: 'Juan', sharedKey: 'juanperez' })),
};

// Mock de ActivatedRoute para resolver el NullInjectorError
const mockActivatedRoute = {
  snapshot: {
    params: { id: '123' },
    queryParams: {},
  },
  paramMap: {
    get: (key: string) => null,
  },
};

describe('ClienteFormComponent - save', () => {
  let component: ClienteFormComponent;
  let fixture: ComponentFixture<ClienteFormComponent>;
  let clienteService: ClienteService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ClienteFormComponent],
      imports: [
        MatIconModule,
        FormsModule,
      ],
      providers: [
        { provide: ClienteService, useValue: mockClienteService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClienteFormComponent);
    component = fixture.componentInstance;
    clienteService = TestBed.inject(ClienteService);

    // Resetear el espía entre cada prueba
    mockClienteService.crear.calls.reset();

    // Asignar datos de prueba directamente al modelo
    const clienteMock = {
      name: 'Juan',
      lastName: 'Perez',
      email: 'juan.perez@example.com',
      startDate: new Date(),
      endDate: new Date(Date.now() + 100000),
    };
    component.model = clienteMock; // Asegurar el modelo para la prueba
  });

  fit('should save a client successfully', () => {
    // Ejecutar el método crear()
    component.crear();

    // Verificar que se ha llamado el servicio
    expect(mockClienteService.crear).toHaveBeenCalledTimes(1);
    expect(mockClienteService.crear).toHaveBeenCalledWith(component.model);
  });

  fit('should handle a failed save scenario', () => {
    // Simular un error
    mockClienteService.crear.and.returnValue(throwError(() => new Error('Error de prueba')));

    // Ejecutar el método crear()
    component.crear();

    // Verificar que el espía fue llamado
    expect(mockClienteService.crear).toHaveBeenCalledTimes(1);
  });
});
