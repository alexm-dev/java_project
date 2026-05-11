CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    created_time TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status TEXT,
    avg_rating REAL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS categories (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE IF NOT EXISTS sub_categories (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    category_id INTEGER NOT NULL,

    FOREIGN KEY (category_id) REFERENCES categories(id),

    UNIQUE(name, category_id)
);

CREATE TABLE IF NOT EXISTS assets (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    owner_id INTEGER NOT NULL,
    sub_category_id INTEGER NOT NULL,
    description TEXT,
    condition TEXT,
    asset_location_id INTEGER NOT NULL,
    daily_rate REAL NOT NULL,


    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (asset_location_id) REFERENCES locations(id) ON DELETE RESTRICT,
    FOREIGN KEY (sub_category_id) REFERENCES sub_categories(id)
);

CREATE TABLE IF NOT EXISTS locations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    city TEXT NOT NULL,
    postal_code TEXT NOT NULL,
    district TEXT NOT NULL,
    street_address TEXT NOT NULL,
    country TEXT NOT NULL
);
