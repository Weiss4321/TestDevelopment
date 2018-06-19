

SET CURRENT_SCHEMA = 'SIP';



select * from CUSTOMER_ACCOUNT with ur;

select * from CUSTOMER_ACCOUNT ca where ca.cus_acc_id = 29665636522 with ur;  --- to je ID od Ivane Šegote 
select * from CUSTOMER_ACCOUNT ca where ca.cus_acc_id = 238166003  with ur;  --- to je ID od Beloševiæ Željko

select * from turn_customer with ur;

select * from turnover with ur;

select * from turn_customer where value_date = '08.07.2013'   with ur;

select * from CUSTOMER_ACCOUNT  where cus_acc_no = '3234044911'  with ur;   --- dohvaæam raèun za Šegota Ivana    -- contract_no  je isti kao i cus_acc_no partija klijenta  -- CUS_ID = 5663041551

select * from CUSTOMER_ACCOUNT  where cus_acc_no = '3223846176'  with ur;   --- dohvaæam raèun za Beloševiæ Željko    -- contract_no  je isti kao i cus_acc_no  -- CUS_ID = 16123125551


select * from turnover where cus_id = 5663041551 and account = '80404' and accounting_date > '01.07.2013' with ur;  -- dohvaæam priljeve 1  -- i Plaæa -VETERINARSKA STANICA D.D.,DO   Šegota Ivana

select * from turnover where cus_id = 16123125551 and account = '80404' and accounting_date > '01.07.2013' with ur;  -- dohvaæam priljeve 1  -- NAKNADA ZA UDŽBENIKE   i Naknada za FlexiIDEAL   Beloševiæ Željko       

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



 select name, surname, pay_list_flag from citizen where name ='IVANA' and surname = 'ŠEGOTA';     --- dohvaæam ime IVANA ŠEGOTA


SELECT * FROM EVENT WHERE eve_typ_id = 6266764704 AND value_date IS NOT NULL AND ext_event_num IS NOT NULL ORDER BY value_date desc;   -- dohvat iz tablice event kada je kreirana datoteka bfR4 


----------------------------   dohvaæam podatke OK   -- Beloševiæ Željko 1 upit
SELECT  ca.cus_acc_no, t.process_timestamp FROM customer_account ca, citizen ci, counter_list cl, turnover t, event_trx ex, turn_customer tc
where
ca.cus_acc_id = 238166003
and ca.BAN_PRO_TYP_ID = 790661005  --- ??
AND ca.cus_acc_status NOT IN ('X', 'C', 'E') 
AND ca.bank_sign = 'RB'
AND t.cus_id = 5663041551
AND ci.name = 'ŽELJKO'
AND ci.surname = 'BELOŠEVIÆ'
AND tc.process_date >= '01.07.2013'
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


----------------------------   dohvaæam podatke OK   -- Beloševiæ Željko 2 upit   -select iz wherea upisati 
SELECT  ca.cus_acc_no, t.process_timestamp FROM customer_account ca, citizen ci, counter_list cl, turnover t, event_trx ex, turn_customer tc
where
ca.cus_acc_id = 238166003
and ca.BAN_PRO_TYP_ID = 790661005  --- ??
AND ca.cus_acc_status NOT IN ('X', 'C', 'E') 
AND ca.bank_sign = 'RB'
AND t.cus_id = 5663041551
AND ci.name = 'ŽELJKO'
AND ci.surname = 'BELOŠEVIÆ'
AND tc.process_date >= '01.07.2013'
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


---------------------------------------------------  dohvat ---- 15.7.2013  --  što se sve dohvaæa a što ne
 --AND t.cus_id = 5663041551 --- BŽ

 				SELECT 
                      ci.pay_list_flag,ca.ban_pro_typ_id,ca.cus_acc_status,chg_stat_date,cl.process_date,t.turnover_desc,t.credit_amount,t.cus_id,t.tur_id,t.system_timestamp
                    FROM
                        turn_customer tc
                        INNER JOIN turnover t ON tc.tur_id = t.tur_id
                        INNER JOIN event_trx ex ON t.tra_id = ex.tra_id
                        INNER JOIN turnover_type tt ON t.tur_typ_id = tt.tur_typ_id
                        INNER JOIN customer_account ca ON tc.cus_acc_id = ca.cus_acc_id                
                        INNER JOIN citizen ci ON ca.cus_id = ci.cus_id
                        LEFT OUTER JOIN counter_list cl ON ( 
                                ca.cus_acc_id = cl.cus_acc_id 
                                AND cl.pro_met_code = 'HNB' 
                                AND cl.process_date >= '06.07.2013' )
                    WHERE
                        t.tur_id = 1474396593321
                    ORDER BY
                        ca.cus_acc_no,
                        t.process_timestamp;



