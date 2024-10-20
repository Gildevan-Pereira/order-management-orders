create table "ORDER" (
	ID SERIAL primary key,
	CREATED_AT TIMESTAMP default CURRENT_TIMESTAMP not null,
	UPDATED_AT TIMESTAMP default CURRENT_TIMESTAMP not null,
	REMOVED_AT TIMESTAMP,
	AMOUNT DECIMAL (10,2) not null,
	STATUS VARCHAR (15) not null,
	CLIENT_NAME VARCHAR (255) not null,
	CLIENT_CPF VARCHAR (11) not null,
	CLIENT_ADDRESS VARCHAR (255) not null,
	CLIENT_POSTAL_CODE VARCHAR (8) not null,
	CLIENT_CITY VARCHAR (255) not null,
	CLIENT_STATE VARCHAR (255) not null
);
create table "ITEM" (
	ID SERIAL primary key,
	ORDER_ID INTEGER not null,
	CREATED_AT TIMESTAMP default CURRENT_TIMESTAMP not null,
	UPDATED_AT TIMESTAMP default CURRENT_TIMESTAMP not null,
	REMOVED_AT TIMESTAMP,
	NAME VARCHAR (255) not null,
	DESCRIPTION VARCHAR (255) not null,
	UNITY_PRICE DECIMAL (10,2) not null,
	COUNT INTEGER not null,

	constraint FK_ORDER_ID foreign key (ORDER_ID) references "ORDER" (ID) on delete cascade
);