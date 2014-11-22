drop table if exists budget.payment;
drop table if exists budget.income;
drop table if exists budget.bill;
drop table if exists budget.paycheck;
drop table if exists budget."user";
drop table if exists budget.address;

CREATE TABLE budget.address
(
    address_id serial not null unique primary key,
    address_line_1 varchar(256) not null,
    address_line_2 varchar(256),
    address_city varchar(256) not null,
    address_state char(2) not null,
    address_zipcode char(5) not null
) 
WITH (
  OIDS = FALSE
)
;
ALTER TABLE budget.address
  OWNER TO postgres;

  CREATE TABLE budget."user"
(
    user_id char(32) unique not null primary key,		--generated from email address, fname, lname
    user_fname varchar(128) not null,
    user_lname varchar(128) not null,
    user_phone varchar(10),
    user_email text not null,
    user_preferred_contact varchar(5) default 'EMAIL',
    user_address bigint references budget.address(address_id) on update cascade on delete cascade default null
) 
WITH (
  OIDS = FALSE
)
;
ALTER TABLE budget."user"
  OWNER TO postgres;

CREATE TABLE budget.bill
(
    bill_id char(32) unique not null primary key,	--generated using bill_owner, bill_source, bill_due_date
    bill_owner char(32) not null references budget."user"(user_id) on update cascade on delete cascade,
    bill_name varchar(128),
    bill_source varchar(128) not null,
    bill_type varchar(128) default 'OTHER',
    bill_due_date integer not null check(bill_due_date > 0 and bill_due_date < 32),
    bill_is_revolving boolean default false,
    bill_num_payments integer,
    bill_amount double precision not null,
    bill_late_fee_amount double precision,
    bill_interest_rate double precision,
    bill_grace_period integer check(bill_grace_period <= 15),
    bill_website text,
    bill_site_user_id text,
    bill_site_pwd text,
    bill_mail_address bigint references budget.address(address_id) on update no action on delete no action,
    bill_is_satisfied boolean default false
) 
WITH (
  OIDS = FALSE
)
;
ALTER TABLE budget.bill
  OWNER TO postgres;

CREATE TABLE budget.payment
(
    payment_id char(32) not null unique primary key, --generated from user_id, bill_id, and filing date
    payment_bill_id char(32) not null references budget.bill(bill_id) on update no action on delete no action,
    payment_user_id char(32) not null references budget."user"(user_id) on update cascade on delete cascade,
    payment_due_date date not null,
    payment_filing_date date not null,
    payment_last_notification_date date,
    payment_paid_date date,
    payment_qrcode_location text
) 
WITH (
  OIDS = FALSE
)
;
ALTER TABLE budget.payment
  OWNER TO postgres;

CREATE TABLE budget.paycheck
(
    paycheck_id char(32) not null unique primary key,	--generated using paycheck_source, gross_amount, net_amount, paycheck_owner
    paycheck_owner char(32) not null references budget."user"(user_id),
    paycheck_source varchar(256) not null,
    paycheck_occurrence integer not null default 1,		--bi-weekly
    paycheck_gross_amount double precision not null,
    paycheck_net_amount double precision not null,
    paycheck_filing_status integer default 1,		--single
    paycheck_total_allowances integer default 0,
    paycheck_source_state char(2),
    paycheck_401k_deduction double precision,
    paycheck_is_401k_percentage boolean default true,
    paycheck_is_401k_pre_tax boolean default true,
    paycheck_total_pre_tax_deduction double precision,
    paycheck_pre_tax_deduction_labels text,		--comma separated list of deductions
    paycheck_total_post_tax_deduction double precision,
    paycheck_post_tax_deduction_labels text,		--comma separated list of deductions
    paycheck_source_observed_holidays text		--comma separated list of source observed holidays
) 
WITH (
  OIDS = FALSE
)
;
ALTER TABLE budget.paycheck
  OWNER TO postgres;

CREATE TABLE budget.income
(
    income_id serial not null unique primary key,
    income_user_id char(32) not null references budget."user"(user_id)  on update cascade on delete cascade,
    income_paycheck_id char(32) not null references budget.paycheck(paycheck_id)  on update no action on delete no action,
    income_date date not null
) 
WITH (
  OIDS = FALSE
)
;
ALTER TABLE budget.income
  OWNER TO postgres;