create DATABASE IF NOT EXISTS admin;
USE  admin;
DROP TABLE IF EXISTS user;
create table sd_details(
user_name char(50) PRIMARY KEY NOT NULL,
password char(50) NOT NULL,
user_type char(50) NOT NULL,
email_id char(50)NOT NULL UNIQUE
);
DROP TABLE IF EXISTS service_info;
create table service_info(
  service char(50) PRIMARY KEY NOT NULL,
  path char(50) NOT NULL,
  type char(50) NOT NULL
  );
