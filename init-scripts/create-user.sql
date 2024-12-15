-- Criar usuário adicional
CREATE USER ms_orders WITH PASSWORD 'ms_orders';

-- Conceder privilégios ao usuário
GRANT ALL PRIVILEGES ON DATABASE order_management_db TO ms_orders;
