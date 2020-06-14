/*CREACION DE USUARIOS.*/
START TRANSACTION;

DROP USER IF EXISTS 'admindb_ima'@'%';
CREATE USER 'admindb_ima'@'%' IDENTIFIED BY 'admindb2020' PASSWORD EXPIRE NEVER;
GRANT ALL ON *.* TO 'admindb_ima'@'%';

DROP USER IF EXISTS 'ima_user'@'%';
CREATE USER `ima_user`@`%` IDENTIFIED BY 'ima_user_2020' PASSWORD EXPIRE NEVER;
GRANT ALL ON bam.* TO 'ima_user'@'%';
GRANT ALL ON pcm.* TO 'ima_user'@'%';
GRANT ALL ON wop.* TO 'ima_user'@'%';


DROP USER IF EXISTS 'invitado'@'%';
CREATE USER 'invitado'@'%' IDENTIFIED BY 'invitado' PASSWORD EXPIRE NEVER;
GRANT SELECT ON bam.* TO 'invitado'@'%';
GRANT SELECT ON pcm.* TO 'invitado'@'%';
GRANT SELECT ON wop.* TO 'invitado'@'%';
COMMIT;