select cus_id from CUSTOMER_ACCOUNT  where cus_acc_no = '3234044911'  with ur; --- dohvaæam raèun za Šegota Ivana    -- contract_no  je isti kao i cus_acc_no partija klijenta  -- CUS_ID = 5663041551

select cus_id from CUSTOMER_ACCOUNT  where cus_acc_no = '3223846176'  with ur;   --- dohvaæam raèun za Beloševiæ Željko    -- contract_no  je isti kao i cus_acc_no  -- CUS_ID = 16123125551

select * from turnover where cus_id = 5663041551 and account = '80404' and accounting_date > '01.07.2013' with ur;  -- dohvaæam priljeve 1  -- i Plaæa -VETERINARSKA STANICA D.D.,DO   Šegota Ivana

select * from turnover where cus_id = 16123125551 and account = '80404' and accounting_date > '01.07.2013' with ur;  -- dohvaæam priljeve 1  -- NAKNADA ZA UDŽBENIKE   i Naknada za FlexiIDEAL   Beloševiæ Željko       

select system_timestamp,process_timestamp from turnover where cus_id = 16123125551 and account = '80404' and accounting_date > '01.07.2013' with ur;  -- dohvaæam priljeve 1  -- NAKNADA ZA UDŽBENIKE   i Naknada za FlexiIDEAL   Beloševiæ Željko   


---------------------------------------------------  dohvat ---- 15.7.2013  --  2 dio


 				SELECT 
                      ca.ban_pro_typ_id,ca.cus_acc_status,chg_stat_date,cl.nnb_no_days,tc.process_date,cl.process_date,t.credit_amount,t.tur_typ_id,ex.tra_typ_id,ci.pay_list_flag,cl.nnb_no_days
                    FROM
                        turn_customer tc
                        INNER JOIN turnover t ON tc.tur_id = t.tur_id
                        INNER JOIN event_trx ex ON t.tra_id = ex.tra_id
                        INNER JOIN turnover_type tt ON t.tur_typ_id = tt.tur_typ_id
                        INNER JOIN customer_account ca ON tc.cus_acc_id = ca.cus_acc_id                
                        INNER JOIN citizen ci ON ca.cus_id = ci.cus_id
                        LEFT OUTER JOIN counter_list cl ON ( 
                                ca.cus_acc_id = cl.cus_acc_id 
                                AND cl.pro_met_code = 'HNB' 
                                AND cl.process_date >= '06.07.2013' )
                    WHERE
                        t.tur_id = 1474396593321
                    ORDER BY
                        ca.cus_acc_no,
                        t.process_timestamp;


-------------------------------------------   Batch Log  15.7.2013   za SIRIUS RBA Razvojni test -- TestSirdb4

select * from batch_log where bat_def_id = 6266762704;     ---   batch_log za bfR4  -- 12.7.2013 puštena je zadnja obrada

select * from batch_log where bat_def_id = 6259167704;     ---   batch_log za bfR3  -- 11.7.2013 puštena je zadnja obrada

select * from batch_log where bat_def_id = 6253448704;      ---   batch_log za bfR2  -- 12.7.2013 puštena je zadnja obrada


------------------   bfr2    -----  puštam za provjeru

14.07.2013   -- za 14.7.2013 dobijam 2860 zapisa  koliko ima i slogova u datoteci 

