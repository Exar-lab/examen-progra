<div align="center">

<h1>🚌 Progra Bus Tickets</h1>

<p>
  <strong>Sistema de venta de pasajes para una empresa de buses de Centroamérica con sede en Costa Rica.</strong>
</p>

<p>
  <img alt="Java 25" src="https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk">
  <img alt="Spring Boot" src="https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img alt="Maven" src="https://img.shields.io/badge/Maven-Wrapper-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white">
  <img alt="Architecture" src="https://img.shields.io/badge/Architecture-Hexagonal-blueviolet?style=for-the-badge">
</p>

<table>
  <tr>
    <td align="center">🌎<br><strong>Rutas internacionales</strong><br>Servicios entre países centroamericanos</td>
    <td align="center">🎫<br><strong>Código único</strong><br>Cada tiquete genera un código de viaje</td>
    <td align="center">📄<br><strong>Comprobante PDF</strong><br>Recibo electrónico posterior a la compra</td>
  </tr>
</table>

</div>

---

## 📌 Descripción

**Progra Bus Tickets** es un proyecto académico construido con **Spring Boot 4**, **Java 25** y **Maven** para modelar un sistema de venta de pasajes de bus.

La aplicación debe permitir que clientes registrados compren tiquetes para servicios programados entre países. Después de la compra, cada tiquete obtiene un **código único inventado** que se usa para viajar y el sistema emite un **comprobante electrónico en PDF** con los datos del pasaje.

El diseño del proyecto sigue **Arquitectura Hexagonal (Ports and Adapters)** para mantener el dominio aislado de detalles técnicos como controladores web, persistencia, seguridad o generación de PDF.

---

## 🕒 Horarios y rutas de referencia

Estos son los servicios base del enunciado para precargar y validar rutas, horarios y precios:

<table>
  <thead>
    <tr>
      <th align="center">Ruta</th>
      <th align="center">Horario</th>
      <th align="center">Precio</th>
    </tr>
  </thead>
  <tbody>
    <tr><td><strong>🇨🇷 CR → 🇳🇮 NI</strong></td><td><strong>3 AM</strong></td><td><strong>$80</strong></td></tr>
    <tr><td><strong>🇨🇷 CR → 🇸🇻 ES</strong></td><td><strong>6 AM</strong></td><td><strong>$120</strong></td></tr>
    <tr><td><strong>🇨🇷 CR → 🇬🇹 GUA</strong></td><td><strong>6 AM</strong></td><td><strong>$140</strong></td></tr>
    <tr><td><strong>🇨🇷 CR → 🇳🇮 NI</strong></td><td><strong>6 AM</strong></td><td><strong>$80</strong></td></tr>
    <tr><td><strong>🇨🇷 CR → 🇭🇳 HN</strong></td><td><strong>6 AM</strong></td><td><strong>$110</strong></td></tr>
    <tr><td><strong>🇨🇷 CR → 🇵🇦 PN</strong></td><td><strong>5 AM</strong></td><td><strong>$80</strong></td></tr>
    <tr><td><strong>🇵🇦 PN → 🇨🇷 CR</strong></td><td><strong>8 AM</strong></td><td><strong>$80</strong></td></tr>
    <tr><td><strong>🇬🇹 GUA → 🇨🇷 CR</strong></td><td><strong>5 AM</strong></td><td><strong>$140</strong></td></tr>
    <tr><td><strong>🇬🇹 GUA → 🇸🇻 ES</strong></td><td><strong>5 AM</strong></td><td><strong>$80</strong></td></tr>
    <tr><td><strong>🇬🇹 GUA → 🇭🇳 HN</strong></td><td><strong>5 AM</strong></td><td><strong>$110</strong></td></tr>
    <tr><td><strong>🇬🇹 GUA → 🇳🇮 NI</strong></td><td><strong>5 AM</strong></td><td><strong>$120</strong></td></tr>
    <tr><td><strong>🇳🇮 NI → 🇨🇷 CR</strong></td><td><strong>3 AM</strong></td><td><strong>$80</strong></td></tr>
    <tr><td><strong>🇳🇮 NI → 🇨🇷 CR</strong></td><td><strong>6 AM</strong></td><td><strong>$80</strong></td></tr>
  </tbody>
</table>

> Leyenda: CR = Costa Rica, NI = Nicaragua, ES = El Salvador, GUA = Guatemala, HN = Honduras, PN = Panamá.

