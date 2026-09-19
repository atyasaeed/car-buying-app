CREATE TABLE customer_requests (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    checked_by_company VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_customer_id ON customer_requests(customer_id);
CREATE INDEX idx_status ON customer_requests(status);
