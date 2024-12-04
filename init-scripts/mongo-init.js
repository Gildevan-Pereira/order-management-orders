db = db.getSiblingDB('order_management_db');
// Criar usuário adicional
db.createUser({
  user: "ms_orders",
  pwd: "ms_orders",
//  Conceder privilégios ao usuário
  roles: [
    { role: "readWrite", db: "order_management_db" }
  ]
});