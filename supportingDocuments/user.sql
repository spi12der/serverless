create DATABASE IF NOT EXISTS admin;
USE  project;
DROP TABLE IF EXISTS user;
create table sd_details(
user_name char(50) PRIMARY KEY NOT NULL,
password char(50) NOT NULL,
user_type char(50) NOT NULL,
email_id char(50)NOT NULL UNIQUE
);
