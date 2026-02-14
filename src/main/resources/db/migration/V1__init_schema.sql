CREATE TABLE concerts (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    artist_name VARCHAR(255) NOT NULL,
    venue VARCHAR(255) NOT NULL,
    concert_date TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_concerts_search ON concerts(name, artist_name, concert_date);

CREATE TABLE ticket_categories (
    id BIGSERIAL PRIMARY KEY,
    concert_id BIGINT NOT NULL REFERENCES concerts(id),
    name VARCHAR(100) NOT NULL,
    base_price DECIMAL(19, 2) NOT NULL,
    total_allocation INT NOT NULL,
    available_stock INT NOT NULL,
    version BIGINT DEFAULT 0,
    CONSTRAINT chk_stock_non_negative CHECK (available_stock >= 0)
);

CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    concert_id BIGINT NOT NULL REFERENCES concerts(id),
    user_id BIGINT NOT NULL, -- Simulasi user ID
    ticket_category_id BIGINT NOT NULL REFERENCES ticket_categories(id),
    total_amount DECIMAL(19, 2) NOT NULL,
    status VARCHAR(50) NOT NULL, -- PENDING, PAID, CANCELLED, REFUNDED
    booking_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL, -- Untuk auto-cancel > 5 menit
    idempotency_key VARCHAR(255) UNIQUE, -- Requirement: Idempotency
    version BIGINT DEFAULT 0
);

CREATE TABLE booking_items (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL REFERENCES bookings(id),
    ticket_category_id BIGINT NOT NULL REFERENCES ticket_categories(id),
    quantity INT NOT NULL,
    price_at_booking DECIMAL(19, 2) NOT NULL
);

CREATE TABLE payment_ledger (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL REFERENCES bookings(id),
    transaction_type VARCHAR(50) NOT NULL, -
    amount DECIMAL(19, 2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(255) NOT NULL,
    transaction_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    metadata JSONB
);

CREATE INDEX idx_ledger_booking ON payment_ledger(booking_id);