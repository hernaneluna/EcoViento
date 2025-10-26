create database ecoviento
  character set utf8mb4  collate utf8mb4_unicode_ci;
use ecoviento;

-- Tabla central_eolica es la tabla central, el resto de las tablas tiene relacion con esta
create table central_eolica (
  id int auto_increment primary key,
  nombre varchar(100) not null,
  unique key uq_central_nombre (nombre)
);

-- Tabla parametro, se usa para registrar los parametros de las centrales
create table parametro (
  id int auto_increment primary key,
  central_id int not null unique,
  viento_minimo double not null,
  viento_maximo double not null,
  potencia_minima double not null,
  potencia_maxima double not null
 );

-- Tabla turbina, tiene las turbinas asociadas a las centrales
create table turbina (
  id int auto_increment primary key,
  central_id int not null,
  nombre varchar(100) not null,
  estado enum('ENCENDIDA','APAGADA','ALERTADA') not null default 'APAGADA',
  constraint uq_turbina_nombre_por_central unique (central_id, nombre)
);

-- Tabla lectura, tiene las lecturas de las turbinas
create table lectura (
  id bigint auto_increment primary key,
  turbina_id int not null,
  fecha_hora datetime not null default current_timestamp,
  potencia_kw double not null,
  velocidad_viento double not null,
  estado_turbina enum('ENCENDIDA','APAGADA','ALERTADA') not null,
  index idx_lectura_turbina_fecha (turbina_id, fecha_hora)
);

-- Tabla alerta, registra las alertas de las turbinas
create table alerta (
  id bigint auto_increment primary key,
  turbina_id int not null,
  tipo varchar(100) not null,
  severidad enum('BAJA','MEDIA','ALTA') not null,
  descripcion varchar(500) not null,
  fecha_hora datetime not null default current_timestamp,
  index idx_alerta_turbina_fecha (turbina_id, fecha_hora)
);
