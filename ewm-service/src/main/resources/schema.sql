CREATE TABLE IF EXISTS likes;

CREATE TABLE IF EXISTS requests;

CREATE TABLE IF EXISTS events_in_compilations;

CREATE TABLE IF EXISTS compilations;

CREATE TABLE IF EXISTS events;

CREATE TABLE IF EXISTS categories;

CREATE TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(250),
	email VARCHAR(254) unique
);

CREATE TABLE IF NOT EXISTS categories (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(50) unique
);

CREATE TABLE IF NOT EXISTS events (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	annotation VARCHAR(2000),
	category_id BIGINT NOT NULL REFERENCES categories(id),
	confirmed_requests INTEGER,
	created_on TIMESTAMP,
	event_date TIMESTAMP,
	description VARCHAR(7000),
	user_id BIGINT NOT NULL REFERENCES users(id),
	lat DOUBLE PRECISION,
	lon DOUBLE PRECISION,
	paid BOOLEAN,
    participant_limit INTEGER,
    published_on TIMESTAMP,
    request_moderation BOOLEAN,
    state VARCHAR(10),
    title VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS compilations (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	pinned BOOLEAN,
	title VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS events_in_compilations (
	event_id BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE,
	compilation_id BIGINT NOT NULL REFERENCES compilations(id) ON DELETE CASCADE,
	CONSTRAINT pk2 PRIMARY KEY (event_id, compilation_id)
);

CREATE TABLE IF NOT EXISTS requests (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	event_id BIGINT NOT NULL REFERENCES events(id),
	created TIMESTAMP,
	user_id BIGINT NOT NULL REFERENCES users(id),
    status VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS likes (
	event_id BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE,
	user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	is_liked BOOLEAN,
	CONSTRAINT pk1 PRIMARY KEY (event_id, user_id)
);
