// Helpers de formato para el frontend
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
