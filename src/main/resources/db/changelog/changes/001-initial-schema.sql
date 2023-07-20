DROP TABLE IF EXISTS "drones";
DROP SEQUENCE IF EXISTS drones_drone_id_seq;
CREATE SEQUENCE drones_drone_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 START 2 CACHE 1;

CREATE TABLE "public"."drones" (
                                   "id" integer DEFAULT nextval('drones_drone_id_seq') NOT NULL,
                                   "serial_number" character varying(100) NOT NULL UNIQUE,
                                   "model" character varying(20) NOT NULL,
                                   "weight" integer NOT NULL,
                                   "battery_capacity" integer NOT NULL,
                                   "state" character varying(20) NOT NULL,
                                   CONSTRAINT "drones_pkey" PRIMARY KEY ("id"),
                                   CONSTRAINT "drones_serial_number" UNIQUE ("serial_number")
) WITH (oids = false);

INSERT INTO "drones" ("id", "serial_number", "model", "weight", "battery_capacity", "state") VALUES
(2,	'01',	'Heavy',	12,	78,	'IDLE');

DROP TABLE IF EXISTS "medications";
DROP SEQUENCE IF EXISTS medications_medication_id_seq;
CREATE SEQUENCE medications_medication_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 START 2 CACHE 1;

CREATE TABLE "public"."medications" (
                                        "id" integer DEFAULT nextval('medications_medication_id_seq') NOT NULL,
                                        "name" character varying(100) NOT NULL,
                                        "weight" integer NOT NULL,
                                        "code" character varying(100) NOT NULL,
                                        "image" bytea,
                                        "drone_id" integer,
                                        CONSTRAINT "medications_pkey" PRIMARY KEY ("id"),
                                        CONSTRAINT "medications_code" UNIQUE ("code")
) WITH (oids = false);

INSERT INTO "medications" ("id", "name", "weight", "code", "image", "drone_id") VALUES
(2,	'Aspirine',	12,	'01',	NULL,	2);

ALTER TABLE ONLY "public"."medications" ADD CONSTRAINT "medications_drone_id_fkey" FOREIGN KEY (drone_id) REFERENCES drones(id) NOT DEFERRABLE;
