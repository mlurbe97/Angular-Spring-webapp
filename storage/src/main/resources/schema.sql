CREATE TABLE IF NOT EXISTS users (
	user_uuid varchar(36) PRIMARY KEY,
	username varchar(16) UNIQUE NOT NULL,
	email varchar(255) UNIQUE NOT NULL,
	enabled varchar(12) NOT NULL,
	password varchar(32) NOT NULL,
	role varchar(16) NOT NULL,
	last_password_reset_date timestamp NOT NULL,
	create_time timestamp NOT NULL
);

CREATE TABLE IF NOT EXISTS documents (
	document_uuid varchar(36) PRIMARY KEY,
	user_uuid varchar(36) NOT NULL,
	file_name varchar(36),
	file_size NUMERIC,
	file_type VARCHAR(36),
	description varchar(1024)
);

CREATE TABLE IF NOT EXISTS metadatas (
	metadata_uuid varchar(36) PRIMARY KEY,
	document_uuid varchar(36) NOT NULL,
	create_time timestamp NOT NULL,
	update_time timestamp NOT NULL,
	key_meta varchar(255) NOT NULL,
	value_meta varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS contents (
	content_uuid varchar(36) PRIMARY KEY,
	document_uuid varchar(36) NOT NULL,
	content LONGBLOB
);