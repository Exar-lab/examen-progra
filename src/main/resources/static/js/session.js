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
  };
})();
