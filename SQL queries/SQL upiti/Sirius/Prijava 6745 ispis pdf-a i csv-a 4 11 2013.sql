SET CURRENT_SCHEMA = 'SIDEV'


select * from income_head;

select * from customer_account;

select * from citizen;


select * from batch_log;

--- prijava 6745 ispis pdf-a i csv

select * from batch_log where recording_time > '2013-11-04 00:00:00.000000'order by exec_start_time ; --- sve obrade koje su danas zadane

select * from customer_account where cus_acc_no = '3200008644';  -- testni raèun za Jadranku Skubiæ paket P070000001082

select * from customer where cus_id = 465649251;  -- Jadranka Skubiæ  -- CODE -> JMBG 1309933330219  

-- meðutim potrebno je ispisati cus_acc_no 3200008644

