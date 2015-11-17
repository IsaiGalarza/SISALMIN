--
-- JBoss, Home of Professional Open Source
-- Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
-- contributors by the @authors tag. See the copyright.txt in the
-- distribution for a full listing of individual contributors.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
-- http://www.apache.org/licenses/LICENSE-2.0
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

--PRODUCCION --
-- Deafault

-- ----------------------------
-- Records of usuario
-- ----------------------------
INSERT INTO "public"."usuario" VALUES (1, 'super.super@gmail.com', '2015-01-01 00:00:00', null, 'DEMO', 'Mauricio Bejarano', 'DEMO', 0, 'SU', 'super');

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO "public"."roles" VALUES (1, '2015-01-01 00:00:00', 'superusuario', 'SU', 'super');
INSERT INTO "public"."rol" (id,estado,fecha_modificacion,fecha_registro,nombre,usuario_registro) VALUES (1,'SU',null,'2015-01-01 00:00:00','superusuario','super');

-- ----------------------------
-- Records of usuario_rol
-- ----------------------------
INSERT INTO "public"."usuario_rol" VALUES (1, 1, 1);
INSERT INTO "public"."usuario_rolv1" (id,estado,fecha_modificacion,fecha_registro,usuario_registro,descripcion,id_rol,id_usuario) VALUES (1,'AC',null,'2015-01-01 00:00:00','super','Grupo de Usuario Super usuarios',1,1);

-- ----------------------------
-- Records of modulo
-- ----------------------------
INSERT INTO "public"."modulo" (id,nombre,id_modulo_padre) VALUES (1,'SEGURIDAD',null);
INSERT INTO "public"."modulo" (id,nombre,id_modulo_padre) VALUES (2,'PARAMETRIZACION',null);
INSERT INTO "public"."modulo" (id,nombre,id_modulo_padre) VALUES (3,'CONTABILIDAD',null);
INSERT INTO "public"."modulo" (id,nombre,id_modulo_padre) VALUES (4,'COMPRA',null);
INSERT INTO "public"."modulo" (id,nombre,id_modulo_padre) VALUES (5,'VENTA',null);
INSERT INTO "public"."modulo" (id,nombre,id_modulo_padre) VALUES (6,'REPORTE',null);
INSERT INTO "public"."modulo" (id,nombre,id_modulo_padre) VALUES (7,'LIBRO',6);
INSERT INTO "public"."modulo" (id,nombre,id_modulo_padre) VALUES (8,'ESTADO FINANCIERO',6);
INSERT INTO "public"."modulo" (id,nombre,id_modulo_padre) VALUES (9,'CUADRO ACTIVO FIJO',6);

-- ----------------------------
-- Records of accion
-- ----------------------------
INSERT INTO "public"."accion" (id,nombre) VALUES (1,'REGISTRAR');
INSERT INTO "public"."accion" (id,nombre) VALUES (2,'MODIFICAR');
INSERT INTO "public"."accion" (id,nombre) VALUES (3,'ELIMINAR');
INSERT INTO "public"."accion" (id,nombre) VALUES (4,'PROCESAR');
INSERT INTO "public"."accion" (id,nombre) VALUES (5,'ANULAR COMPROBANTE');

-- ----------------------------
-- Records of pagina
-- ----------------------------
INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (1,'USUARIO','usuario.xhtml',1);
INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (1,'ROL','rol.xhtml',1);
INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (2,'PERMISO','permiso.xhtml',1);

INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (3,'EMPRESA','empresa.xhtml',2);
INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (4,'SUCURSAL','sucursal.xhtml',2);
INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (5,'TIPO COMPROBANTE','tipo_comprobante.xhtml',2);
INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (6,'TIPO CAMBIO','tipo_cambio.xhtml',2);
INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (7,'TIPO CAMIO UFV','tipo_ufv.xhtml',2);
INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (8,'CENTRO COSTO','centro-costo.xhtml',2);
INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (9,'CUENTA BANCARIA','cuenta_bancaria.xhtml',2) ;
INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (10,'GRUPO DE IMPUESTO','grupo_impuesto.xhtml',2);
INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (11,'PARAMETRIZACION','parametrizacion.xhtml',2);

INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (12,'COMPROBANTE','comprobante_index.xhtml',3) ;
INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (13,'PLAN DE CUENTA','plan_cuenta.xhtml',3);
INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (14,'APERTURA Y CIERRE','apertura_cierre.xhtml',3);

INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (15,'PROVEEDORES','proveedores.xhtml',4) ;
INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (16,'ORDEN DE COMPRA','orden_compra.xhtml',4);

INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (17,'CLIENTES','clientes.xhtml',5) ;
INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (18,'COTIZACION','cotizacion_index.xhtml',5);
INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (19,'SERVICIOS','servicios.xhtml',5);

INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (20,'LIBRO DIARIO','libro_diario.xhtml',7) ;
INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (21,'LIBRO MAYOR','libro_mayor.xhtml',7) ;
INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (22,'SUMAS Y SALDOS','sumas_saldos.xhtml',7) ;

INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (23,'BALANCE GENERAL','balance_general.xhtml',8) ;
INSERT INTO pagina (id,nombre,path,id_modulo) VALUES (24,'ESTADO RESULTADO','estado_resultado.xhtml',8) ;

-- ----------------------------
-- Records of detalle_pagina
-- ----------------------------

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (1,1,1);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (2,2,1);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (3,3,1);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (4,1,2);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (5,2,2);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (6,3,2);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (7,1,3);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (8,2,3);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (9,3,3);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (10,1,4);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (11,2,4);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (12,3,4);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (13,1,5);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (14,2,5);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (15,3,5);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (16,1,6);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (17,2,6);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (18,3,6);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (19,1,7);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (20,2,7);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (21,3,7);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (22,1,8);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (23,2,8);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (24,3,8);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (25,1,9);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (26,2,9);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (27,3,9);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (28,1,10);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (29,2,10);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (30,3,10);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (31,1,11);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (32,2,11);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (33,3,11);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (34,1,12);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (35,2,12);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (36,3,12);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (37,1,13);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (38,2,13);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (39,3,13);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (40,1,14);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (41,2,14);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (42,3,14);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (43,1,15);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (44,2,15);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (45,3,15);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (46,1,16);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (47,2,16);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (48,3,16);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (49,1,17);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (50,2,17);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (51,3,17);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (52,1,18);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (53,2,18);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (54,3,18);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (55,1,19);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (56,2,19);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (57,3,19);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (58,1,20);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (59,2,20);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (60,3,20);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (61,1,21);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (62,2,21);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (63,3,21);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (64,1,22);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (65,2,22);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (66,3,22);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (67,1,23);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (68,2,23);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (69,3,23);

INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (70,1,24);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (71,2,24);
INSERT INTO detalle_pagina (id,id_accion, id_pagina) VALUES  (72,3,24);


