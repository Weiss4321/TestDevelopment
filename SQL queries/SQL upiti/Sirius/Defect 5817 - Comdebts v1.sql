
SET CURRENT_SCHEMA = 'SIP'

select * from counter_list where cus_acc_no = 3200125745 and process_date between '01.08.2013' and '01.10.2013'

---- bFR2 stanja teku�ih ra�una prijava 5817

select * from batch_log where bat_def_id = 6253448704 order by exec_start_time desc ;

select * from bank_product_type;

select * from customer_account;

select * from customer_account where cus_acc_no = '8000108607';  -- ok

select acc_cur_id from customer_account where cus_acc_no = '8000108607';  -- ok  -- to su euri i oni bi trebali biti u datoteci - Za�to nisu????

--   



		SELECT 
                 CUR_ID             
              FROM
                  PROC_MEMBER
              WHERE
                  PRO_MEM_ID = 'RB'


--- acc_cur_id = 64999

			SELECT *  FROM
                    currency
                    Where cur_id = '64999';    --- to je euro i tu su oni u pravu

                    
			SELECT *  FROM
                    currency
                    Where cur_id = '63999';   -- to su kune  
-----


 		SELECT
                ca.cus_acc_id,
                ca.ban_pro_typ_id,
                ca.ban_rel_typ_id,
                ca.old_cus_acc_no,
                ca.cus_acc_no,
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
                ca.pur_sub_id,
                ca.pro_cat_id,
                ca.ove_typ_id,
                bpt.code bank_product_type_code,
                ci.cus_id,
                ci.code cus_code,
                ci.register_no,
                ci.name,
                ci.surname
            FROM
                customer_account ca
                INNER JOIN bank_product_type bpt ON ca.ban_pro_typ_id  IN (37722005,37723005,2245837003,2245840003,2550768003)
                INNER JOIN citizen ci ON ca.cus_id = '57520251'
            WHERE
                ca.cus_acc_status NOT IN ('I', 'X', 'C', 'E')
                --AND ca.org_uni_id = '18253'
                AND ca.opening_date <= '14.10.2013' 
                AND ( ca.closing_date IS NULL OR ca.closing_date >= '14.01.2013'  )
                AND ca.bank_sign = 'RB'
                AND ca.ban_pro_typ_id IN (
                        790652005,
                        790653005,
                        790654005,
                        790655005,
                        790656005,
                        790657005,
                        790658005,
                        790659005,
                        790660005,
                        790662005,
                        790663005,
                        790664005,
                        790665005,
                        795983005,
                        795984005,
                        795985005,
                        795986005,
                        3767010394,
                        3767013254,
                        3767023264,
                        3767024694,
                        3767066164,
                        3767067594 )
               -- AND ci.pay_list_flag = '1';



---- 

select * from due_term_table;


select * from bank_product_type;

select * from bank_product_type where ban_rel_type = '250';  -- okviri

select * from bank_product_type where ban_rel_type = '005';  -- akreditivi

select * from bank_product_type where ban_rel_type IN ('250','005');  ---- dohvat vrsta akreditiva i okvira koji se zapisuju u datoteku





--------------   provjera
select * from currency where cur_id = 63999;   -- kune HRK

		--Dohvate domace valute
        --domesticCurrencyId = bfr21.getDomesticCurId();
  ----- getDomesticCurId()   -- >  
                
                SELECT 
                  CUR_ID           
              FROM
              PROC_MEMBER
              WHERE
                 PRO_MEM_ID = 'RB';

---------------------   getCurCode
                  SELECT
                     code_num                 
                  FROM
                      currency
                  WHERE 
                     cur_id = 63999;

  ---------     lineData.currencyNumCode = bfr22.getCurCode(domesticCurrencyId);    --- i zapisuje se 191

  
---- cusacc_balance  -- tablica Saldo partije

select * from cusacc_balance;

--- amo_type -- vrsta iznosa

select * from amount_type;  --- opisi vrsta iznosa ( naknadne naplate, provizije, potra�ivanja i sl)


-- za ra�un 
select * from customer_account where cus_acc_no = '8000108607';  -- ok

select acc_cur_id from customer_account where cus_acc_no = '8000108607';  -- ok  -- to su euri i oni bi trebali biti u datoteci - Za�to nisu????  -- acc_cur_id = 64999

---- 118

		SELECT
                org_uni_id,
                code
            FROM
                organization_unit
                where code = '118';    --- org_uni_id = 35253

  select * from cusacc_balance;

  select * from bank_relation_type where ban_rel_typ_id = 37721005;   --- 005

  select * from bank_product_type where ban_rel_type = '005';  -- code = 806 i 807 

---- provjera �to se sve dohva�a za offbalanceInCur
--- u common YFW10 proslije�ujemo sljede�e podatke
--  cus_acc_id          							395761921322
-- bank_product_type_code
--   setBan_rel_type
---  ban_pro_typ_id                                  37722005
-- datum - Date    -- to je datum          14-10-2013
--- 

