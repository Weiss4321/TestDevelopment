

SET CURRENT_SCHEMA = 'SIP';

---  ci.pay_list_flag,ca.ban_pro_typ_id,ca.cus_acc_status,chg_stat_date,cl.process_date,t.turnover_desc,t.credit_amount,t.cus_id,t.tur_id,t.system_timestamp
---   ca.cus_acc_no, t.process_timestamp

SELECT  ci.pay_list_flag,ca.ban_pro_typ_id,ca.cus_acc_status,chg_stat_date,cl.process_date,t.turnover_desc,t.credit_amount,t.cus_id,t.tur_id,t.system_timestamp,ca.cus_acc_no, t.process_timestamp
FROM customer_account ca, citizen ci, counter_list cl, turnover t, event_trx ex, turn_customer tc
where
ca.cus_acc_id = 238166003
and ca.BAN_PRO_TYP_ID = 790661005  --- ??
AND ca.cus_acc_status NOT IN ('X', 'C', 'E') 
AND ca.bank_sign = 'RB'
AND t.cus_id = 5663041551
AND ci.name = 'ŽELJKO'
AND ci.surname = 'BELOŠEVIÆ'
AND tc.process_date = '08.07.2013'
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


------------------ dodao cl.nnb_no_days  -----------------

SELECT distinct
ci.pay_list_flag,cl.nnb_no_days,ca.ban_pro_typ_id,ca.cus_acc_status,chg_stat_date,cl.process_date,t.turnover_desc,t.credit_amount,t.cus_id,t.tur_id,t.system_timestamp,ca.cus_acc_no, t.process_timestamp
FROM customer_account ca, citizen ci, counter_list cl, turnover t, event_trx ex, turn_customer tc
where
ca.cus_acc_id = 238166003
and ca.BAN_PRO_TYP_ID = 790661005  --- ??
AND ca.cus_acc_status NOT IN ('X', 'C', 'E') 
AND ca.bank_sign = 'RB'
AND t.cus_id = 5663041551
AND ci.name = 'ŽELJKO'
AND ci.surname = 'BELOŠEVIÆ'
AND tc.process_date = '08.07.2013'
AND (ci.pay_list_flag = '1' OR cl.nnb_no_days >= 2 );    --- nnb days je broj dana kašnjenja u odnosu da na datum puštanja obrade
                                      

------------------------------------------


select * from counter_list  where cus_acc_id = 238166003;


and NNB_date > '01.07.2013';
 
--- usporediti user_lock u odosu na kraj izvršenja obrade koji je 04:39:00 8.7.2013

--     counter_list        cl  ON cl.cus_acc_id = ca.cus_acc_id AND cl.pro_met_code = 'HNB' AND cl.process_date = :(process_date)

select * from counter_list cl  WHERE  cl.process_date = '07.07.2013' AND cl.pro_met_code = 'HNB' AND cl.cus_acc_no = '3234044911';   --- user_lock je 2013-07-08 02:01:30.777467 što je prije od 02:36:55 

select nnb_no_days from counter_list cl  WHERE  cl.process_date = '07.07.2013' AND cl.pro_met_code = 'HNB' AND cl.cus_acc_no = '3234044911';   --- user_lock je 2013-07-08 02:01:30.777467 što je prije od 02:36:55 
------------ NNB_NO_DAYS je 35 je za 3234044911


select nnb_no_days from counter_list cl, CUSTOMER_ACCOUNT ca  WHERE  cl.process_date = '07.07.2013' AND cl.pro_met_code = 'HNB' AND cl.cus_acc_id = ca.cus_acc_id and  ca.cus_acc_id= 29665636522; 
--- NNB_NO_DAYS je 35

select nnb_no_days from counter_list cl, CUSTOMER_ACCOUNT ca  WHERE  cl.process_date = '07.07.2013' AND cl.pro_met_code = 'HNB' AND cl.cus_acc_id = ca.cus_acc_id and  ca.cus_acc_id= 238166003; 







----------------------------------------------
select * from counter_list cl  WHERE   cl.cus_acc_no = '3223846176'; 

select * from counter_list cl  WHERE  cl.process_date > '01.07.2013'  AND cl.cus_acc_no = '3223846176'; 

select * from counter_list cl  WHERE  cl.process_date = '07.07.2013' AND cl.pro_met_code = 'HNB' AND cl.cus_acc_no = '3223846176'; 

select nnb_no_days from counter_list cl  WHERE  cl.process_date = '07.07.2013' AND cl.pro_met_code = 'HNB' AND cl.cus_acc_no = '3223846176';



------------------- cijeli upit koji trebam prilagoditi uvjetima puštanja obrade i pustiti BFR3 --- 16.7.2013 moram ispuniti SQL sa podacima od dana puštanja obrade i tako ju pokrenuti
--- upit sa org_uni_id

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
                        LEFT OUTER JOIN counter_list        cl  ON cl.cus_acc_id = ca.cus_acc_id AND cl.pro_met_code = 'HNB' AND cl.process_date = '07.07.2013'
                WHERE 
                    ca.cus_id = ci1.cus_id
                    AND (ci1.pay_list_flag = '1' OR cl.nnb_no_days >= 2
                    AND ca.ban_pro_typ_id = 790661005
                    AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
                    AND ca.opening_date <= '07.07.2013'
                    AND ( ( ca.cus_acc_status <> 'I' AND ((ca.closing_date IS NULL) OR (ca.closing_date >= '07.07.2013')) )
                        OR ( (ca.cus_acc_status = 'I' AND ca.chg_stat_date >= '08.05.2013') ) )
                  AND ca.org_uni_id = :(org_uni_id)
                    AND ca.bank_sign = 'RB'
                ORDER BY
                    ca.cus_acc_id,
                    cl.nnb_no_days desc;




			
              




-------------------------------------- ok dohvaæa
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
	AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
	AND ca.opening_date <= '07.07.2013'
	AND ( ( ca.cus_acc_status <> 'I' AND ((ca.closing_date IS NULL) OR (ca.closing_date >= '07.07.2013')) )
		OR ( (ca.cus_acc_status = 'I' AND ca.chg_stat_date >= '08.05.2013') ) )
	AND ca.bank_sign = 'RB'
ORDER BY ca.cus_acc_id

--------------------------------------------

-------------------------------------- ok dohvaæa  ali sada sa ca.cus_acc_id= 29665636522
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









                                       