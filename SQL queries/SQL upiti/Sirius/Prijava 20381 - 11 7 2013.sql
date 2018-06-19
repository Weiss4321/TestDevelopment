

SET CURRENT_SCHEMA = 'SIP';



select * from CUSTOMER_ACCOUNT with ur;

select * from CUSTOMER_ACCOUNT ca where ca.cus_acc_id = 29665636522 with ur;  --- to je ID od Ivane Šegote 
select * from CUSTOMER_ACCOUNT ca where ca.cus_acc_id = 238166003  with ur;  --- to je ID od Beloševiæ Željko

select * from turn_customer with ur;

select * from turnover with ur;

select * from turn_customer where value_date = '08.07.2013'   with ur;

select * from CUSTOMER_ACCOUNT  where cus_acc_no = '3234044911'  with ur;   --- dohvaæam raèun za Šegota Ivana    -- contract_no  je isti kao i cus_acc_no partija klijenta  -- CUS_ID = 5663041551

select * from CUSTOMER_ACCOUNT  where cus_acc_no = '3223846176'  with ur;   --- dohvaæam raèun za Beloševiæ Željko    -- contract_no  je isti kao i cus_acc_no  -- CUS_ID = 16123125551


select * from turnover where cus_id = 5663041551 and account = '80404' and accounting_date > '01.07.2013' with ur;  -- dohvaæam priljeve 1  -- i Plaæa -VETERINARSKA STANICA D.D.,DO

select * from turnover where cus_id = 16123125551 and account = '80404' and accounting_date > '01.07.2013' with ur;  -- dohvaæam priljeve 1  -- NAKNADA ZA UDŽBENIKE   i Naknada za FlexiIDEAL        

------------------- dohvaæam podatke OK
SELECT * FROM customer_account ca 
where ca.cus_acc_id = 29665636522 and ca.BAN_PRO_TYP_ID = 790661005 AND ca.cus_acc_status NOT IN ('X', 'C', 'E') AND ca.bank_sign = 'RB';

----------------------------   dohvaæam podatke OK
SELECT * FROM customer_account ca, citizen ci, counter_list cl
where ca.cus_acc_id = 29665636522 and ca.BAN_PRO_TYP_ID = 790661005 AND ca.cus_acc_status NOT IN ('X', 'C', 'E') AND ca.bank_sign = 'RB'
AND (ci.pay_list_flag = '1' OR cl.nnb_no_days >= 2 );

----------------------------  ne dohvaæam podatke ali upit prolazi  -- tj neznam da li dobijam praznu datoteku
SELECT  ca.cus_acc_no, t.process_timestamp FROM customer_account ca, citizen ci, counter_list cl, turnover t
where ca.cus_acc_id = 29665636522 and ca.BAN_PRO_TYP_ID = 790661005 AND ca.cus_acc_status NOT IN ('X', 'C', 'E') AND ca.bank_sign = 'RB'
AND (ci.pay_list_flag = '1' OR cl.nnb_no_days >= 2 )
AND t.credit_amount <> 0;  --- kada imam ovaj uvjet upit traje vjeèno

