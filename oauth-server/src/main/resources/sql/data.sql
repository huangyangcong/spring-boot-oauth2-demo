INSERT INTO `oauth_client_details`
(client_id,resource_ids,client_secret,scope,authorized_grant_types,web_server_redirect_uri,authorities,access_token_validity,refresh_token_validity,additional_information,autoapprove)
VALUES ('server', 'server', 'server', 'write,read', 'password,authorization_code,refresh_token', null, null, 600, 3000, null, 'true');

INSERT INTO `oauth_client_details`
(client_id,resource_ids,client_secret,scope,authorized_grant_types,web_server_redirect_uri,authorities,access_token_validity,refresh_token_validity,additional_information,autoapprove)
VALUES ('photo', 'photo', 'photo', 'read', 'authorization_code,refresh_token', null, null, 600, 3000, null, 'true');

INSERT INTO `oauth_client_details`
(client_id,resource_ids,client_secret,scope,authorized_grant_types,web_server_redirect_uri,authorities,access_token_validity,refresh_token_validity,additional_information,autoapprove)
VALUES ('note', 'note', 'note', 'read,write', 'authorization_code,refresh_token', null, null, 600, 3000, null, 'true');

INSERT INTO `oauth_client_details`
(client_id,resource_ids,client_secret,scope,authorized_grant_types,web_server_redirect_uri,authorities,access_token_validity,refresh_token_validity,additional_information,autoapprove)
VALUES ('all', 'server,note,photo', 'all', 'read', 'authorization_code,refresh_token', null, null, 600, 3000, null, 'true');

insert into user (name, password, code, email, phone, disable, expired, locked) values ('admin','admin','001','001@qq.com','001',0,0,0);
insert into user (name, password, code, email, phone, disable, expired, locked) values ('user','user','002','002@qq.com','001',0,0,0);
insert into user (name, password, code, email, phone, disable, expired, locked) values ('user_photo','user','002','002@qq.com','001',0,0,0);
insert into user (name, password, code, email, phone, disable, expired, locked) values ('user_note','user','002','002@qq.com','001',0,0,0);
insert into user (name, password, code, email, phone, disable, expired, locked) values ('user_both','user','002','002@qq.com','001',0,0,0);
insert into user (name, password, code, email, phone, disable, expired, locked) values ('disable','disable','003','003@qq.com','001',1,0,0);
insert into user (name, password, code, email, phone, disable, expired, locked) values ('expire','expire','004','004@qq.com','001',0,1,0);
insert into user (name, password, code, email, phone, disable, expired, locked) values ('lock','lock','005','005@qq.com','001',0,0,1);

insert into role (name) values ('ADMIN');
insert into role (name) values ('USER');
insert into role (name) values ('PHOTO');
insert into role (name) values ('NOTE');
insert into role (name) values ('GUEST');

#--user_id   role_id
insert into user_role values (1,1);
insert into user_role values (2,2);
insert into user_role values (3,3);
#--insert into user_role values (4,4);
#--insert into user_role values (5,3);
#--insert into user_role values (5,4);
#--insert into user_role values (6,2);
#--insert into user_role values (7,2);
#--insert into user_role values (8,2);