INSERT INTO producto (ID, CODIGO,NOMBRE, PRECIO) 
VALUES (10, 'PROD_001', 'COMPUTADOR', 1200000);
INSERT INTO producto (ID, CODIGO,NOMBRE, PRECIO) 
VALUES (20, 'PROD_002', 'TECLADO', 10000);
INSERT INTO producto (ID, CODIGO,NOMBRE, PRECIO) 
VALUES (30, 'PROD_003', 'MOUSE', 200000);
INSERT INTO producto (ID, CODIGO,NOMBRE, PRECIO)
VALUES (40, 'PROD_004', 'CPU', 1300000);
INSERT INTO producto (ID, CODIGO,NOMBRE, PRECIO)
VALUES (41, 'F01TSA0150', 'CPU', 1300000);
INSERT INTO producto (ID, CODIGO,NOMBRE, PRECIO)
VALUES (42, 'F01TSA0151', 'CPU', 30000);
INSERT INTO producto (ID, CODIGO,NOMBRE, PRECIO)
VALUES (50, 'PROD_AEI', 'CPU', 1300000);
INSERT INTO producto (ID, CODIGO,NOMBRE, PRECIO)
VALUES (51, 'F1O8A06I', 'CPU', 130000);

INSERT INTO GARANTIA_EXTENDIDA (
ID,FECHA_FIN_GARANTIA,FECHA_SOLICITUD_GARANTIA,NOMBRE_CLIENTE,PRECIO,ID_PRODUCTO)
VALUES(100, NULL, NULL, 'Felipe', 240000, 10)