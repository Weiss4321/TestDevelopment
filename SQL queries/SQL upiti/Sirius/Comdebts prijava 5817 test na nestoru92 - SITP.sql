SET CURRENT_SCHEMA = 'SITP'

-- test datoteke sa nestora92  -- 

select * from customer_account where cus_acc_no ='6000001982';  -- 250 - code  860  kunski okvir

select * from customer_account where cus_acc_no ='6000001999';  -- 250  -code   - 861 devizni okvir

select * from customer_account where cus_acc_no ='1100147111';  -- 001  -  code redovan devizni raèun za pravne osobe

select * from customer_account where cus_acc_no ='6000000598';   -- 250 - code 860 kunski okvir


select * from bank_product_type where ban_pro_typ_id = '2245837003' ;


where ban_rel_type_id IN ('005','250')