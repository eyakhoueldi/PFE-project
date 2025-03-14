import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role'); // Retrieve role from localStorage

  if (token) {
    const headers: { [key: string]: string } = {
      Authorization: `Bearer ${token}`
    };

    // Optionally add role to headers if needed by backend
    if (role) {
      headers['X-User-Role'] = role;
    }

    const clonedReq = req.clone({
      setHeaders: headers
    });
    return next(clonedReq);
  }

  return next(req);
};