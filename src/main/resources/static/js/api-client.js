const ApiClient = (() => {
  const DEFAULT_HEADERS = { 'Content-Type': 'application/json' };
  let csrfToken = null;
  let csrfHeaderName = 'X-CSRF-TOKEN';

  async function fetchCsrf() {
    const response = await fetch('/api/csrf', { credentials: 'include' });
    if (!response.ok) {
      throw new Error('No se pudo obtener el token CSRF');
    }
    const data = await response.json();
    csrfToken = data.token;
    csrfHeaderName = data.headerName || 'X-CSRF-TOKEN';
    return csrfToken;
  }

  async function ensureCsrf() {
    if (csrfToken) return csrfToken;
    return fetchCsrf();
  }

  function buildErrorMessage(status, body) {
    if (body && body.message) {
      return body.message;
    }
    if (status === 401) return 'Credenciales inválidas o sesión expirada.';
    if (status === 403) return 'No autorizado. CSRF inválido o sesión vencida.';
    if (status === 400 || status === 422) return 'Datos inválidos. Revisá los campos.';
    return 'Ocurrió un error inesperado. Intentalo nuevamente.';
  }

  async function request(method, url, body, requireCsrf = false) {
    const headers = { ...DEFAULT_HEADERS };
    if (requireCsrf) {
      await ensureCsrf();
      headers[csrfHeaderName] = csrfToken;
    }

    const response = await fetch(url, {
      method,
      credentials: 'include',
      headers,
      body: body ? JSON.stringify(body) : undefined,
    });

    if (response.status === 204) {
      return null;
    }

    let payload = null;
    const contentType = response.headers.get('content-type') || '';
    if (contentType.includes('application/json')) {
      payload = await response.json();
    }

    if (!response.ok) {
      const error = new Error(buildErrorMessage(response.status, payload));
      error.status = response.status;
      error.payload = payload;
      throw error;
    }

    return payload;
  }

  return {
    get: (url) => request('GET', url),
    post: (url, body) => request('POST', url, body, true),
    put: (url, body) => request('PUT', url, body, true),
    del: (url, body) => request('DELETE', url, body, true),
    ensureCsrf,
  };
})();