---

## 🧾 Enunciado resumido

- Una empresa de buses de Centroamérica, con sede en Costa Rica, necesita un sistema de venta de pasajes.
- Los clientes reciben un código único por tiquete después de comprar.
- Existen rutas y horarios de servicios entre países, con montos diferentes.
- Solo se pueden comprar pasajes para servicios de máximo una semana hacia adelante.
- Una persona no puede comprar más de 5 pasajes.
- Para comprar, el usuario debe estar registrado o registrarse con sus datos personales y de tarjeta.
- El sistema debe manejar sesiones para restringir el acceso a personas registradas.
- Después de comprar, se debe emitir un comprobante electrónico en PDF.
- La base de datos debe venir precargada con datos iniciales.

---

## 🧭 Reglas de negocio principales

| Regla | Descripción |
| --- | --- |
| Código de tiquete | Cada tiquete debe tener un código único generado por el sistema. |
| Ventana de compra | `fechaCompra <= salidaServicio <= fechaCompra + 7 días`. |
| Límite de pasajes | Una persona no puede comprar más de 5 pasajes. |
| Registro obligatorio | Solo usuarios registrados pueden comprar. |
| Sesiones | El acceso protegido se controla con sesión de usuario registrado. |
| PDF | La compra genera un comprobante electrónico con código, ruta, horario y datos del pasaje. |
| Datos precargados | La base debe incluir datos iniciales de rutas, servicios u otros catálogos necesarios. |

---

## 🏗️ Arquitectura

El proyecto usa **Arquitectura Hexagonal** organizada por feature/dominio:

```text
src/main/java/com/buses/examen/Progra/
  <feature>/
    domain/                 # Entidades, value objects, enums e invariantes
    application/            # Casos de uso y orquestación
      port/in/              # Interfaces de entrada
      port/out/             # Puertos de salida
    adapter/
      in/web/               # Controllers y DTOs web
      out/persistence/      # JPA/Spring Data y mappers de persistencia
      out/pdf/              # Generación concreta de comprobantes PDF
      out/security/         # Adaptadores de sesión/seguridad cuando aplique
    exception/              # Excepciones específicas
  config/                   # Configuración compartida de Spring
```

### Modelo de capas tradicional

| Nombre común | Ubicación esperada | Regla |
| --- | --- | --- |
| `model` / entidad | `domain/` | Define datos e invariantes del negocio. |
| `repository` | `application/port/out/` + `adapter/out/persistence/` | La aplicación depende de interfaces, no de JPA concreto. |
| `service` | `application/` | Orquesta casos de uso y transacciones. |
| `dto` | `adapter/in/web/` o command/result de aplicación | No debe filtrarse al dominio. |
| `controller` | `adapter/in/web/` | Traduce HTTP a casos de uso; no posee reglas de negocio. |

> La regla central: **el dominio no depende de frameworks**. Los adapters dependen del core, no al revés.

---

## 🧰 Stack técnico

- **Java 25**
- **Spring Boot 4.0.6**
- **Maven Wrapper**
- **Spring Data JPA**
- **Spring Security / OAuth2 Authorization Server dependencies**
- **Bean Validation**
- **MySQL runtime connector**
- **JUnit 5 + AssertJ** para pruebas

---

## 🚀 Comandos útiles

```bash
# Ejecutar la aplicación
./mvnw spring-boot:run

# Compilar solamente
./mvnw compile

# Ejecutar todas las pruebas
./mvnw test

# Ejecutar una clase de prueba específica
./mvnw test -Dtest=MyServiceTest

# Empaquetar sin pruebas
./mvnw package -DskipTests
```

> No ejecutar `./mvnw` sin objetivo. Usá siempre un goal específico.

---

## 🧪 Testing

El proyecto prioriza pruebas enfocadas:

- `@DataJpaTest` para persistencia y restricciones de JPA.
- `@WebMvcTest` para controllers cuando existan adapters web.
- `@SpringBootTest` solo para integración real o carga de contexto.

---

## 🚌 Visión del producto

El sistema debe sentirse como una terminal digital de buses: rutas claras, horarios disponibles, compra controlada, código de viaje único y comprobante electrónico listo para el pasajero.

El foco técnico es mantener un código limpio, entendible y preparado para crecer sin mezclar reglas de negocio con detalles de infraestructura.
