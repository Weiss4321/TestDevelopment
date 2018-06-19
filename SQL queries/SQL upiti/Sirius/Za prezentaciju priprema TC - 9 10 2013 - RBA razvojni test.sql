SET CURRENT_SCHEMA = SIP

SELECT * from customer;


select * from INCOME_SUBTYPE;


SELECT * from customer where register_no = 10100;  -- Tehnièka trgovina

SELECT * from customer where register_no = 1100;  -- David & Company d.o.o. 

update customer set tax_number = '63019353660' where cus_id = 7894251;

commit;
----------------
select * from customer where name like '%Gavrilo%';

select * from customer_account;

select * from customer_account where cus_id = '41505251'; 

select * from cusacc_external;

select * from cusacc_external where register_no = '10100';   --- 3 raèuna ( cus_acc_external)

select * from customer_account where cus_acc_status = 'A' and ban_pro_typ_id = 790661005 and external_flag = '0' with ur ;  -- raèuni koji  rade

select tax_number,cus_code, register_no,cus_acc_ext_no,iban_acc_no from cusacc_external where cus_id in (select cus_id from customer where cus_typ_id in (2998,2999) and status = '0') and acc_ext_typ_id = '01' with ur


select tax_number,cus_code, register_no,cus_acc_ext_no,iban_acc_no from cusacc_external where cus_id in (select cus_id from customer where cus_typ_id in (2998,2999) and status = '0') and acc_ext_typ_id = '01' with ur

select * from income_head order by recording_time desc 