with racuni as(
SELECT ca.cus_acc_id
FROM
customer_account ca
INNER JOIN bank_product_type bpt ON ca.ban_pro_typ_id = bpt.ban_pro_typ_id
INNER JOIN citizen ci ON ca.cus_id = ci.cus_id
WHERE
ca.cus_acc_status NOT IN ('I', 'X', 'C', 'E')
AND ca.opening_date <= '2013-07-13'
AND ( ca.closing_date IS NULL OR ca.closing_date >= '2013-07-13' )
AND ca.bank_sign = 'RB'
AND ca.ban_pro_typ_id IN (790652005,790653005,790654005,790655005,790656005,790657005,790658005, 790659005, 790660005, 790662005, 790663005, 790664005, 790665005, 795983005, 795984005, 795985005, 795986005, 3767010394, 3767013254, 3767023264, 3767024694, 3767066164, 3767067594)
AND ci.pay_list_flag = '1')
, stanja as ( select cus_acc_id,cur_id from cusacc_balance where pro_typ_id in (
843980005,
843982005,
843984005,
843986005,
843988005,
843990005,
843978005,
843975005,
826791005,
826790005 ) and balance_date between '01.01.2013' and '2013-07-13' and cus_acc_id in (select cus_acc_id from racuni) group by cus_acc_id,cur_id
union
select cus_acc_id, due_ter_tab_cur_id cur_id from due_term_table where 
value_date <= '2013-07-13'
AND pro_typ_id IN (41999, 923675005, 31598005) and cus_acc_id in (select cus_acc_id from racuni) group by cus_acc_id,due_ter_tab_cur_id) 
select count(r.cus_acc_id) from racuni r left outer join stanja s on r.cus_acc_id = s.cus_acc_id with ur;

------------------   bfr3    -----  puštam za provjeru  -- i dobijam 45762  dok u datoteci ima 45760 slogova
with racuni as (SELECT ca.cus_acc_id
FROM
citizen ci1,
customer_account ca
LEFT OUTER JOIN counter_list cl ON cl.cus_acc_id = ca.cus_acc_id AND cl.pro_met_code = 'HNB' AND cl.process_date = '2013-07-15'
WHERE 
ca.cus_id = ci1.cus_id
AND (ci1.pay_list_flag = '1' OR cl.nnb_no_days >= 2)
AND ca.ban_pro_typ_id = 790661005
AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
AND ca.opening_date <= '2013-07-15'
AND ( ( ca.cus_acc_status <> 'I' AND ((ca.closing_date IS NULL) OR (ca.closing_date >= '2013-07-15')) )
OR ( (ca.cus_acc_status = 'I' AND ca.chg_stat_date >= '2013-05-15') ) )
AND ca.bank_sign = 'RB'
ORDER BY ca.cus_acc_id, cl.nnb_no_days desc
),
brojevi as (select count(cus_acc_id) br from racuni 
union
select count(r.cus_acc_id) br from
racuni r, cusacc_rel_person crp where r.cus_acc_id = crp.cus_acc_id AND crp.DATE_FROM <= '15.07.2013'
AND crp.DATE_UNTIL >= '15.07.2013')
select sum(br) from brojevi with ur;

 ------------------   bfr4    -----  puštam za provjeru  -- 42 raèuna sam dobio nazad  -- puštao sam upit za predzadnju obradu bFR3 koja je išla 10-7-2013 u ovom sluèaju

SELECT *
FROM
turn_customer tc
INNER JOIN turnover t ON tc.tur_id = t.tur_id
INNER JOIN event_trx ex ON t.tra_id = ex.tra_id
INNER JOIN turnover_type tt ON t.tur_typ_id = tt.tur_typ_id
INNER JOIN customer_account ca ON tc.cus_acc_id = ca.cus_acc_id 
INNER JOIN citizen ci ON ca.cus_id = ci.cus_id
LEFT OUTER JOIN counter_list cl ON ( 
ca.cus_acc_id = cl.cus_acc_id 
AND cl.pro_met_code = 'HNB' 
AND cl.process_date = '2013-07-15')
WHERE
ca.ban_pro_typ_id = 790661005
AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
AND ( (ca.cus_acc_status <> 'I') OR (ca.cus_acc_status = 'I' AND chg_stat_date >= '2013-05-15') )
AND ca.bank_sign = 'RB'
AND (ci.pay_list_flag = '1' OR cl.nnb_no_days >= 2)
AND tc.process_date >= '2013-07-15' ----------------> što se ovdje bilježi?
AND tc.process_date <= '2013-07-15'
AND t.credit_amount <> 0
AND t.tur_typ_id NOT IN (203992005, 203991005, 203990005, 203989005, 272350005, 272356005, 272357005, 272366005, 1561010887, 1561012317, 1561039487, 1561040917, 1561055217, 1561070947, 1561128147, 1561129577, 1561133867, 1561135297, 1901718005, 1901719005, 3718517667, 3850379797, 3850379797)
AND (ex.tra_typ_id <> 1426436003
OR t.tur_typ_id NOT IN (120348005, 120362005, 867912005, 867916005, 867920005, 867924005, 867928005, 984509005, 984719005, 984722005, 984723005, 989237005, 1173130005, 989239005, 984721005, 4020407797, 4020408797, 4034626797, 4034629797, 4034630797, 4034632797, 4034660797)) 
AND ex.tra_typ_id NOT IN (4680657704, 4680661704, 4670626704, 4680665704, 4680669704, 4680673704, 1685855003, 1685858003, 1685861003, 1685880003, 1685883003, 1685886003, 1685900003, 1685903003, 1685906003, 1700949003, 1700984003, 1700987003, 1701043003, 1701046003, 1701049003, 1700952003, 1700955003, 1700981003)
AND (ca.cus_acc_status <> 'W' OR t.tur_typ_id NOT IN(3994573797, 3994572797)) 
ORDER BY
ca.cus_acc_no,
t.process_timestamp

