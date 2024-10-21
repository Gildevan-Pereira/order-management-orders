ALTER TABLE "ORDER" ALTER COLUMN client_city TYPE varchar(50) USING client_city::varchar(50);
ALTER TABLE "ORDER" ALTER COLUMN client_state TYPE varchar(2) USING client_state::varchar(2);
