create table oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256)
);

CREATE TABLE oauth_resources (
  resouce_id VARCHAR (256) PRIMARY KEY,
  name VARCHAR (256),
);

CREATE TABLE oauth_scopes (
  scope_id VARCHAR (256) PRIMARY KEY,
  name VARCHAR (256),
);

CREATE TABLE uc_user (
  id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
  name VARCHAR (256) ,
  password VARCHAR(256),
  enable INTEGER,
  lock INTEGER,
  expired INTEGER
);

CREATE TABLE uc_role (
  id INTEGER (11) PRIMARY KEY,
  name VARCHAR (256),
  desc VARCHAR (256),
);

CREATE TABLE uc_authority (
  id INTEGER(11) PRIMARY KEY,
  name VARCHAR (256),
  desc VARCHAR (256),
);

CREATE TABLE uc_user_role (
  id INTEGER (11) PRIMARY KEY ,
  role_id INTEGER ,
  user_id INTEGER ,
);

CREATE TABLE uc_role_auth(
  id INTEGER (11) PRIMARY KEY ,
  role_id INTEGER ,
  auth_id INTEGER ,
);
