SET CURRENT_SCHEMA = 'SITP'


--- prijava 6755 - testiranje 6.11.2013 OK -- vratiti prijavu

select * from customer_account where cus_acc_no = '3200009102';

select * from turnover t , turn_customer tc where t.tur_id = tc.tur_id and tc.cus_acc_id = 7935610 and date (system_timestamp) = current_date with ur;

select * from event where EVE_ID in (2277727607,2277733607) with ur;


select * from income_type where eve_typ_id = 1689659003 with ur; --- to bi bilo ok

select * from turnover where tra_id = 2277728607 with ur;
