CREATE TABLE tournaments (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT tournaments_name_length CHECK (char_length(name) BETWEEN 3 AND 120),
    CONSTRAINT tournaments_date_range CHECK (end_date >= start_date),
    CONSTRAINT tournaments_status_valid CHECK (
        status IN ('DRAFT', 'SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')
    )
);
