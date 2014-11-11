insert into budget.user values ('C9E4758E025144499600F2C757C4CDCF', 'MacDers', 'Louis', '6095094605', 'macdersonlouis@gmail.com', 'TEXT', null);

--delete from budget.bill;
insert into budget.bill values('13CCAB2B845847A1B14CA16CA9D74F1E', 'C9E4758E025144499600F2C757C4CDCF', 'Car Payment', 'honda financial services', 'Auto Loan',
22, false, 8, 400.00, 35.00, 1.99, 10, 'www.hondafinancialservices.com', 'macdersonlouis@gmail.com', 'Notorious1982', null, false);

insert into budget.bill values('3355C67A34E74063827B5186D8389995', 'C9E4758E025144499600F2C757C4CDCF', 'Rent', 'DL', 'Rent',
22, false, 8, 1475.00, 0, 0, 5, null, null, null, null, false);

insert into budget.bill values('3355C67c34E74064827B5186D8389995', 'C9E4758E025144499600F2C757C4CDCF', 'Rent', 'Joe', 'Rent',
21, false, 8, 25.00, 0, 0, 5, null, null, null, null, false);

select * from budget.payment;

delete from budget.payment;