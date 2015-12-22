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
INSERT INTO "public"."usuario"(id,email ,fecha_registro,foto_perfil,login,nombre,password,peso_foto,state,usuario_registro,fecha_modificacion) VALUES (1,'admin.admin@gmail.com','2015-01-01 00:00:00',null,'admin','ADMINISTRADOR','admin', 0,'SU','admin',null);

-- ----------------------------
--  Sequence structure for usuario_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."usuario_id_seq";
CREATE SEQUENCE "public"."usuario_id_seq" INCREMENT 1 START 2 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."usuario_id_seq" OWNER TO "inventario";


-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO "public"."roles"(id,estado,fecha_modificacion,fecha_registro,nombre,usuario_registro,descripcion) VALUES(1,'SU',null,'2015-01-01 00:00:00','ADMINISTRADOR','admin','Grupo de Usuario Super Us.');

-- ----------------------------
--  Sequence structure for roles_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."roles_id_seq";
CREATE SEQUENCE "public"."roles_id_seq" INCREMENT 1 START 2 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."roles_id_seq" OWNER TO "inventario";


-- ----------------------------
-- Records of usuario_rol
-- ----------------------------
INSERT INTO "public"."usuario_rol"(id,estado,fecha_modificacion ,fecha_registro ,usuario_registro,id_roles, id_usuario) values(1,'AC',null,'2015-01-01 00:00:00','admin',1,1);
-- ----------------------------
--  Sequence structure for usuario_rol_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."usuario_rol_id_seq";
CREATE SEQUENCE "public"."usuario_rol_id_seq" INCREMENT 1 START 2 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."usuario_rol_id_seq" OWNER TO "inventario";
-- ----------------------------
-- Records of modulo
-- ----------------------------
INSERT INTO "public"."modulo" (id,nombre,id_modulo_padre) VALUES (1,'SEGURIDAD',null);
INSERT INTO "public"."modulo" (id,nombre,id_modulo_padre) VALUES (2,'PARAMETRIZACION',null);
INSERT INTO "public"."modulo" (id,nombre,id_modulo_padre) VALUES (3,'INVENTARIO',null);
INSERT INTO "public"."modulo" (id,nombre,id_modulo_padre) VALUES (4,'PROCESO',null);
INSERT INTO "public"."modulo" (id,nombre,id_modulo_padre) VALUES (5,'REPORTE',null);

-- ----------------------------
-- Records of accion
-- ----------------------------
INSERT INTO "public"."accion" (id,nombre) VALUES (1,'REGISTRAR');
INSERT INTO "public"."accion" (id,nombre) VALUES (2,'MODIFICAR');
INSERT INTO "public"."accion" (id,nombre) VALUES (3,'ELIMINAR');
INSERT INTO "public"."accion" (id,nombre) VALUES (4,'PROCESAR');
INSERT INTO "public"."accion" (id,nombre) VALUES (5,'VER REPORTE');
INSERT INTO "public"."accion" (id,nombre) VALUES (6,'CONCILIAR');
INSERT INTO "public"."accion" (id,nombre) VALUES (7,'REVISAR');
INSERT INTO "public"."accion" (id,nombre) VALUES (8,'VER LISTA');
INSERT INTO "public"."accion" (id,nombre) VALUES (9,'EXPORTAR');
INSERT INTO "public"."accion" (id,nombre) VALUES (10,'IMPORTAR');

-- ----------------------------
-- Records of pagina
-- ----------------------------

