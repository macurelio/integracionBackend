-- Script de inserción de datos de prueba para la base de datos H2
-- Puedes ejecutar este script en la consola H2 o dejarlo para carga automática si está configurado

INSERT INTO USER (id, name, email, password, created, modified, last_login, token, is_active)
VALUES
  ('11111111-1111-1111-1111-111111111111', 'Juan Rodriguez', 'juan@dominio.cl', 'hunter2password', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'token1', true),
  ('22222222-2222-2222-2222-222222222222', 'Maria Perez', 'maria@dominio.cl', 'password123', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'token2', true);

INSERT INTO PHONE (id, number, citycode, contrycode, user_id)
VALUES
  (1, '1234567', '1', '57', '11111111-1111-1111-1111-111111111111'),
  (2, '7654321', '2', '56', '22222222-2222-2222-2222-222222222222');