----------------------------------------------
--dohvaæam raèun za Šegota Ivana     -- CUS_ID = 5663041551

select Pay_list_flag from citizen where cus_id = 5663041551;

 --- dohvaæam raèun za Beloševiæ Željko    -- contract_no  je isti kao i cus_acc_no  -- CUS_ID = 16123125551 
select Pay_list_flag from citizen where cus_id = 16123125551;

--------- '3234044911'   i    '3223846176'

SELECT  ca.cus_acc_no,cl.nnb_no_days, cl.PROCESS_DATE
FROM customer_account ca,  counter_list cl
where ca.cus_acc_no = cl.cus_acc_no
and ca.cus_acc_no = '3234044911' 
and cl.PROCESS_DATE > '01.07.2013'
order by  cl.PROCESS_DATE,cl.nnb_no_days,ca.cus_acc_no;


SELECT  ca.cus_acc_no,cl.nnb_no_days, cl.PROCESS_DATE
FROM customer_account ca,  counter_list cl
where ca.cus_acc_no = cl.cus_acc_no
and ca.cus_acc_no = '3223846176' 
and cl.PROCESS_DATE > '01.07.2013'
order by  cl.PROCESS_DATE,cl.nnb_no_days,ca.cus_acc_no;


   ----      ----- parameter value je 2

 				 SELECT
                    pa.PARAMETER_VALUE
                FROM
                    PARAM_APPLICATION pa,
                    PARAMS_DICTIONARY pd
                WHERE
                    pa.PAR_DIC_ID = pd.PAR_DIC_ID
                    AND pd.NAME = 'DPD_MIN_TR';
                  
select * from counter_list cl
where cl.cus_acc_no = 3234044911
and cl.process_date between '01.07.2013' and '10.07.2013';

select * from counter_list cl
where cl.cus_acc_no = 3223846176
and cl.process_date between '01.06.2013' and '10.07.2013';



 			SELECT
                    pa.parameter_value
                FROM 
                    param_application pa 
                    INNER JOIN params_dictionary pd ON pa.par_dic_id = pd.par_dic_id
                WHERE
                    pd.name = 'DPD_MIN_TR';


------------------------


SELECT ca.cus_acc_id,
                    ca.cus_acc_no,
                    ca.old_cus_acc_no,
                    ca.iban_acc_no,
                    ca.cus_acc_name,
                    ca.org_uni_id,
                    ca.currency_type,
                    ca.acc_cur_id,
                    ca.opening_date,
                    ca.closing_date,
                    ca.cus_acc_status,
                    ca.chg_stat_reason,
                    ca.chg_stat_date,
                    ca.warn_note_status,
                    ca.law_block_status,
                    ca.collection_status,
                    ca.court_proc_status,
                    ca.distraint_flag,
                    ca.ban_rel_typ_id,
                    ca.ban_pro_typ_id,
                    ca.pur_sub_id,
                    ca.pro_cat_id,
                    ci1.cus_id,
                    ci1.code cus_code,
                    ci1.register_no,
                    ci1.name,
                    ci1.surname,
                    cl.nnb_no_days,
                    cl.nnb_date,
                    cl.absorbing_status,
                    cl.abs_status_date,
                    cl.process_date cl_process_date
	FROM
	citizen ci1,
	customer_account ca
		LEFT OUTER JOIN counter_list cl ON cl.cus_acc_id = ca.cus_acc_id AND cl.pro_met_code = 'HNB' AND cl.process_date = '07.07.2013'
