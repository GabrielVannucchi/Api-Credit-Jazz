CREATE TABLE IF NOT EXISTS CREDIT_ANALYSIS(
        id uuid NOT NULL,
        approved boolean,
        approved_limit decimal(7,2),
        requested_amount decimal(10,2),
        withdraw decimal(7,2),
        annual_interest decimal(6,3),
        client_id uuid,
        local_date timestamp,
        created_at timestamp,
        updated_at timestamp,
        PRIMARY KEY (id)
);