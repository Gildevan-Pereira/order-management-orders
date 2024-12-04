-- Criar usuário adicional
CREATE USER MS_ORDERS WITH PASSWORD 'MS_ORDERS';

-- Conceder privilégios ao usuário
GRANT ALL PRIVILEGES ON DATABASE msordersdb TO MS_ORDERS;