----------------------------   dohvaæam podatke OK  treba mu 240 sekundi  -- Ivana Šegota
SELECT  ca.cus_acc_no, t.process_timestamp FROM customer_account ca, citizen ci, counter_list cl, turnover t, event_trx ex
where
ca.cus_acc_id = 29665636522
and ca.BAN_PRO_TYP_ID = 790661005 
AND ca.cus_acc_status NOT IN ('X', 'C', 'E') 
AND ca.bank_sign = 'RB'
AND t.cus_id = 5663041551
AND ci.name = 'IVANA'
AND ci.surname = 'ŠEGOTA'
AND (ci.pay_list_flag = '1' OR cl.nnb_no_days >= 2 )
AND t.tur_typ_id NOT IN (
                                203992005, 203991005, 203990005, 203989005,     /* Prometi preknjizenja */
                                272350005, 272356005, 272357005, 272366005,     /* Prometi preknjizenja po po domicilu, sektoru i komitentu */
                                1561010887, 1561012317, 1561039487, 1561040917, 1561055217, 1561070947, 1561128147, 1561129577, 1561133867, 1561135297, 1901718005, 1901719005,     /* Rezervacije */
                                3718517667, 3850379797,     /* Prijenosi na utuzeno potrazivanje */
                                3850379797)                 /* Preknjizenje duga po utuzenim */
                        AND (ex.tra_typ_id <> 1426436003   /* Kamate za Opci nalog */
                                OR t.tur_typ_id NOT IN (120348005, 120362005, 867912005, 867916005, 867920005, 867924005, 867928005, 
                                                        984509005, 984719005, 984722005, 984723005, 989237005, 1173130005, 989239005, 984721005,
                                                        4020407797, 4020408797, 4034626797, 4034629797, 4034630797, 4034632797, 4034660797))                     
                        AND ex.tra_typ_id NOT IN (
                                4680657704, 4680661704, 4670626704, 4680665704, 4680669704, 4680673704,
                                1685855003, 1685858003, 1685861003, 1685880003, 1685883003, 1685886003, 1685900003, 1685903003, 1685906003,
                                1700949003, 1700984003, 1700987003, 1701043003, 1701046003, 1701049003, 1700952003, 1700955003, 1700981003)     /* Kamate */
                      AND (ca.cus_acc_status <> 'W'
                                       OR t.tur_typ_id NOT IN(3994573797,3994572797))  ;

-----------------------------------------------------------------------------------------------------------------------------



 select name, surname from citizen where name ='IVANA' and surname = 'ŠEGOTA';     --- dohvaæam ime IVANA ŠEGOTA


SELECT * FROM EVENT WHERE eve_typ_id = 6266764704 AND value_date IS NOT NULL AND ext_event_num IS NOT NULL ORDER BY value_date desc   -- dohvat iz tablice event kada je kreirana datoteka bfR4 


----------------------------   dohvaæam podatke OK   -- Beloševiæ Željko
SELECT  ca.cus_acc_no, t.process_timestamp FROM customer_account ca, citizen ci, counter_list cl, turnover t, event_trx ex
where
ca.cus_acc_id = 238166003
and ca.BAN_PRO_TYP_ID = 790661005  --- ??
AND ca.cus_acc_status NOT IN ('X', 'C', 'E') 
AND ca.bank_sign = 'RB'
AND t.cus_id = 5663041551
AND ci.name = 'ŽELJKO'
AND ci.surname = 'BELOŠEVIÆ'
AND (ci.pay_list_flag = '1' OR cl.nnb_no_days >= 2 )
AND t.tur_typ_id NOT IN (
                                203992005, 203991005, 203990005, 203989005,     /* Prometi preknjizenja */
                                272350005, 272356005, 272357005, 272366005,     /* Prometi preknjizenja po po domicilu, sektoru i komitentu */
                                1561010887, 1561012317, 1561039487, 1561040917, 1561055217, 1561070947, 1561128147, 1561129577, 1561133867, 1561135297, 1901718005, 1901719005,     /* Rezervacije */
                                3718517667, 3850379797,     /* Prijenosi na utuzeno potrazivanje */
                                3850379797)                 /* Preknjizenje duga po utuzenim */
                        AND (ex.tra_typ_id <> 1426436003   /* Kamate za Opci nalog */
                                OR t.tur_typ_id NOT IN (120348005, 120362005, 867912005, 867916005, 867920005, 867924005, 867928005, 
                                                        984509005, 984719005, 984722005, 984723005, 989237005, 1173130005, 989239005, 984721005,
                                                        4020407797, 4020408797, 4034626797, 4034629797, 4034630797, 4034632797, 4034660797))                     
                        AND ex.tra_typ_id NOT IN (
                                4680657704, 4680661704, 4670626704, 4680665704, 4680669704, 4680673704,
                                1685855003, 1685858003, 1685861003, 1685880003, 1685883003, 1685886003, 1685900003, 1685903003, 1685906003,
                                1700949003, 1700984003, 1700987003, 1701043003, 1701046003, 1701049003, 1700952003, 1700955003, 1700981003)     /* Kamate */
                      AND (ca.cus_acc_status <> 'W'
                                       OR t.tur_typ_id NOT IN(3994573797,3994572797))  ;

-----------------------------------------------------------------------------------------------------------------------------