WHERE 
	ca.cus_id = ci1.cus_id
	AND (ci1.pay_list_flag = '1' OR cl.nnb_no_days >= 2)
	AND ca.ban_pro_typ_id = 790661005
	AND ca.cus_acc_id= 29665636522
	AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
	AND ca.opening_date <= '07.07.2013'
	AND ( ( ca.cus_acc_status <> 'I' AND ((ca.closing_date IS NULL) OR (ca.closing_date >= '07.07.2013')) )
		OR ( (ca.cus_acc_status = 'I' AND ca.chg_stat_date >= '08.05.2013') ) )
	AND ca.bank_sign = 'RB'
ORDER BY ca.cus_acc_id;


SELECT ca.cus_acc_id,
                    ca.cus_acc_no,
                    ca.old_cus_acc_no,
                    ca.iban_acc_no,
                    ca.cus_acc_name,
                    ca.org_uni_id,
                    ca.currency_type,
                    ca.acc_cur_id,
                    ca.opening_date,
                    ca.closing_date,
                    ca.cus_acc_status,
                    ca.chg_stat_reason,
                    ca.chg_stat_date,
                    ca.warn_note_status,
                    ca.law_block_status,
                    ca.collection_status,
                    ca.court_proc_status,
                    ca.distraint_flag,
                    ca.ban_rel_typ_id,
                    ca.ban_pro_typ_id,
                    ca.pur_sub_id,
                    ca.pro_cat_id,
                    ci1.cus_id,
                    ci1.code cus_code,
                    ci1.register_no,
                    ci1.name,
                    ci1.surname,
                    cl.nnb_no_days,
                    cl.nnb_date,
                    cl.absorbing_status,
                    cl.abs_status_date,
                    cl.process_date cl_process_date
	FROM
	citizen ci1,
	customer_account ca
		LEFT OUTER JOIN counter_list cl ON cl.cus_acc_id = ca.cus_acc_id AND cl.pro_met_code = 'HNB' AND cl.process_date = '07.07.2013'
WHERE 
	ca.cus_id = ci1.cus_id
	AND (ci1.pay_list_flag = '1' OR cl.nnb_no_days >= 2)
	AND ca.ban_pro_typ_id = 790661005
	AND ca.cus_acc_id= 238166003
	AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
	AND ca.opening_date <= '07.07.2013'
	AND ( ( ca.cus_acc_status <> 'I' AND ((ca.closing_date IS NULL) OR (ca.closing_date >= '07.07.2013')) )
		OR ( (ca.cus_acc_status = 'I' AND ca.chg_stat_date >= '08.05.2013') ) )
	AND ca.bank_sign = 'RB'
ORDER BY ca.cus_acc_id;

------------------------------------------ poveæati referentni broj dana kašnjenja na 8 --- datum process date '07.07.2013'

SELECT ca.cus_acc_id,
                    ca.cus_acc_no,
                    ca.old_cus_acc_no,
                    ca.iban_acc_no,
                    ca.cus_acc_name,
                    ca.org_uni_id,
                    ca.currency_type,
                    ca.acc_cur_id,
                    ca.opening_date,
                    ca.closing_date,
                    ca.cus_acc_status,
                    ca.chg_stat_reason,
                    ca.chg_stat_date,
                    ca.warn_note_status,
                    ca.law_block_status,
                    ca.collection_status,
                    ca.court_proc_status,
                    ca.distraint_flag,
                    ca.ban_rel_typ_id,
                    ca.ban_pro_typ_id,
                    ca.pur_sub_id,
                    ca.pro_cat_id,
                    ci1.cus_id,
                    ci1.code cus_code,
                    ci1.register_no,
                    ci1.name,
                    ci1.surname,
                    cl.nnb_no_days,
                    cl.nnb_date,
                    cl.absorbing_status,
                    cl.abs_status_date,
                    cl.process_date cl_process_date
	FROM
	citizen ci1,
	customer_account ca
		LEFT OUTER JOIN counter_list cl ON cl.cus_acc_id = ca.cus_acc_id AND cl.pro_met_code = 'HNB' AND cl.process_date = '07.07.2013'
