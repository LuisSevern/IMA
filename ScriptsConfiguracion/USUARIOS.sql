/*CREACION DE USUARIOS.*/
START TRANSACTION;

DROP USER IF EXISTS 'admindb_ima'@'localhost';	
CREATE USER `admindb_ima`@`localhost` IDENTIFIED BY 'admindb2020' PASSWORD EXPIRE NEVER;
GRANT ALL ON *.* TO 'adminbd_ima'@'localhost';

DROP USER IF EXISTS 'ima_user'@'localhost';	
CREATE USER `ima_user`@`localhost` IDENTIFIED BY 'ima_user_2020' PASSWORD EXPIRE NEVER;
GRANT ALL ON bam.* TO 'ima_user'@'localhost';
GRANT ALL ON pcm.* TO 'ima_user'@'localhost';
GRANT ALL ON wop.* TO 'ima_user'@'localhost';


DROP USER IF EXISTS 'invitado'@'localhost';	
CREATE USER `invitado`@`localhost` IDENTIFIED BY 'invitado' PASSWORD EXPIRE NEVER;
GRANT SELECT ON bam.* TO `invitado`@`localhost`;
GRANT SELECT ON pcm.* TO `invitado`@`localhost`;
GRANT SELECT ON wop.* TO `invitado`@`localhost`;
COMMIT;
