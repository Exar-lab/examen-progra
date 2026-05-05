INSERT INTO pais (id, codigo_iso, nombre) VALUES (1, 'PE', 'Peru');
INSERT INTO ciudad (id, pais_id, nombre, codigo) VALUES (1, 1, 'Lima', 'LIM');
INSERT INTO ciudad (id, pais_id, nombre, codigo) VALUES (2, 1, 'Cusco', 'CUS');

INSERT INTO compania (id, nombre_comercial, ruc_nit, activa) VALUES (1, 'Buses Sur', '20123456789', true);
INSERT INTO bus (id, compania_id, placa, modelo, capacidad_total, activo) VALUES (1, 1, 'ABC-123', 'Volvo 9700', 4, true);

INSERT INTO asiento (id, bus_id, numero, piso, categoria, activo) VALUES (1, 1, '1', 1, 'REGULAR', true);
INSERT INTO asiento (id, bus_id, numero, piso, categoria, activo) VALUES (2, 1, '2', 1, 'REGULAR', true);
INSERT INTO asiento (id, bus_id, numero, piso, categoria, activo) VALUES (3, 1, '3', 1, 'REGULAR', true);
INSERT INTO asiento (id, bus_id, numero, piso, categoria, activo) VALUES (4, 1, '4', 1, 'REGULAR', true);

INSERT INTO ruta (id, ciudad_origen_id, ciudad_destino_id, duracion_minutos, distancia_km, activa) VALUES (1, 1, 2, 90, 120, true);
INSERT INTO servicio (id, ruta_id, bus_id, salida_programada, llegada_programada, precio_base, estado, capacidad_disponible)
VALUES (1, 1, 1, TIMESTAMPADD('DAY', 3, CURRENT_TIMESTAMP), TIMESTAMPADD('DAY', 3, TIMESTAMPADD('HOUR', 2, CURRENT_TIMESTAMP)), 50, 'PROGRAMADO', 4);