WHERE 
	ca.cus_id = ci1.cus_id
	AND (ci1.pay_list_flag = '1' OR cl.nnb_no_days >= 8)
	AND ca.ban_pro_typ_id = 790661005
	AND ca.cus_acc_id= 238166003
	AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
	AND ca.opening_date <= '07.07.2013'
	AND ( ( ca.cus_acc_status <> 'I' AND ((ca.closing_date IS NULL) OR (ca.closing_date >= '07.07.2013')) )
		OR ( (ca.cus_acc_status = 'I' AND ca.chg_stat_date >= '08.05.2013') ) )
	AND ca.bank_sign = 'RB'
ORDER BY ca.cus_acc_id;

-----     
select * from COUNTER_LIST where CUS_ACC_ID=238166003 order by user_lock desc;    --- dohvat iz counter liste za CUS_ACC_ID=238166003 gdje se vidi da postoji rupa u datumima
------ process_date 2013-07-07 do 2013-07-05 i onda 2013-05-15 pa prema dolje
---- NNB_NO_DAYS 8  do 6 i onda 46 prema dolje

    

----------------------- ptipremiti upit za drugi datum   ---datum process date  8.7.2013  ------


SELECT ca.cus_acc_id,
                    ca.cus_acc_no,
                    ca.old_cus_acc_no,
                    ca.iban_acc_no,
                    ca.cus_acc_name,
                    ca.org_uni_id,
                    ca.currency_type,
                    ca.acc_cur_id,
                    ca.opening_date,
                    ca.closing_date,
                    ca.cus_acc_status,
                    ca.chg_stat_reason,
                    ca.chg_stat_date,
                    ca.warn_note_status,
                    ca.law_block_status,
                    ca.collection_status,
                    ca.court_proc_status,
                    ca.distraint_flag,
                    ca.ban_rel_typ_id,
                    ca.ban_pro_typ_id,
                    ca.pur_sub_id,
                    ca.pro_cat_id,
                    ci1.cus_id,
                    ci1.code cus_code,
                    ci1.register_no,
                    ci1.name,
                    ci1.surname,
                    cl.nnb_no_days,
                    cl.nnb_date,
                    cl.absorbing_status,
                    cl.abs_status_date,
                    cl.process_date cl_process_date
	FROM
	citizen ci1,
	customer_account ca
		LEFT OUTER JOIN counter_list cl ON cl.cus_acc_id = ca.cus_acc_id AND cl.pro_met_code = 'HNB' AND cl.process_date = '08.07.2013'
WHERE 
	ca.cus_id = ci1.cus_id
	AND (ci1.pay_list_flag = '1' OR cl.nnb_no_days >= null)
	AND ca.ban_pro_typ_id = 790661005
	AND ca.cus_acc_id= 238166003
	AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
	AND ca.opening_date <= '08.07.2013'
	AND ( ( ca.cus_acc_status <> 'I' AND ((ca.closing_date IS NULL) OR (ca.closing_date >= '08.07.2013')) )
		OR ( (ca.cus_acc_status = 'I' AND ca.chg_stat_date >= '09.05.2013') ) )
	AND ca.bank_sign = 'RB'
ORDER BY ca.cus_acc_id;



------ upiti za HNB_NO_DAYS    

select nnb_no_days from counter_list cl, CUSTOMER_ACCOUNT ca  WHERE  cl.process_date = '08.07.2013' AND cl.pro_met_code = 'HNB' AND cl.cus_acc_id = ca.cus_acc_id and  ca.cus_acc_id= 29665636522; 
--- NNB_NO_DAYS nema dohvata

select nnb_no_days from counter_list cl, CUSTOMER_ACCOUNT ca  WHERE  cl.process_date = '08.07.2013' AND cl.pro_met_code = 'HNB' AND cl.cus_acc_id = ca.cus_acc_id and  ca.cus_acc_id= 238166003; 
--- NNB_NO_DAYS nema dohvata 


---- upit za DPD_MIN_TR


 			SELECT
                    pa.PARAMETER_VALUE
                FROM
                    PARAM_APPLICATION pa,
                    PARAMS_DICTIONARY pd
                WHERE
                    pa.PAR_DIC_ID = pd.PAR_DIC_ID
                    AND pd.NAME = 'DPD_MIN_TR';








