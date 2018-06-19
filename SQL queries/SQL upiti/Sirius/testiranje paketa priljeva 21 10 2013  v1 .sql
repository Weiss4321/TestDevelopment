SET CURRENT_SCHEMA = SIDEV

select * from income_item;

select * from income_head;

select * from income_item where inc_hea_id like 'P070000000594';

select * from income_head where file_name IS NOT NULL ORDER BY value_date desc ;

select * from customer;



select * from customer_account;


select * from customer_account where contract_no = '3205000286';


select * from customer_account where ban_pro_typ_id =  790661005;

select * from customer_account where ban_pro_typ_id =  790661005 and cus_acc_status = 'A';

------------------------------------------
select * from customer where register_no = '1045';



select * from customer_account where cus_id = 26738352;  --- dohvat raèuna koji imaju cus_id = 26738352


select * from batch_def;  --- 

select * from batch_def where bat_def_id = 1569874003;  --- bf55 Generianje CSV datoteke iz rokovnika 

SELECT * from batch_log;

SELECT * from batch_log where bat_def_id = 1569874003  order by recording_time asc;



