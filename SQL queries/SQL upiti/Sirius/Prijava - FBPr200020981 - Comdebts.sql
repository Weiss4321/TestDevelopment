SET CURRENT_SCHEMA = 'SIP'


select * from counter_list;   --- counter lista 
--- raèuni  3208260147 i 3216047015

--Datoteka stanja tekuæih raèuna iz obrade BFR3 nema podatke o danu kašnjenja. 
-- 3208260147 ima nedopušteno prekoraèenje od 16.07.2010 i broje mu se dani 
-- no stanje raèuna u datotekama stanja je 0.00. 
--Isti sluèaj je i sa stanje TR 3216047015. 

--Oba raèuna su blokirana. Molim hitno provjeru i ispravak. 

select * from counter_list where cus_acc_no = '3208260147'; -- daje rezultate  -- 2260 zapisa u bazi 

select * from counter_list where cus_acc_no = '3216047015'; -- daje rezultate

select * from counter_list where cus_acc_no = '3216047015' and pro_met_code = 'HNB'; 

---  dohvat podataka BFR3

  SELECT
                    ca.cus_acc_id,
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
                    ci1.code,
                    ci1.register_no,
                    ci1.name,
                    ci1.surname,
                    cl.nnb_no_days,
                    cl.nnb_date,
                    cl.absorbing_status,
                    cl.abs_status_date,
                    cl.process_date 
                FROM
                    citizen ci1,
                    customer_account ca
                        LEFT OUTER JOIN counter_list cl ON cl.cus_acc_id = ca.cus_acc_id AND cl.pro_met_code = 'HNB' AND cl.process_date = '11-11-2012'
                WHERE 
                    ca.cus_id = 6833996551                
                    AND ( ( ca.cus_acc_status <> 'I' AND ((ca.closing_date IS NULL) OR (ca.closing_date >= '25-04-2013')) )
                        OR ( (ca.cus_acc_status = 'I' AND ca.chg_stat_date >= '11-11-2012') ) )
                    AND ca.org_uni_id = '14253'
                    AND ca.bank_sign = 'RB'
                ORDER BY
                    ca.cus_acc_id,
                    cl.nnb_no_days desc






                    -------- 

                    SELECT 
                    ca.cus_acc_id,
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
                    ci1.code,
                    ci1.register_no,
                    ci1.name,
                    ci1.surname,
                    cl.nnb_no_days,
                    cl.nnb_date,
                    cl.absorbing_status,
                    cl.abs_status_date,
                    cl.process_date                   	
FROM		
	citizen ci1,	
	customer_account ca	
		LEFT OUTER JOIN counter_list cl ON cl.cus_acc_id = ca.cus_acc_id AND cl.pro_met_code = 'HNB' AND cl.process_date = '2013-04-25'
WHERE 		
	ca.cus_id = 6833996551	
	AND (ci1.pay_list_flag = '1' OR cl.nnb_no_days >= 2)	
	AND ca.ban_pro_typ_id = 790661005	
	AND ca.cus_acc_status NOT IN ('X', 'C', 'E')	
	AND ca.opening_date <= '2013-01-01'	
	AND ( ( ca.cus_acc_status <> 'I' AND ((ca.closing_date IS NULL) OR (ca.closing_date >= '2013-07-07')) )	
		OR ( (ca.cus_acc_status = 'I' AND ca.chg_stat_date >= '2013-05-08') ) )
	AND ca.bank_sign = 'RB'	
ORDER BY ca.cus_acc_id, cl.nnb_no_days desc		



select * from CUSACC_BALANCE_LAST; 
		
		

select * FROM procedure_type;

select * FROM procedure_type where pro_typ_id = 790661005;

select * from bank_product_type;   

-------------------     Prijava 21086 

select * from counter_list;   --- counter lista 

select * from counter_list where cus_acc_no = 3208260147;   --- counter lista -- 

--- 3200125745

-- 3200125745   ovaj raèun je potrebno istražiti - za njega nisu dohvaæeni raèuni

select * from counter_list where cus_acc_no = 3200125745 and ban_pro_typ_id = 790661005 and process_date between  '01.08.2013' and '01.10.2013';   ---  dohvaæa se 14 zapisa

select * from counter_list where cus_acc_no = 3200125745;     -- 790,661,005


select * from counter_list where cus_acc_no = 3200125745 and ban_pro_typ_id = 790661005 fetch first 1000 rows only; 
----------------

select * from counter_list where cus_acc_no = 3200125745 and ban_pro_typ_id = 790661005  and process_date > '01.01.2013' ; 

--------------------------------------------------------  cus_acc_id = 1231003

---   AND cl.PROCESS_DATE = '14.10.2013'   
---  AND cl.PROCESS_DATE = '29.09.2013'

		SELECT
                 NNB_NO_DAYS,
                 ABSORBING_STATUS          
              FROM
              COUNTER_LIST cl
             WHERE
                     cl.CUS_ACC_ID = 1231003
                 AND cl.PRO_MET_CODE = 'RI'
                AND cl.PROCESS_DATE = '14.10.2013'   -- nema podataka


                SELECT
                 NNB_NO_DAYS,
                 ABSORBING_STATUS          
              FROM
              COUNTER_LIST cl
             WHERE
                     cl.CUS_ACC_ID = 1231003
                 AND cl.PRO_MET_CODE = 'HNB'
                AND cl.PROCESS_DATE = '14.10.2013'   -- nema podataka

---------------------------------------------------  za ovaj raèun bi trebalo biti podataka 320826014
                
select * from counter_list where cus_acc_no = 320826014 and ban_pro_typ_id = 790661005  and process_date > '01.08.2013' ; 

select * from customer_account;

select * from customer_account where iban_acc_no like 'HR4724840083208260147';  -- cus_id = 1718521551  -- dohvat tekuæeg raèuna 

select * from customer_account where cus_acc_no = '3208260147';  --- jedan te isti raèun   ---- problematièni raèun -- cus_acc_id = 82495003

select * from customer_account where cus_acc_no = '3200125745';  --- jedan te isti raèun   ---- problematièni raèun -- cus_acc_id = 1231003

select * from counter_list where cus_acc_no = 3200125745   -- dohvat iz counter liste


select * from counter_list where cus_acc_no = 3200125745   -- dohvat iz counter liste

---- raèun bez dana kašnjenja  3200125745 za navedene datume

   			SELECT
                 NNB_NO_DAYS,
                 ABSORBING_STATUS          
              FROM
              COUNTER_LIST cl
             WHERE
                     cl.CUS_ACC_ID = 1231003
                 AND cl.PRO_MET_CODE = 'HNB'
                AND cl.PROCESS_DATE = '14.10.2013'

--- dohvaæa se ništa za HNB NNB_NO_DAYS  


		SELECT
                 NNB_NO_DAYS,
                 ABSORBING_STATUS          
              FROM
              COUNTER_LIST cl
             WHERE
                     cl.CUS_ACC_ID = 1231003
                 AND cl.PRO_MET_CODE = 'RI'
                AND cl.PROCESS_DATE = '14.10.2013'

--- dohvaæa se ništa za RI NNB_NO_DAYS  



		SELECT
                 NNB_NO_DAYS,
                 ABSORBING_STATUS          
              FROM
              COUNTER_LIST cl
             WHERE
                     cl.CUS_ACC_ID = 1231003
                 AND cl.PRO_MET_CODE = 'RI'
                AND cl.PROCESS_DATE = '07.09.2013'
