// ============================================================
//  MOCK DATA — Reemplazar fetch() con llamadas reales al backend
//  cuando los endpoints estén listos
// ============================================================

const PAISES = {
  CR: { nombre: "Costa Rica", bandera: "🇨🇷" },
  NI: { nombre: "Nicaragua", bandera: "🇳🇮" },
  ES: { nombre: "El Salvador", bandera: "🇸🇻" },
  GUA: { nombre: "Guatemala", bandera: "🇬🇹" },
  HN: { nombre: "Honduras", bandera: "🇭🇳" },
  PN: { nombre: "Panamá", bandera: "🇵🇦" },
};

const RUTAS = [
  { id: 1, origen: "CR", destino: "NI", horario: "03:00", precio: 80, duracionHoras: 8 },
  { id: 2, origen: "CR", destino: "ES", horario: "06:00", precio: 120, duracionHoras: 14 },
  { id: 3, origen: "CR", destino: "GUA", horario: "06:00", precio: 140, duracionHoras: 18 },
  { id: 4, origen: "CR", destino: "NI", horario: "06:00", precio: 80, duracionHoras: 8 },
  { id: 5, origen: "CR", destino: "HN", horario: "06:00", precio: 110, duracionHoras: 12 },
  { id: 6, origen: "CR", destino: "PN", horario: "05:00", precio: 80, duracionHoras: 7 },
  { id: 7, origen: "PN", destino: "CR", horario: "08:00", precio: 80, duracionHoras: 7 },
  { id: 8, origen: "GUA", destino: "CR", horario: "05:00", precio: 140, duracionHoras: 18 },
  { id: 9, origen: "GUA", destino: "ES", horario: "05:00", precio: 80, duracionHoras: 4 },
  { id: 10, origen: "GUA", destino: "HN", horario: "05:00", precio: 110, duracionHoras: 6 },
  { id: 11, origen: "GUA", destino: "NI", horario: "05:00", precio: 120, duracionHoras: 10 },
  { id: 12, origen: "NI", destino: "CR", horario: "03:00", precio: 80, duracionHoras: 8 },
  { id: 13, origen: "NI", destino: "CR", horario: "06:00", precio: 80, duracionHoras: 8 },
];

// Genera servicios disponibles para los próximos 7 días
function generarServicios() {
  const servicios = [];
  const hoy = new Date();

  RUTAS.forEach((ruta) => {
    for (let dia = 0; dia < 7; dia++) {
      const fecha = new Date(hoy);
      fecha.setDate(hoy.getDate() + dia);
      const [h, m] = ruta.horario.split(":").map(Number);
      fecha.setHours(h, m, 0, 0);

      // Solo agregar si la salida es en el futuro
      if (fecha > new Date()) {
        servicios.push({
          id: ruta.id * 100 + dia,
          rutaId: ruta.id,
          ruta,
          fechaSalida: new Date(fecha),
          asientosDisponibles: Math.floor(Math.random() * 30) + 5,
          precio: ruta.precio,
        });
      }
    }
  });

  return servicios;
}

const SERVICIOS = generarServicios();

// Usuario en sesión (null = no logueado)
let SESSION_USER = JSON.parse(localStorage.getItem("session_user")) || null;

// Compras realizadas en esta sesión
let COMPRAS = JSON.parse(localStorage.getItem("compras")) || [];

// ── Helpers ────────────────────────────────────────────────

function getRutaLabel(ruta) {
  const o = PAISES[ruta.origen];
  const d = PAISES[ruta.destino];
  return `${o.bandera} ${o.nombre} → ${d.bandera} ${d.nombre}`;
}

function formatFecha(date) {
  return new Intl.DateTimeFormat("es-CR", {
    weekday: "short", day: "numeric", month: "short", year: "numeric",
  }).format(new Date(date));
}

function formatHora(date) {
  return new Intl.DateTimeFormat("es-CR", {
    hour: "2-digit", minute: "2-digit", hour12: true,
  }).format(new Date(date));
}

function generarCodigoTicket() {
  const chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
  let code = "BUS-";
  for (let i = 0; i < 8; i++) {
    code += chars[Math.floor(Math.random() * chars.length)];
  }
  return code;
}

function isLoggedIn() {
  return SESSION_USER !== null;
}

function logout() {
  SESSION_USER = null;
  localStorage.removeItem("session_user");
  window.location.href = "index.html";
}

function guardarSesion(usuario) {
  SESSION_USER = usuario;
  localStorage.setItem("session_user", JSON.stringify(usuario));
}

function ticketsComprados() {
  return COMPRAS.reduce((total, c) => total + c.tickets.length, 0);
}