--   private Date datum = null;         -- '14.10.2013'
--	private BigDecimal org_uni_id = null;   --  35253 
--	private BigDecimal cus_acc_id = null;  -- 395761921322
---	private BigDecimal cus_id = null;                --- 17021251
--	private String ban_rel_type = null;    -- 
--	private String ban_pro_code = null;
--     private String amountType = null;

  -- minbalanceDate = 2012-01-01

  --- SQL iz commona

  
  							SELECT	cus.org_uni_domicil_id org_uni_id,
									orgun.code org_uni_code,
									cusbal.cus_acc_id cus_acc_id,
									RTRIM(custac.cus_acc_no) cus_acc_no,
									cusbal.cur_id cur_id,
									RTRIM(curr.code_num) code_num,
									cusbal.cus_id cus_id, 
									RTRIM(cus.register_no) register_no,
									cus.name name,
									cus.code code,
									cusbal.account account,
									cusbal.balance balance,
									excrat.midd_rate midd_rate,
					                cloc.latest_d,
					                scv.sys_code_desc available_use_desc,
				                    ct.term_date_until expiry_date,
				                    ctt.ter_tab_date maturity_date,
					                con.contract_no,
                                    ct1.num_days as postponed_days,
                                    ct2.term_date_until as exp_mat_date,
                                    cap1.par_value as from_event,
					                cusbal.ban_rel_type
						
							FROM 	cusacc_balance cusbal LEFT OUTER JOIN con_lett_of_credit cloc
                                                                  ON cusbal.cus_acc_id = cloc.cus_acc_id
                                                                  LEFT OUTER JOIN cusacc_param cap
                                                                  ON cap.cus_acc_id = cusbal.cus_acc_id AND cap.p_contract_id = 'available_use' AND '14.10.2013' between cap.date_from and cap.date_until
                                                                  LEFT OUTER JOIN system_code_value scv
                                                                  ON cap.par_value = scv.sys_code_value AND scv.sys_cod_id = 'available_use',
									customer cus
										LEFT OUTER JOIN organization_unit orgun 
													ON cus.org_uni_domicil_id =  orgun.org_uni_id,
									customer_account custac
				                        LEFT OUTER JOIN (SELECT DISTINCT ctt3.cus_acc_id, ctt3.ter_tab_date FROM cusacc_term_table ctt3
                                                                WHERE ctt3.ter_tab_date = (select max(ter_tab_date) from cusacc_term_table ctt2
                                                                                                        where ctt2.cus_acc_id = ctt3.cus_acc_id
                                                                                        AND current date BETWEEN ctt2.date_from AND ctt2.date_until)
                                                                       AND current date BETWEEN ctt3.date_from AND ctt3.date_until) ctt
                                                        ON ctt.cus_acc_id = custac.cus_acc_id,
									currency curr,
									bank_product_type bprt,
									exchange_rate excrat,
                                    cusacc_term ct LEFT OUTER JOIN contract con 
                                                        ON con.cus_acc_id = ct.cus_acc_id 
                                                        AND con.contract_status='A' 
                                                        AND '14.10.2013' between con.activation_date AND con.closing_date
                                                   LEFT OUTER JOIN cusacc_term ct1
                                                        ON ct1.cus_acc_id = ct.cus_acc_id
                                                        AND ct1.p_contract_id = 'postponed_days'
                                                        AND '14.10.2013' between ct1.date_from and ct1.date_until
                                                   LEFT OUTER JOIN cusacc_term ct2
                                                        ON ct2.cus_acc_id = ct.cus_acc_id
                                                        AND ct2.p_contract_id = 'exp_mat_date'
                                                        AND '14.10.2013' between ct2.date_from and ct2.date_until
                                                   LEFT OUTER JOIN cusacc_param cap1
                                                        ON cap1.cus_acc_id = ct.cus_acc_id
                                                        AND cap1.p_contract_id = 'from_event'
                                                        AND '14.10.2013' between cap1.date_from and cap1.date_until
								
							WHERE  	cusbal.cus_id = cus.cus_id
									AND cus.cus_id = 17021251
									AND cusbal.cus_acc_id = custac.cus_acc_id 
									AND bprt.ban_pro_typ_id = custac.ban_pro_typ_id
									AND custac.cus_acc_id = 395761921322
									AND cusbal.cur_id = curr.cur_id
									AND excrat.cur_id = cusbal.cur_id
									AND excrat.date_from <= '14.10.2013'
									AND excrat.date_until >= '14.10.2013'
									AND cusbal.ban_rel_type IN ('004', '005', '009', '011')
									AND cus.org_uni_domicil_id = 35253
									AND RTRIM(cusbal.ban_rel_type) LIKE '005'
									AND RTRIM(bprt.code) LIKE '806'
									AND cusbal.balance_date = ( SELECT MAX ('14.10.2013')
																FROM cusacc_balance cusb 
																WHERE cusb.balance_date <= '14.10.2013'
																AND cusb.cus_id = cusbal.cus_id 
																AND cusb.cus_acc_id = cusbal.cus_acc_id
																AND cusb.acc_num_id = cusbal.acc_num_id
																AND cusb.account = cusbal.account 
																AND cusb.ban_rel_type = cusbal.ban_rel_type 
																AND cusb.amo_type = cusbal.amo_type 
																AND cusb.cur_id = cusbal.cur_id 
																AND cusb.pro_typ_id = cusbal.pro_typ_id 
																AND cusb.bank_sign = 'RB'
					                                            AND cusb.balance_date >= '2012-01-01'
															)
                                    AND cusbal.balance_date >= '2012-01-01'
									AND cusbal.bank_sign = 'RB'
				                    AND ct.cus_acc_id = custac.cus_acc_id
				                    AND ct.p_contract_id = 'valid_term'
				                    AND current date BETWEEN ct.date_from AND ct.date_until
				                    AND cusbal.amo_type = 'null'
									ORDER BY orgun.code, cusbal.cus_id, custac.cus_acc_no;



