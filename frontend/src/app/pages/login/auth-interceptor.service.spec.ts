import { TestBed } from '@angular/core/testing';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { authInterceptor } from './auth-interceptor.service';

describe('AuthInterceptor', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {
          provide: HTTP_INTERCEPTORS,
          useValue: authInterceptor, // Provide the interceptor function
          multi: true
        }
      ]
    });
  });

  it('should be defined', () => {
    expect(authInterceptor).toBeDefined();
  });
});