-- ----------------------------
-- Records of pagina - MODULO SEGURIDAD
-- ----------------------------
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('1', 'USUARIO', 'usuario.xhtml', '1', null);
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('2', 'ROL', 'rol.xhtml', '1', null);
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('3', 'PERMISO', 'permiso.xhtml', '1', null);
-- ----------------------------
-- Records of pagina - MODULO PARAMETRIZACION
-- ----------------------------
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('4', 'EMPRESA', 'empresa.xhtml', '2', null);
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('5', 'PROVEEDOR', 'proveedor.xhtml', '2', null);
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('6', 'ALMACEN', 'almacen.xhtml', '2', null);
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('7', 'PARTIDA', 'partida.xhtml', '2', null);
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('8', 'PROYECTO', 'proyecto.xhtml', '2', null);
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('9', 'AREA DE TRABAJO', 'unidad.xhtml', '2', null);
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('10', 'FUNCIONARIO', 'funcionario.xhtml', '2', null);
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('11', 'UNIDAD DE MEDIDA', 'unidad_medida.xhtml', '2', null);
-- ----------------------------
-- Records of pagina - MODULO INVENTARIO
-- ----------------------------
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('12', 'CATALOGO PRODUCTO', 'catalogo_producto.xhtml', '3', null);
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('13', 'KARDEX PRODUCTO', 'kardex_producto.xhtml', '3', null);
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('14', 'GESTION PRODUCTO', 'gestion-producto.xhtml', '3', null);
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('15', 'STOCK PRODUCTO', 'stock_producto.xhtml', '3', null);
-- ----------------------------
-- Records of pagina - MODULO PROCESO
-- ----------------------------
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('16', 'ORDEN INGRESO', 'orden_ingreso.xhtml', '4', null);
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('17', 'ORDEN TRASPASO', 'orden_traspaso.xhtml', '4', null);
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('18', 'ORDEN SALIDA', 'orden_salida.xhtml', '4', null);
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('19', 'TOMA DE INVENTARIO', 'toma_inventario.xhtml', '4', null);
-- ----------------------------
-- Records of pagina - MODULO REPORTE
-- ----------------------------
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('20', 'LISTA ITEM PEPS', 'lista_item.xhtml', '5', null);
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('21', 'REPORTE UNIDAD SOLICITANTE', 'report_unidad_solicitante.xhtml', '5', null);
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('22', 'REPORTE PARTIDA', 'report_partida.xhtml', '5', null);
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('23', 'REPORTE PROYECTO', 'report_proyecto.xhtml', '5', null);
INSERT INTO "public"."pagina" (id,nombre,path,id_modulo,path2) VALUES ('24', 'REPORTE SERV. PUBLICO', 'report_total_funcionario.xhtml', '5', null);


-- ----------------------------
-- Records of detalle_pagina
-- ----------------------------

-- 1 USUARIO
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('1', '1', '1');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('2', '2', '1');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('3', '3', '1');
-- 2 ROL
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('4', '1', '2');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('5', '2', '2');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('6', '3', '2');
-- 3 PERMISO
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('7', '1', '3');
-- 4 EMPRESA
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('8', '2', '4');
-- 5 PROVEEDOR
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('9', '1', '5');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('10', '2', '5');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('11', '3', '5');
-- '6', 'ALMACEN'
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('12', '1', '6');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('13', '2', '6');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('14', '3', '6');
-- '7', 'PARTIDA'
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('15', '1', '7');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('16', '2', '7');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('17', '3', '7');
-- '8', 'PROYECTO'
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('18', '1', '8');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('19', '2', '8');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('20', '3', '8');
-- '9', 'AREA DE TRABAJO'
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('21', '1', '9');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('22', '2', '9');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('23', '3', '9');
-- '10', 'FUNCIONARIO'
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('24', '1', '10');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('25', '2', '10');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('26', '3', '10');
-- '11', 'UNIDAD DE MEDIDA'
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('27', '1', '11');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('28', '2', '11');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('29', '3', '11');
-- '12', 'CATALOGO PRODUCTO'
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('30', '8', '12');
-- '13', 'KARDEX PRODUCTO'
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('31', '4', '13');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('32', '5', '13');
-- '14', 'GESTION PRODUCTO'
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('33', '1', '14');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('34', '2', '14');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('35', '3', '14');
-- '15', 'STOCK PRODUCTO'
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('36', '4', '15');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('37', '5', '15');
-- '16', 'ORDEN INGRESO'
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('38', '1', '16');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('39', '2', '16');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('40', '3', '16');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('41', '4', '16');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('42', '5', '16');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('43', '10', '16');
-- '17', 'ORDEN TRASPASO'
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('44', '1', '17');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('45', '2', '17');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('46', '3', '17');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('47', '4', '17');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('48', '5', '17');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('49', '9', '17');
-- '18', 'ORDEN SALIDA'
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('50', '1', '18');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('51', '2', '18');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('52', '3', '18');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('53', '4', '18');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('54', '5', '18');
-- '19', 'TOMA DE INVENTARIO'
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('55', '1', '19');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('56', '3', '19');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('57', '4', '19');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('58', '5', '19');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('59', '6', '19');
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('60', '7', '19');
-- '20', 'LISTA ITEM PEPS'
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('61', '5', '20');
-- '21', 'REPORTE UNIDAD SOLICITANTE'
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('62', '5', '21');
-- '22', 'REPORTE PARTIDA'
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('63', '5', '22');
-- '23', 'REPORTE PROYECTO'
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('64', '5', '23');
-- '24' 'REPORTE SERV. PUBLICO'
INSERT INTO "public"."detalle_pagina" (id,id_accion, id_pagina) VALUES ('65', '5', '24');

