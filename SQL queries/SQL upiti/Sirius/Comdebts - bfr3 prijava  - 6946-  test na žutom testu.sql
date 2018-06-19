SET CURRENT_SCHEMA = 'SIP'


			SELECT
                    pa.PARAMETER_VALUE                
                 FROM
                      PARAM_APPLICATION pa,          
                      PARAMS_DICTIONARY pd
                 WHERE
                      pa.PAR_DIC_ID = pd.PAR_DIC_ID




                  	SELECT
                    pa.PARAMETER_VALUE                
                 FROM
                      PARAM_APPLICATION pa,          
                      PARAMS_DICTIONARY pd
                 WHERE
                      pa.PAR_DIC_ID = 61999                
                     AND pd.NAME = 'DPD_HNB_PARAMETER_NAME';


-------------------------------

                  	SELECT
                    pa.PARAMETER_VALUE                
                 FROM
                      PARAM_APPLICATION pa,          
                      PARAMS_DICTIONARY pd
                 WHERE                                  
                     pd.NAME = 'DPD_HNB_PARAMETER_NAME';

                     


		SELECT
                  *          
                 FROM
                      PARAM_APPLICATION pa       
                  


               
                  	SELECT
                    pa.PARAMETER_VALUE                
                 FROM
                      PARAM_APPLICATION pa,          
                      PARAMS_DICTIONARY pd
                 WHERE
                      pa.PAR_DIC_ID = 61999; 
                             
   ----- Parameter_value je 9

----------------------------------------
select * from counter_list;

select * from batch_log where bat_def_id = 6259167704;

select * from event where eve_typ_id = 6259192704;



select * from event where eve_id = 1444450882003;  --- to je ok    to je ok 29.10.2013




SELECT *
FROM
	citizen ci1,
	customer_account ca
		LEFT OUTER JOIN counter_list cl ON cl.cus_acc_id = ca.cus_acc_id AND cl.pro_met_code = 'HNB' AND cl.process_date = '2013-10-31'
WHERE 
	ca.cus_id = ci1.cus_id
	AND (ci1.pay_list_flag = '1' OR cl.nnb_no_days >= 2)
	AND (cl.nnb_no_days >= 2)
	AND ca.ban_pro_typ_id = 790661005
	AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
	AND ca.opening_date <= '2013-10-31'
	AND ( ( ca.cus_acc_status <> 'I' AND ((ca.closing_date IS NULL) OR (ca.closing_date >= '2013-08-30')) )
		OR ( (ca.cus_acc_status = 'I' AND ca.chg_stat_date >= '2013-08-30') ) )
	AND ca.bank_sign = 'RB'
ORDER BY ca.cus_acc_id, cl.nnb_no_days desc;





-------------------------------------------

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
                    cl.process_date cl_process_date,
                   
                    
                FROM
                    citizen ci1,
                    customer_account ca
                        LEFT OUTER JOIN counter_list cl ON cl.cus_acc_id = ca.cus_acc_id AND cl.pro_met_code = 'HNB' AND cl.process_date = '2013-10-30'
                WHERE 
                    ca.cus_id = ci1.cus_id
                   -- AND ( ci1.pay_list_flag = '1' )
                    AND (ci1.pay_list_flag = '1' OR cl.nnb_no_days >= 9)
                    AND ca.ban_pro_typ_id = 790661005
                    AND ca.cus_acc_status NOT IN ('X', 'C', 'E')
                    AND ca.opening_date <= '2013-10-30'
                    AND ( ( ca.cus_acc_status <> 'I' AND ((ca.closing_date IS NULL) OR (ca.closing_date >= '2013-08-30')) )
                        OR ( (ca.cus_acc_status = 'I' AND ca.chg_stat_date >= '2013-08-30') ) )
                   -- AND ca.org_uni_id = :(org_uni_id)
                    AND ca.bank_sign = 'RB'
                ORDER BY
                    ca.cus_acc_id,
                    cl.nnb_no_days desc


---- provjera dana kašnjenja

select * from counter_list;

	select * from counter_list where cus_acc_no IN (3234977233,3200458910,3200458643,3200458580,3200458104,3200457925,3200457642,3200457265,
3200457048,3200456682,3200454568,3200453877,3200453422,3200452794,3200452583,3200451308,
3200450707,3200450410,3200450372,3200449561,3200448577,3200447664,3200445921,3200445817,
3200445751,3200445655,3200445288,3200444603,3200444113,3200443612,3200443401,3200442378,
3200442183,3200442020,3200441792,3200441514,3200441145,3200440101,3200439584,3200439314,
3200438422,3200436113,3200435688,3200434907,3200434861,3200434095,3200434046,3200433274,
3200432595,3200431035,3200430681,3200430544,3200429462,3200427740,3200425936,3200425065,
3200424878,3200424851,3200422350,3200422317,3200418888,3200418513,3200418351,3200418255,
3200418020,3200416642,3200416345,3200416265,3200414376,3200413402,3200412810,3200412422,
3200412326,3200412220,3200209197,3200201893,3200201300,3200181698,3200180210,3200166082,
3200162436,3200158082,3200157287,3200152266,3200134205,3200133155,3200131166,3200125745,
3200123166,3200112335,3200104505,3200095111,3200094406,3200082055,3200076575,3200074645,
3200053593,3200044460,3200029187,3200010957)
AND pro_met_code = 'HNB' AND process_date = '2013-10-30'
and nnb_no_days >= 9;


----

select * from batch_def where bat_def_name like 'bnC2%';   -- 1,026,594,144

select * from batch_log where  bat_def_id = 1026594144;



SELECT cus_acc_no, process_date, nnb_no_days FROM COUNTER_LIST where 
process_date = '2013-10-30'
and cus_acc_id in (SELECT cus_acc_id FROM CUSTOMER_ACCOUNT where CUS_ACC_NO in 
('3234977233','3200458910','3200458643','3200458580','3200458104','3200457925','3200457642','3200457265','3200457048','3200456682','3200454568','3200453877','3200453422'))
and pro_met_id = 8057995612
and BANK_SIGN = 'RB'
with ur; 



select * from batch_log where  bat_def_id = 6259167704;   -- obrada bfR3 se i dalje vrti na Žutom testu  - 31.10.2013


---------------  

SELECT count(*) FROM counter_list where PROCESS_DATE = '2013-11-03' and MODULE_NAME = 'RET' and PRO_MET_ID in (8057995612,8057995611) with ur; 
--- SQL vraæa 93587 zapisa iz counter liste za 2013-11-03



SELECT cus_acc_no, process_date, nnb_no_days FROM COUNTER_LIST where 
process_date = '2013-11-03'
and cus_acc_id in (SELECT cus_acc_id FROM CUSTOMER_ACCOUNT where CUS_ACC_NO in 
('3234977233','3200458910','3200458643','3200458580','3200458104','3200457925','3200457642','3200457265','3200457048','3200456682','3200454568','3200453877','3200453422'))
and pro_met_id = 8057995612
and BANK_SIGN = 'RB'
with ur; 

----------------------- nova prijava  10814 bfR3 SQL code = -811


select * from customer_account;

select * FROM customer_account where cus_acc_id = 324552053522;
---  


select * FROM cusacc_risk where cus_acc_id = 324552053522  ;  -- postoje tri zapisa


select * from customer_account;
    