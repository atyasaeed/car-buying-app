CREATE TABLE supplier_offers (
    id BIGSERIAL PRIMARY KEY,
    supplier_id BIGINT NOT NULL,
    customer_request_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    inspection_score INTEGER NOT NULL DEFAULT 0,
    car_details TEXT NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_customer_request FOREIGN KEY (customer_request_id) REFERENCES customer_requests(id) ON DELETE CASCADE
);

CREATE INDEX idx_supplier_id ON supplier_offers(supplier_id);
CREATE INDEX idx_request_id ON supplier_offers(customer_request_id);
CREATE INDEX idx_offer_status ON supplier_offers(status);
CREATE UNIQUE INDEX idx_request_supplier_unique ON supplier_offers(customer_request_id, supplier_id);
