SET CURRENT_SCHEMA = 'SIDEV'

select * from customer_account;

select * from income_type;

---- place
select * from income_head where inc_typ_id = 56322019;

select inc_hea_id,inc_hea_no,payor_register_no,file_date,value_date,total_item_count,total_amount from income_head where inc_typ_id = 56322019;

select * from income_head where inc_hea_id = 1017657421;

select * from income_head where inc_hea_id = 1015241421;

select * from income_item;

-------------- podaci o raèunima koje mogu pokušati iskorititi za unos
select * from income_item where inc_hea_id = 1015241421 ;

select inc_hea_id,inc_hea_no,payor_register_no,file_date,value_date,total_item_count,total_amount from income_head where inc_typ_id = 56322019 and inc_hea_id = 1015241421 ;