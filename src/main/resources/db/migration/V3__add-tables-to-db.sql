drop table if exists beer_order;

drop table if exists beer_order_line;


create table beer_order(
    id varchar(36) primary key,
    created_date datetime(6),
    customer_ref varchar(255),
    last_modified_date datetime(6),
    version bigint,
    customer_id varchar (36)
    foreign key (customer_id) references customer(id)
    )engine=innoDB;

create table beer_order_line(
    id varchar(36),
    beer_id varchar(36),
    created_date datetime(6),
    last_modified_date datetime(6),
    order_quantity int,
    quantity allocated int,
    version bigint,
    beer_order_id varchar(36),
    foreign key (beer_order_id) references beer_order(id),
    foreign key (beer_id) references beer(id),
)engine=innoDB;