import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Services } from './services.service'; // Correct import

describe('Services', () => { // Match the class name 'Services'
    let service: Services;
    let httpMock: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule],
            providers: [Services]
        });
        service = TestBed.inject(Services);
        httpMock = TestBed.inject(HttpTestingController);
    });

    afterEach(() => {
        httpMock.verify(); // Ensure no outstanding requests
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });

    it('should fetch data with GET request', () => {
        const testData = [{ id: '1', name: 'Test User' }];
        service.get<any>('admin/users').subscribe((data) => {
            expect(data).toEqual(testData);
        });

        const req = httpMock.expectOne('http://localhost:8080/api/admin/users');
        expect(req.request.method).toBe('GET');
        req.flush(testData);
    });

    it('should handle 401 error on GET request', () => {
        service.get<any>('admin/users').subscribe({
            next: () => fail('Should have failed with 401'),
            error: (error) => {
                expect(error.message).toBe('Unauthorized: Please log in again.');
            }
        });

        const req = httpMock.expectOne('http://localhost:8080/api/admin/users');
        req.flush('Unauthorized', { status: 401, statusText: 'Unauthorized' });
    });

    it('should perform POST request', () => {
        const testBody = { status: 'DEACTIVATED' };
        service.post<any>('admin/users/1/deactivate', testBody).subscribe((response) => {
            expect(response).toBeNull(); // Adjust based on your backend response
        });

        const req = httpMock.expectOne('http://localhost:8080/api/admin/users/1/deactivate');
        expect(req.request.method).toBe('POST');
        req.flush(null);
    });
});