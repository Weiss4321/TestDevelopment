SET CURRENT_SCHEMA = SIDEV;



select cus_acc_name from customer_account where ban_pro_typ_id = 4663071704;

select * from customer_account where ban_pro_typ_id = 4663071704;



select * from customer_account;
select * FROM citizen;


select * FROM counter_list;

select * FROM turn_customer;


select * from TURNOVER;

select * from turnover_type  where tur_typ_id = 120348005;  -- kamata
select * from turnover_type  where tur_typ_id = 120362005;  -- dospijeæe kamate
select * from turnover_type  where tur_typ_id = 867912005;  -- pripis pozitivne kamate

-- ovo su kamate

select * from turnover_type  where tur_typ_id = 120348005;  --  kamata
select * from turnover_type  where tur_typ_id =  984509005;  --  pripis pozivne kamate
select * from turnover_type  where tur_typ_id =  4020408797;  --  rasknjižavanje zatezne kamate
select * from turnover_type  where tur_typ_id =  4034626797; -- knjiženje zatezne kamate - t.r.
select * from turnover_type  where tur_typ_id =  4034630797;  -- korekcija zatezne kamate 




select * FROM event_trx where tra_typ_id <> 1426436003;  -- dohvaæa sve što nije u 

-- event trx tablica sadrži zapise o transakcijama???? 
select * FROM event_trx where tra_typ_id = 1426436003; 

select * from customer_account where ban_pro_typ_id = 4663071704 

select * from customer_account;

select * from customer; 

select * from customer_type;


select * from bank_product_type;    -- dohvat tipova proizvoda


select * from bank_product_type where ban_pro_typ_id IN (24999, 37722005, 795985005, 795986005, 23999, 2245837003, 2245840003, 4662964704, 4662965704, 4662968704, 4662969704,
4662970704, 4662971704, 4662972704, 4662979704, 4663049704, 4663050704, 4663051704, 4663053704, 4663054704, 4663057704, 4663058704, 4663060704, 4663061704, 4663063704, 
4663064704, 4663065704, 4663066704, 4663068704, 4663069704, 4663071704);    -- dohvat tipova proizvoda

select ban_pro_typ_id, name from bank_product_type where ban_pro_typ_id IN (24999, 37722005, 795985005, 795986005, 23999, 2245837003, 2245840003, 4662964704, 4662965704, 4662968704, 4662969704,
4662970704, 4662971704, 4662972704, 4662979704, 4663049704, 4663050704, 4663051704, 4663053704, 4663054704, 4663057704, 4663058704, 4663060704, 4663061704, 4663063704, 
4663064704, 4663065704, 4663066704, 4663068704, 4663069704, 4663071704);    -- dohvat tipova proizvoda













;



