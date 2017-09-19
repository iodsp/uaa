select * from oauth_client_details;

insert into oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity ) values('test', 'res1', '$2a$10$ofuJ5MMaxMJGoyQt8mgadOnMk.R6O2aiTsC9fPjhJ8h5z6Zy1cZD.', 'string', 'implicit,password    ', 'string', 'auth1,auth2', 6000, 6000);
insert into uc_user (name, password, enable, lock, expired) values ('reader', '$2a$10$I9T1dbLiDHFeYXOED5x7Ke5mBGIsdIJ5KC5OPS428DkxwyeAZCKy.', 1, 0, 0);