CREATE TABLE users (
                       user_id SERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL ,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE routes (
                        route_id SERIAL PRIMARY KEY,
                        user_id INT REFERENCES users(user_id) ON DELETE CASCADE,
                        name VARCHAR(100) NOT NULL,
                        description TEXT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stops (
                       stop_id SERIAL PRIMARY KEY,
                       route_id INT REFERENCES routes(route_id) ON DELETE CASCADE,
                       name VARCHAR(100) NOT NULL,
                       description TEXT,
                       position INT NOT NULL,
                       location GEOGRAPHY(Point, 4326) NOT NULL,  -- Используем тип GEOGRAPHY для координат
                       stop_type VARCHAR(50),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_stops_location ON stops USING GIST (location);
