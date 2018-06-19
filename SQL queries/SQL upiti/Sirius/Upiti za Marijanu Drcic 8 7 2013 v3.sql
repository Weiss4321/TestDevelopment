
SET CURRENT_SCHEMA = 'SIP';


----------------------------------- pocetak BFR3 upit -- dorada upita 10 7 2013 -----------------------------------

SELECT *
FROM
	citizen ci1,
	customer_account ca
		LEFT OUTER JOIN counter_list cl ON cl.cus_acc_id = ca.cus_acc_id AND cl.pro_met_code = 'HNB' AND cl.process_date = '2013-07-07'
WHERE 
	ca.cus_id = ci1.cus_id
	AND (ci1.pay_list_flag = '1' OR cl.nnb_no_days >= 2)
	AND ca.ban_pro_typ_id = 790661005
	AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
	AND ca.opening_date <= '2013-07-07'
	AND ( ( ca.cus_acc_status <> 'I' AND ((ca.closing_date IS NULL) OR (ca.closing_date >= '2013-07-07')) )
		OR ( (ca.cus_acc_status = 'I' AND ca.chg_stat_date >= '2013-05-08') ) )
	AND ca.bank_sign = 'RB'
ORDER BY ca.cus_acc_id, cl.nnb_no_days desc;


------------------------ kraj BFR3-----------------------------------------------------


SELECT *
FROM
	citizen ci1,
	customer_account ca
		LEFT OUTER JOIN counter_list cl ON cl.cus_acc_id = ca.cus_acc_id AND cl.pro_met_code = 'HNB' AND cl.process_date = '2013-07-07'
WHERE 
	ca.cus_id = ci1.cus_id
	AND (ci1.pay_list_flag = '1' OR cl.nnb_no_days >= 2)
	AND ca.ban_pro_typ_id = 790661005
	AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
	AND ca.opening_date <= '2013-07-07'
	AND ( ( ca.cus_acc_status <> 'I' AND ((ca.closing_date IS NULL) OR (ca.closing_date >= '2013-07-07')) )
		OR ( (ca.cus_acc_status = 'I' AND ca.chg_stat_date >= '2013-05-08') ) )
	AND ca.bank_sign = 'RB'
ORDER BY ca.cus_acc_id, cl.nnb_no_days desc;

--------------------------- upit BFR3  Mirna   -------------------------------------
--- u ovom upitu dohvaæamo ukupan broj zapisa

with racuni as (SELECT ca.cus_acc_id
	FROM
	citizen ci1,
	customer_account ca
		LEFT OUTER JOIN counter_list cl ON cl.cus_acc_id = ca.cus_acc_id AND cl.pro_met_code = 'HNB' AND cl.process_date = '2013-07-09'
WHERE 
	ca.cus_id = ci1.cus_id
	AND (ci1.pay_list_flag = '1' OR cl.nnb_no_days >= 2)
	AND ca.ban_pro_typ_id = 790661005
	AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
	AND ca.opening_date <= '2013-07-09'
	AND ( ( ca.cus_acc_status <> 'I' AND ((ca.closing_date IS NULL) OR (ca.closing_date >= '2013-07-09')) )
		OR ( (ca.cus_acc_status = 'I' AND ca.chg_stat_date >= '2013-05-10') ) )
	AND ca.bank_sign = 'RB'
ORDER BY ca.cus_acc_id, cl.nnb_no_days desc
),

brojevi as (select count(cus_acc_id) br from racuni 
union
select count(r.cus_acc_id) br from
racuni r, cusacc_rel_person crp where r.cus_acc_id = crp.cus_acc_id AND crp.DATE_FROM <= '09.07.2013'
                AND crp.DATE_UNTIL >= '09.07.2013')
                
select sum(br) from brojevi with ur;


------------------------ BFR2 --  stanja na tekucim racunima ------------------------------------


SELECT *
FROM
	customer_account ca
                INNER JOIN bank_product_type bpt ON ca.ban_pro_typ_id = bpt.ban_pro_typ_id
                INNER JOIN citizen ci ON ca.cus_id = ci.cus_id
	WHERE
     	ca.cus_acc_status NOT IN ('I', 'X', 'C', 'E')
              AND ca.opening_date <= '2013-07-07'
             AND ( ca.closing_date IS NULL OR ca.closing_date >= '2013-07-07' )
                AND ca.bank_sign = 'RB'
             AND ca.ban_pro_typ_id IN (790652005,790653005,790654005,790655005,790656005,790657005,790658005, 790659005, 790660005, 790662005, 790663005, 790664005, 790665005, 795983005, 795984005, 795985005, 795986005, 3767010394, 3767013254, 3767023264, 3767024694, 3767066164, 3767067594)
             AND ci.pay_list_flag = '1';



------------------------ BFR2 -- kraj stanja na tekucim racunima ------------------------------------