-- ----------------------------
-- Records of permiso
-- ----------------------------

INSERT INTO "public"."permiso" VALUES ('1', 'AC', null, '2015-01-01 00:00:00', 'admin', '1', '1');
INSERT INTO "public"."permiso" VALUES ('2', 'AC', null, '2015-01-01 00:00:00', 'admin', '2', '1');
INSERT INTO "public"."permiso" VALUES ('3', 'AC', null, '2015-01-01 00:00:00', 'admin', '3', '1');
INSERT INTO "public"."permiso" VALUES ('4', 'AC', null, '2015-01-01 00:00:00', 'admin', '4', '1');
INSERT INTO "public"."permiso" VALUES ('5', 'AC', null, '2015-01-01 00:00:00', 'admin', '5', '1');
INSERT INTO "public"."permiso" VALUES ('6', 'AC', null, '2015-01-01 00:00:00', 'admin', '6', '1');
INSERT INTO "public"."permiso" VALUES ('7', 'AC', null, '2015-01-01 00:00:00', 'admin', '7', '1');
INSERT INTO "public"."permiso" VALUES ('8', 'AC', null, '2015-01-01 00:00:00', 'admin', '8', '1');
INSERT INTO "public"."permiso" VALUES ('9', 'AC', null, '2015-01-01 00:00:00', 'admin', '9', '1');
INSERT INTO "public"."permiso" VALUES ('10', 'AC', null, '2015-01-01 00:00:00', 'admin', '10', '1');
INSERT INTO "public"."permiso" VALUES ('11', 'AC', null, '2015-01-01 00:00:00', 'admin', '11', '1');
INSERT INTO "public"."permiso" VALUES ('12', 'AC', null, '2015-01-01 00:00:00', 'admin', '12', '1');
INSERT INTO "public"."permiso" VALUES ('13', 'AC', null, '2015-01-01 00:00:00', 'admin', '13', '1');
INSERT INTO "public"."permiso" VALUES ('14', 'AC', null, '2015-01-01 00:00:00', 'admin', '14', '1');
INSERT INTO "public"."permiso" VALUES ('15', 'AC', null, '2015-01-01 00:00:00', 'admin', '15', '1');
INSERT INTO "public"."permiso" VALUES ('16', 'AC', null, '2015-01-01 00:00:00', 'admin', '16', '1');
INSERT INTO "public"."permiso" VALUES ('17', 'AC', null, '2015-01-01 00:00:00', 'admin', '17', '1');
INSERT INTO "public"."permiso" VALUES ('18', 'AC', null, '2015-01-01 00:00:00', 'admin', '18', '1');
INSERT INTO "public"."permiso" VALUES ('19', 'AC', null, '2015-01-01 00:00:00', 'admin', '19', '1');
INSERT INTO "public"."permiso" VALUES ('20', 'AC', null, '2015-01-01 00:00:00', 'admin', '20', '1');
INSERT INTO "public"."permiso" VALUES ('21', 'AC', null, '2015-01-01 00:00:00', 'admin', '21', '1');
INSERT INTO "public"."permiso" VALUES ('22', 'AC', null, '2015-01-01 00:00:00', 'admin', '22', '1');
INSERT INTO "public"."permiso" VALUES ('23', 'AC', null, '2015-01-01 00:00:00', 'admin', '23', '1');
INSERT INTO "public"."permiso" VALUES ('24', 'AC', null, '2015-01-01 00:00:00', 'admin', '24', '1');
INSERT INTO "public"."permiso" VALUES ('25', 'AC', null, '2015-01-01 00:00:00', 'admin', '25', '1');
INSERT INTO "public"."permiso" VALUES ('26', 'AC', null, '2015-01-01 00:00:00', 'admin', '26', '1');
INSERT INTO "public"."permiso" VALUES ('27', 'AC', null, '2015-01-01 00:00:00', 'admin', '27', '1');
INSERT INTO "public"."permiso" VALUES ('28', 'AC', null, '2015-01-01 00:00:00', 'admin', '28', '1');
INSERT INTO "public"."permiso" VALUES ('29', 'AC', null, '2015-01-01 00:00:00', 'admin', '29', '1');
INSERT INTO "public"."permiso" VALUES ('30', 'AC', null, '2015-01-01 00:00:00', 'admin', '30', '1');
INSERT INTO "public"."permiso" VALUES ('31', 'AC', null, '2015-01-01 00:00:00', 'admin', '31', '1');
INSERT INTO "public"."permiso" VALUES ('32', 'AC', null, '2015-01-01 00:00:00', 'admin', '32', '1');
INSERT INTO "public"."permiso" VALUES ('33', 'AC', null, '2015-01-01 00:00:00', 'admin', '33', '1');
INSERT INTO "public"."permiso" VALUES ('34', 'AC', null, '2015-01-01 00:00:00', 'admin', '34', '1');
INSERT INTO "public"."permiso" VALUES ('35', 'AC', null, '2015-01-01 00:00:00', 'admin', '35', '1');
INSERT INTO "public"."permiso" VALUES ('36', 'AC', null, '2015-01-01 00:00:00', 'admin', '36', '1');
INSERT INTO "public"."permiso" VALUES ('37', 'AC', null, '2015-01-01 00:00:00', 'admin', '37', '1');
INSERT INTO "public"."permiso" VALUES ('38', 'AC', null, '2015-01-01 00:00:00', 'admin', '38', '1');
INSERT INTO "public"."permiso" VALUES ('39', 'AC', null, '2015-01-01 00:00:00', 'admin', '39', '1');
INSERT INTO "public"."permiso" VALUES ('40', 'AC', null, '2015-01-01 00:00:00', 'admin', '40', '1');
INSERT INTO "public"."permiso" VALUES ('41', 'AC', null, '2015-01-01 00:00:00', 'admin', '41', '1');
INSERT INTO "public"."permiso" VALUES ('42', 'AC', null, '2015-01-01 00:00:00', 'admin', '42', '1');
INSERT INTO "public"."permiso" VALUES ('43', 'AC', null, '2015-01-01 00:00:00', 'admin', '43', '1');
INSERT INTO "public"."permiso" VALUES ('44', 'AC', null, '2015-01-01 00:00:00', 'admin', '44', '1');
INSERT INTO "public"."permiso" VALUES ('45', 'AC', null, '2015-01-01 00:00:00', 'admin', '45', '1');
INSERT INTO "public"."permiso" VALUES ('46', 'AC', null, '2015-01-01 00:00:00', 'admin', '46', '1');
INSERT INTO "public"."permiso" VALUES ('47', 'AC', null, '2015-01-01 00:00:00', 'admin', '47', '1');
INSERT INTO "public"."permiso" VALUES ('48', 'AC', null, '2015-01-01 00:00:00', 'admin', '48', '1');
INSERT INTO "public"."permiso" VALUES ('49', 'AC', null, '2015-01-01 00:00:00', 'admin', '49', '1');
INSERT INTO "public"."permiso" VALUES ('50', 'AC', null, '2015-01-01 00:00:00', 'admin', '50', '1');
INSERT INTO "public"."permiso" VALUES ('51', 'AC', null, '2015-01-01 00:00:00', 'admin', '51', '1');
INSERT INTO "public"."permiso" VALUES ('52', 'AC', null, '2015-01-01 00:00:00', 'admin', '52', '1');
INSERT INTO "public"."permiso" VALUES ('53', 'AC', null, '2015-01-01 00:00:00', 'admin', '53', '1');
INSERT INTO "public"."permiso" VALUES ('54', 'AC', null, '2015-01-01 00:00:00', 'admin', '54', '1');
INSERT INTO "public"."permiso" VALUES ('55', 'AC', null, '2015-01-01 00:00:00', 'admin', '55', '1');
INSERT INTO "public"."permiso" VALUES ('56', 'AC', null, '2015-01-01 00:00:00', 'admin', '56', '1');
INSERT INTO "public"."permiso" VALUES ('57', 'AC', null, '2015-01-01 00:00:00', 'admin', '57', '1');
INSERT INTO "public"."permiso" VALUES ('58', 'AC', null, '2015-01-01 00:00:00', 'admin', '58', '1');
INSERT INTO "public"."permiso" VALUES ('59', 'AC', null, '2015-01-01 00:00:00', 'admin', '59', '1');
INSERT INTO "public"."permiso" VALUES ('60', 'AC', null, '2015-01-01 00:00:00', 'admin', '60', '1');
INSERT INTO "public"."permiso" VALUES ('61', 'AC', null, '2015-01-01 00:00:00', 'admin', '61', '1');
INSERT INTO "public"."permiso" VALUES ('62', 'AC', null, '2015-01-01 00:00:00', 'admin', '62', '1');
INSERT INTO "public"."permiso" VALUES ('63', 'AC', null, '2015-01-01 00:00:00', 'admin', '63', '1');
INSERT INTO "public"."permiso" VALUES ('64', 'AC', null, '2015-01-01 00:00:00', 'admin', '64', '1');
INSERT INTO "public"."permiso" VALUES ('65', 'AC', null, '2015-01-01 00:00:00', 'admin', '65', '1');
-- ----------------------------
--  Sequence structure for permiso_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."permiso_id_seq";
CREATE SEQUENCE "public"."permiso_id_seq" INCREMENT 1 START 66 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."permiso_id_seq" OWNER TO "inventario";

-- ----------------------------
-- Records of empresa
-- ----------------------------
INSERT INTO "public"."empresa" (id,nit,ciudad,direccion,estado,fecha_modificacion,fecha_registro,razonsocial,telefono,usuario_registro,foto_perfil,peso_foto) VALUES ('1', '30000000', 'SANTA CRUZ DE LA SIERRA', 'CALLE RENE MORENO #100', 'AC', null, '2015-01-01 00:00:00', 'EMPRESA DEMO', '398702', 'admin',null,0);

-- ----------------------------
-- Records of gestion
-- ----------------------------
INSERT INTO "public"."gestion" (id,estado,estado_cierre,fecha_modificacion,fecha_registro,gestion,iniciada,usuario_registro,id_empresa) VALUES ('1','AC','AC',null,'2015-01-01 00:00:00','2015','FALSE','admin','1');
-- ----------------------------
--  Sequence structure for gestion_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."gestion_id_seq";
CREATE SEQUENCE "public"."gestion_id_seq" INCREMENT 1 START 2 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
ALTER TABLE "public"."gestion_id_seq" OWNER TO "inventario";


