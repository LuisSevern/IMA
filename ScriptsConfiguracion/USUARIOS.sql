/*CREACION DE USUARIOS.*/
DROP USER IF EXISTS `adminbd`;
CREATE USER 'adminbd'@'localhost' IDENTIFIED BY '2020_adminbd';
GRANT ALL_PRIVILEGES ON *.* TO 'adminbd'@'localhost';

DROP USER IF EXISTS `ima_user`;
CREATE USER 'ima_user'@'localhost' IDENTIFIED BY 'ima_user_2020';
GRANT ALL_PRIVILEGES ON bam.* TO 'ima_user'@'localhost';
GRANT ALL_PRIVILEGES ON pcm.* TO 'ima_user'@'localhost';
GRANT ALL_PRIVILEGES ON wop.* TO 'ima_user'@'localhost';


DROP USER IF EXISTS `invitado`;
CREATE USER 'invitado'@'localhost' IDENTIFIED BY 'invitado';
GRANT SELECT ON bam.* TO 'invitado'@'localhost';
GRANT SELECT ON pcm.* TO 'invitado'@'localhost';
GRANT SELECT ON wop.* TO 'invitado'@'localhost';
