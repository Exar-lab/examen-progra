const Session = (() => {
  const STORAGE_KEY = 'centrobus_session';
  let cached = null;

  function load() {
    if (cached) return cached;
    const raw = localStorage.getItem(STORAGE_KEY);
    cached = raw ? JSON.parse(raw) : null;
    return cached;
  }

  function save(session) {
    cached = session;
    if (session) {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(session));
    } else {
      localStorage.removeItem(STORAGE_KEY);
    }
  }

  function isLoggedIn() {
    return !!load();
  }

  async function syncWithServer() {
    if (!load()) {
      return null;
    }
    try {
      const me = await ApiClient.get('/api/auth/me');
      const currentSession = load();
      save({
        id: me.clienteId,
        nombre: currentSession?.nombre || me.username,
        correo: currentSession?.correo || me.username,
      });
      return me;
    } catch (error) {
      if (error.status === 401 || error.status === 403) {
        save(null);
      }
      return null;
    }
  }

  async function requireAuthenticated(redirectTo = 'login.html') {
    if (!isLoggedIn()) {
      window.location.href = redirectTo;
      return false;
    }
    const me = await syncWithServer();
    if (!me) {
      window.location.href = redirectTo;
      return false;
    }
    return true;
  }

  function current() {
    return load();
  }

  function logout() {
    save(null);
    window.location.href = 'index.html';
  }

  return {
    load,
    save,
    isLoggedIn,
    current,
    logout,
    syncWithServer,
    requireAuthenticated,
  };
})();
