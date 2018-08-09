CREATE TABLE `oauth_client_details` (
  `client_id` varchar(250) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `oauth_client_details` VALUES ('server', 'server', 'server', 'write,read', 'password,authorization_code,refresh_token', null, null, 600, 3000, null, 'true');
INSERT INTO `oauth_client_details` VALUES ('photo', 'photo', 'photo', 'read', 'authorization_code,refresh_token', null, null, 600, 3000, null, 'true');
INSERT INTO `oauth_client_details` VALUES ('note', 'note', 'note', 'read,write', 'authorization_code,refresh_token', null, null, 600, 3000, null, 'true');
INSERT INTO `oauth_client_details` VALUES ('all', 'server,note,photo', 'all', 'read', 'authorization_code,refresh_token', null, null, 600, 3000, null, 'true');

insert into user (enabled, expried, locked, password, username) values (1,0,0,'admin','admin');
insert into user (enabled, expried, locked, password, username) values (1,0,0,'user','user');
insert into user (enabled, expried, locked, password, username) values (1,0,0,'test1','test1');
insert into user (enabled, expried, locked, password, username) values (1,0,0,'test2','test2');
insert into user (enabled, expried, locked, password, username) values (1,0,0,'test3','test3');

insert into role (name) values ('ADMIN');
insert into role (name) values ('USER');
insert into role (name) values ('GUEST');

--user_id   role_id
insert into user_roles values (1,1);
insert into user_roles values (2,2);
insert into user_roles values (3,3);