-- ----------------------------
-- Records of permiso_v1
-- ----------------------------
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (1,'AC',null,'2015-01-01 00:00:00','super',1,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (2,'AC',null,'2015-01-01 00:00:00','super',2,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (3,'AC',null,'2015-01-01 00:00:00','super',3,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (4,'AC',null,'2015-01-01 00:00:00','super',4,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (5,'AC',null,'2015-01-01 00:00:00','super',5,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (6,'AC',null,'2015-01-01 00:00:00','super',6,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (7,'AC',null,'2015-01-01 00:00:00','super',7,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (8,'AC',null,'2015-01-01 00:00:00','super',8,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (9,'AC',null,'2015-01-01 00:00:00','super',9,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (10,'AC',null,'2015-01-01 00:00:00','super',10,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (11,'AC',null,'2015-01-01 00:00:00','super',11,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (12,'AC',null,'2015-01-01 00:00:00','super',12,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (13,'AC',null,'2015-01-01 00:00:00','super',13,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (14,'AC',null,'2015-01-01 00:00:00','super',14,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (15,'AC',null,'2015-01-01 00:00:00','super',15,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (16,'AC',null,'2015-01-01 00:00:00','super',16,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (17,'AC',null,'2015-01-01 00:00:00','super',17,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (18,'AC',null,'2015-01-01 00:00:00','super',18,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (19,'AC',null,'2015-01-01 00:00:00','super',19,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (20,'AC',null,'2015-01-01 00:00:00','super',20,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (21,'AC',null,'2015-01-01 00:00:00','super',21,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (22,'AC',null,'2015-01-01 00:00:00','super',22,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (23,'AC',null,'2015-01-01 00:00:00','super',23,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (24,'AC',null,'2015-01-01 00:00:00','super',24,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (25,'AC',null,'2015-01-01 00:00:00','super',25,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (26,'AC',null,'2015-01-01 00:00:00','super',26,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (27,'AC',null,'2015-01-01 00:00:00','super',27,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (28,'AC',null,'2015-01-01 00:00:00','super',28,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (29,'AC',null,'2015-01-01 00:00:00','super',29,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (30,'AC',null,'2015-01-01 00:00:00','super',30,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (31,'AC',null,'2015-01-01 00:00:00','super',31,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (32,'AC',null,'2015-01-01 00:00:00','super',32,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (33,'AC',null,'2015-01-01 00:00:00','super',33,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (34,'AC',null,'2015-01-01 00:00:00','super',34,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (35,'AC',null,'2015-01-01 00:00:00','super',35,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (36,'AC',null,'2015-01-01 00:00:00','super',36,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (37,'AC',null,'2015-01-01 00:00:00','super',37,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (38,'AC',null,'2015-01-01 00:00:00','super',38,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (39,'AC',null,'2015-01-01 00:00:00','super',39,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (40,'AC',null,'2015-01-01 00:00:00','super',40,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (41,'AC',null,'2015-01-01 00:00:00','super',41,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (42,'AC',null,'2015-01-01 00:00:00','super',42,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (43,'AC',null,'2015-01-01 00:00:00','super',43,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (44,'AC',null,'2015-01-01 00:00:00','super',44,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (45,'AC',null,'2015-01-01 00:00:00','super',45,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (46,'AC',null,'2015-01-01 00:00:00','super',46,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (47,'AC',null,'2015-01-01 00:00:00','super',47,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (48,'AC',null,'2015-01-01 00:00:00','super',48,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (49,'AC',null,'2015-01-01 00:00:00','super',49,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (50,'AC',null,'2015-01-01 00:00:00','super',50,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (51,'AC',null,'2015-01-01 00:00:00','super',51,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (52,'AC',null,'2015-01-01 00:00:00','super',52,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (53,'AC',null,'2015-01-01 00:00:00','super',53,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (54,'AC',null,'2015-01-01 00:00:00','super',54,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (55,'AC',null,'2015-01-01 00:00:00','super',55,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (56,'AC',null,'2015-01-01 00:00:00','super',56,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (57,'AC',null,'2015-01-01 00:00:00','super',57,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (58,'AC',null,'2015-01-01 00:00:00','super',58,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (59,'AC',null,'2015-01-01 00:00:00','super',59,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (60,'AC',null,'2015-01-01 00:00:00','super',60,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (61,'AC',null,'2015-01-01 00:00:00','super',61,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (62,'AC',null,'2015-01-01 00:00:00','super',62,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (63,'AC',null,'2015-01-01 00:00:00','super',63,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (64,'AC',null,'2015-01-01 00:00:00','super',64,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (65,'AC',null,'2015-01-01 00:00:00','super',65,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (66,'AC',null,'2015-01-01 00:00:00','super',66,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (67,'AC',null,'2015-01-01 00:00:00','super',67,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (68,'AC',null,'2015-01-01 00:00:00','super',68,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (69,'AC',null,'2015-01-01 00:00:00','super',69,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (70,'AC',null,'2015-01-01 00:00:00','super',70,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (71,'AC',null,'2015-01-01 00:00:00','super',71,1);
INSERT INTO  permiso_v1(id,estado,fecha_modificacion,fecha_registro,usuario_registro,id_detalle_pagina,id_rol) VALUES (72,'AC',null,'2015-01-01 00:00:00','super',72,1);



-- ----------------------------
-- Records of permiso
-- ----------------------------
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (1, 'Seguridad', 1, null, 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (2, 'Usuario', 2, 'Seguridad', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (3, 'Roles', 2, 'Seguridad', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (4, 'Permiso', 2, 'Seguridad', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (5, 'Parametrizacion', 1, null, 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (6, 'Empresa', 2, 'Parametrizacion', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (7, 'Sucursal', 2, 'Parametrizacion', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (8, 'Informacion Legal', 2, 'Parametrizacion', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (9, 'Tipo de moneda', 2, 'Parametrizacion', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (10, 'Tipo de cambio', 2, 'Parametrizacion', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (11, 'Tipo de UFV', 2, 'Parametrizacion', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (12, 'Plan de cuenta', 2, 'Parametrizacion', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (13, 'Apertura y cierre de gestion', 2, 'Parametrizacion', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (14, 'Proceso', 1, null, 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (15, 'Cuentas monetarias', 2, 'Proceso', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (16, 'Cuentas no monetarias', 2, 'Proceso', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (17, 'Formulario', 1, null, 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (18, 'Comprobante', 2, 'Formulario', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (19, 'Venta', 1, null, 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (20, 'Compra', 1, null, 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (21, 'Proforma cotizacion', 2, 'Formulario', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (22, 'Activo fijo', 2, 'Formulario', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (23, 'Reporte', 1, null, 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (24, 'Libros', 2, 'Reporte', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (25, 'Libro diario', 3, 'Libros', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (26, 'Libro mayores', 3, 'Libros', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (27, 'Sumas y saldos', 3, 'Libros', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (28, 'Libro de compras, ventas y bancarizacion', 2, 'Libros', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (29, 'Estado financiero', 2, 'Reporte', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (30, 'Balance', 2, 'Reporte', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (31, 'Estado de resultados', 2, 'Reporte', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (32, 'Flujo de efectivo', 2, 'Reporte', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (33, 'Estado comparativo', 2, 'Reporte', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (34, 'Cuadro activo fijo', 2, 'Formulario', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (35, 'Centro Costo', 2, 'Parametrizacion', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (36, 'Cliente', 2, 'Parametrizacion', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (37, 'Servicio', 2, 'Parametrizacion', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (38, 'Tipo Comprobante', 2, 'Parametrizacion', 'AC');
INSERT INTO "public"."permiso"(id,nombre,tipo,padre,estado) VALUES (39, 'Proveedor', 2, 'Compra', 'AC');

-- ----------------------------
-- Records of privilegio
-- ----------------------------
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (1, 'AC', 'AC', 1, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (2, 'AC', 'AC', 2, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (3, 'AC', 'AC', 3, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (4, 'AC', 'AC', 4, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (5, 'AC', 'AC', 5, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (6, 'AC', 'AC', 6, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (7, 'AC', 'AC', 7, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (8, 'AC', 'AC', 8, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (9, 'AC', 'AC', 9, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (10, 'AC', 'AC', 10, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (11, 'AC', 'AC', 11, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (12, 'AC', 'AC', 12, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (13, 'AC', 'AC', 13, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (14, 'AC', 'AC', 14, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (15, 'AC', 'AC', 15, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (16, 'AC', 'AC', 16, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (17, 'AC', 'AC', 17, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (18, 'AC', 'AC', 18, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (19, 'AC', 'AC', 19, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (20, 'AC', 'AC', 20, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (21, 'AC', 'AC', 21, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (22, 'AC', 'AC', 22, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (23, 'AC', 'AC', 23, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (24, 'AC', 'AC', 24, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (25, 'AC', 'AC', 25, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (26, 'AC', 'AC', 26, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (27, 'AC', 'AC', 27, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (28, 'AC', 'AC', 28, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (29, 'AC', 'AC', 29, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (30, 'AC', 'AC', 30, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (31, 'AC', 'AC', 31, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (32, 'AC', 'AC', 32, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (33, 'AC', 'AC', 33, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (34, 'AC', 'AC', 34, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (35, 'AC', 'AC', 35, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (36, 'AC', 'AC', 36, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (37, 'AC', 'AC', 37, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (38, 'AC', 'AC', 38, 1, 'AC', '2015-01-01 00:00:00', 'super');
INSERT INTO "public"."privilegio"(id,escritura,lectura,id_permiso,id_roles,estado,fecha_registro,usuario_registro) VALUES (39, 'AC', 'AC', 39, 1, 'AC', '2015-01-01 00:00:00', 'super');

-- ----------------------------
-- Records of moneda
-- ----------------------------
INSERT INTO "public"."moneda" VALUES (1, 'BOLIVIANOS', 'Bs.');
INSERT INTO "public"."moneda" VALUES (2, 'DOLAR', 'Usd.');
INSERT INTO "public"."moneda" VALUES (3, 'REAL', 'R$.');
INSERT INTO "public"."moneda" VALUES (4, 'EURO', 'E.');


-- ----------------------------
-- Records of tipo_cuenta
-- ----------------------------
-- INSERT INTO "public"."tipo_cuenta" VALUES (1, 'ACTIVO');
-- INSERT INTO "public"."tipo_cuenta" VALUES (2, 'PASIVO');
-- INSERT INTO "public"."tipo_cuenta" VALUES (3, 'CAPITAL');
-- INSERT INTO "public"."tipo_cuenta" VALUES (4, 'INGRESO');
-- INSERT INTO "public"."tipo_cuenta" VALUES (5, 'EGRESO');
-- INSERT INTO "public"."tipo_cuenta" VALUES (6, 'COSTO');




