insert into budget.user values ('C9E4758E-0251-4449-9600-F2C757C4CDCF', 'MacDers', 'Louis', '6095094605', 'macdersonlouis@gmail.com', 'TEXT', null);

--delete from budget.bill;
insert into budget.bill values('13CCAB2B-8458-47A1-B14C-A16CA9D74F1E', 'C9E4758E-0251-4449-9600-F2C757C4CDCF', 'Car Payment', 'honda financial services', 'Auto Loan',
22, false, 8, 35.00, 1.99, 10, 'www.hondafinancialservices.com', 'macdersonlouis@gmail.com', 'Notorious1982', null, false);

insert into budget.bill values('3355C67A-34E7-4063-827B-5186D8389995', 'C9E4758E-0251-4449-9600-F2C757C4CDCF', 'Rent', 'DL', 'Rent',
22, false, 8, 0, 0, 5, null, null, null, null, false);

insert into budget.bill values('3355C67c-34E7-4064-827B-5186D8389995', 'C9E4758E-0251-4449-9600-F2C757C4CDCF', 'Rent', 'Joe', 'Rent',
21, false, 8, 0, 0, 5, null, null, null, null, false);

select * from budget.payment;

delete from budget.payment;