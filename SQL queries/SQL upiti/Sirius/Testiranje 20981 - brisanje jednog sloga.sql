

SET CURRENT_SCHEMA = SIDEV




select * from batch_log where bat_log_id = '6723086601'

select * from batch_file_log where bat_def_id = '3755231484';

select * from batch_log;

select * from batch_file_log where bat_def_id = '6441891704' with ur; --- bl20

select * from batch_log  where bat_def_id = '6441891704' order by user_lock desc with ur;    --- bl20

select * from batch_file_log where bat_def_id = '6441891704' order by user_lock desc with ur;  --- bl20


select * from event  where eve_id = '6485592704';

select * from income_item;

select * from TRANSACTION_TYPE;






select * from income_item where inc_hea_id = '455827019';

select * from income_type;




SELECT inc_typ_id, inc_typ_code, inc_typ_desc FROM income_type ORDER BY inc_typ_code;   -- lookup za income type upit -- to je lookup za Vrstu paketa - ne QBE za prvo polje

select * FROM income_subtype;

select inc_sub_typ_id, inc_sub_typ_code, inc_sub_typ_desc FROM income_subtype ORDER BY inc_sub_typ_code;   --- gotovi upit za moj lookup.xml  - 

-- moj mali ljubimac srušio select into promijenio sam ga na 101




--UPDATE INCOME_SUBTYPE set INC_SUB_TYPE_CODE = '678' where inc_sub_typ_id = '1010060421';




SELECT   INC_SUB_TYP_DESC            
                    FROM    
                            income_subtype
                    WHERE  
                            inc_sub_typ_code = '100' with ur;

                   
         ------------ gotov SQL za   CL032.sqlj          			

				SELECT
						INC_SUB_TYP_DESC            
                    FROM    
                            	income_subtype
                    WHERE 
                            	inc_sub_typ_code = '100'
                            
                    AND   	date_from between '2010-09-01' and '2013-12-01' with ur;
                    					

------------------------------------

select * FROM income_type  WHERE inc_typ_id NOT IN '3999';



SELECT
ih.inc_hea_id IncHeaID ,
ih.inc_typ_id IncTypID,
ih.total_cur_id,
it.inc_typ_code,
it.inc_typ_desc,
ih.inc_hea_no,
ih.file_date,
ih.total_item_count,
ih.total_amount,
cur.code_char,
ih.package_status,
ih.pckg_fin_status,
ih.error_code,
ih.error_desc,
ih.value_date,
CURRENT TIMESTAMP userLock,
ih.payor_register_no,
cur.code_num,
ih.payor_code,
ih.payor_name,
ih.posting_time,
ou.code ,
ih.closing_date ,
ih.payor_bank_cou_id,
ih.PAYOR_TAX_NUM,
ih.INC_FILE_SOURCE,
ih.EXEC_TYPE,
ih.PAYOR_IBAN,
ih.MIDPAYOR_TAX_NUM
FROM INCOME_HEAD ih LEFT OUTER
JOIN INCOME_TYPE it ON ih.inc_typ_id = it.inc_typ_id LEFT OUTER
JOIN CURRENCY cur ON cur.cur_id = ih.total_cur_id LEFT OUTER
JOIN ORGANIZATION_UNIT ou ON ou.org_uni_id = ih.org_uni_id
WHERE ih.bank_sign = 'RB'
AND it.inc_typ_id NOT IN '153467019'
AND ih.package_status IN ('0','0','0','0','null')AND it.inc_typ_code = '20'
ORDER BY ih.inc_hea_no DESC




select * from income_item where inc_hea_id = '455637019';   ----   plamen zagreb d.o.o. 


select * from income_head;

select * from turnover where account = '150461';

select * from customer;   -- npr. register_no = 8505  - Crosco

select * from customer where register_no = '387428';   --- polje Isplatitelj - na ekranu Detalji paketa 

select * from CUSACC_EXTERNAL;

select * from CUSACC_EXTERNAL where register_no = '387428';

--UPDATE INCOME_SUBTYPE set INC_SUB_TYPE_CODE = '678' where inc_sub_typ_id = '1010060421';
select * from CUSACC_EXTERNAL where cus_name like 'Ž-ÈUTURA%';     --- Ž-ÈUTURA TRGOVINA D.O.O.  register no 492797   -- IBAN_ACC_NO je trenutno prazan
--------------------------------------
UPDATE CUSACC_EXTERNAL set IBAN_ACC_NO = 'HR4524840081100001234' WHERE CUS_ACC_EXT_ID = '170865022';    ---  update IBAN_ACC_NO 

commit;
--------------------------------

select * from income_item;


select * from income_item where benef_reg_no = '492797';

select * from income_head;

-----------------------------------------------------------

select * from income_subtype;   -- inc_sub_type_code  --- 


select
inc_sub_typ_desc
from income_subtype
where inc_sub_typ_code = '100'; --- upit za dohvat opisa vrste primanja


select code_char from currency where cur_id = '63999';    --- to je to

select * from currency;



select * from income_item where inc_hea_id = '1010229421';   -- GLOBAL COLLECT B.V. 



SELECT
ih.inc_hea_id IncHeaID ,
ih.inc_typ_id IncTypID,
ih.total_cur_id,
it.inc_typ_code,
it.inc_typ_desc,
ih.inc_hea_no,
ih.file_date,
ih.total_item_count,
ih.total_amount,
cur.code_char,
ih.package_status,
ih.pckg_fin_status,
ih.error_code,
ih.error_desc,
ih.value_date,
CURRENT TIMESTAMP userLock,
ih.payor_register_no,
cur.code_num,
ih.payor_code,
ih.payor_name,
ih.posting_time,
ou.code ,
ih.closing_date ,
ih.payor_bank_cou_id,
ih.PAYOR_TAX_NUM,
ih.INC_FILE_SOURCE,
ih.EXEC_TYPE,
ih.PAYOR_IBAN,
ih.MIDPAYOR_TAX_NUM
FROM INCOME_HEAD ih LEFT OUTER
JOIN INCOME_TYPE it ON ih.inc_typ_id = it.inc_typ_id LEFT OUTER
JOIN CURRENCY cur ON cur.cur_id = ih.total_cur_id LEFT OUTER
JOIN ORGANIZATION_UNIT ou ON ou.org_uni_id = ih.org_uni_id
WHERE ih.bank_sign = 'RB'
AND it.inc_typ_id NOT IN '153467019'
AND ih.package_status IN ('N','K','P','P','P')
AND ih.payor_iban = 'HR0724840083605000276'
ORDER BY ih.inc_hea_no DESC;                         ---------- payor iban



select * from income_head ih where ih.inc_typ_id = 1008282421;

select * from income_head ih where ih.inc_typ_id = 1008282421 and ih.pay_iban_or_acc_no like  'HR7624070001100318864' ;   --   ih.pay_iban_or_acc_no    to staviti u dATOTEKU


select * from income_head ih where ih.inc_typ_id = 1008282421 AND ih.payor_iban like  '%HR07248400836050002%';       

select * from income_head ih where  ih.payor_iban like  '%07248400836050002%';       

AND ih.payor_iban =  'HR0724840083605000276';


AND ih.payor_iban like '%HR0724840083605000276%';


select * from income_head ih where ih.inc_hea_no = 'P130000000240';   --- pretraživanje po inc_hea_no



-------------------------------------- upit po uzorku   ---------------


					SELECT 
								ii.inc_ite_id
								, ii.item_ord_no
								, ii.benef_cus_code
								, cu.name
								, ii.benef_cus_acc_no
								, cr.code_char
								, ii.amount
								, ii.item_status
								, scv1.sys_code_desc scv1desc
								, ii.financial_status
								, scv2.sys_code_desc scv2desc
								, ii.error_code
								, ii.error_desc
								, CURRENT TIMESTAMP ct
								, ii.eve_id
								, ii.external_flag
                                		, ii.iban
                                		, ii.iban_or_acc_no
							FROM 
								currency cr
								, system_code_value scv1
								, system_code_value scv2
								, income_item ii 
								LEFT OUTER JOIN customer cu
									ON ii.benef_cus_id = cu.cus_id
							WHERE 
								ii.inc_hea_id = 1009976421                              					 --- polje Broj raèuna je benefCusAccNo   IBAN primatelja je 
								AND cr.cur_id = 63999
								AND scv1.sys_code_value = '4'
								--AND scv1.sys_cod_id = 'item_status       '
								--AND scv2.sys_code_value = ii.financial_status
								---AND scv2.sys_cod_id = 'pckg_i_status     '
								AND (ii.benef_cus_acc_no like 'HR0624840083251005002' OR ii.iban like 'HR0624840083251005002')             ----- AND (ii.benef_cus_acc_no like :(isIBANorAccNo) OR ii.iban like :(benefCusAccNo))
					            AND ( 0 = 0 OR (ii.iban_or_acc_no like 'HR0624840083251005002' OR ii.iban like 'HR0624840083251005002'))                        ------ AND (:(isIBANorAccNoEntered)= 0 OR (ii.iban_or_acc_no like :(isIBANorAccNo)))
								AND ii.item_status like '4'
								AND ii.financial_status like '0'
								AND ii.bank_sign = 'RB'
							ORDER BY ii.item_ord_no with ur;


---------------------------  HR011234123412341235    ili --- 3205000286
------------ na razvojnom testu - IBAN -- HR0724840083605000276


------------------------  nova varijanta upita


					SELECT 
								ii.inc_ite_id
								, ii.item_ord_no
								, ii.benef_cus_code
								, cu.name
								, ii.benef_cus_acc_no
								, cr.code_char
								, ii.amount
								, ii.item_status
								, scv1.sys_code_desc scv1desc
								, ii.financial_status
								, scv2.sys_code_desc scv2desc
								, ii.error_code
								, ii.error_desc
								, CURRENT TIMESTAMP ct
								, ii.eve_id
								, ii.external_flag
                                		, ii.iban
                                		, ii.iban_or_acc_no
							FROM 
								currency cr
								, system_code_value scv1
								, system_code_value scv2
								, income_item ii 
								LEFT OUTER JOIN customer cu
									ON ii.benef_cus_id = cu.cus_id
							WHERE 
								ii.inc_hea_id = 455827019                              					 --- polje Broj raèuna je benefCusAccNo   IBAN primatelja je 
								AND cr.cur_id = 63999
								AND scv1.sys_code_value = '4'
								--AND scv1.sys_cod_id = 'item_status       '
								--AND scv2.sys_code_value = ii.financial_status
								---AND scv2.sys_cod_id = 'pckg_i_status     '
								AND ( (ii.benef_cus_acc_no like '%' OR ii.iban like 'HR011234123412341235')  OR ( 0 = 0 OR (ii.iban_or_acc_no like '%' OR ii.iban like '%')) )      
								AND ii.item_status like '4'
								AND ii.financial_status like '0'
								AND ii.bank_sign = 'RB'
							ORDER BY ii.item_ord_no with ur;



----- AND (ii.benef_cus_acc_no like :(isIBANorAccNo) OR ii.iban like :(benefCusAccNo))	
 ------ AND (:(isIBANorAccNoEntered)= 0 OR (ii.iban_or_acc_no like :(isIBANorAccNo)))


select * from income_head where inc_hea_no = 'P130000000285';


select * from income_item where inc_hea_id =  '1010485421';
 
select
inc_sub_typ_desc
from income_subtype
where inc_sub_typ_code = '100'; --- upit za dohvat opisa vrste primanja

select * from income_head where inc_hea_no = 'P130000000240';

SELECT  benef_cus_acc_no, iban, iban_or_acc_no from income_item where  inc_ite_id = '1009963421';


select * from counter_list;

select nnb_no_days from counter_list;


select nnb_no_days from counter_list where nnb_no_days IS NULL;








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
		LEFT OUTER JOIN counter_list cl ON cl.cus_acc_id = ca.cus_acc_id AND cl.pro_met_code = 'HNB' AND cl.process_date = '25-04-2013'
WHERE 		
	ca.cus_id = 6833996551	
	AND (ci1.pay_list_flag = '1' OR cl.nnb_no_days >= 2)	
	AND ca.ban_pro_typ_id = 790661005	
	AND ca.cus_acc_status NOT IN ('X', 'C', 'E')	
	AND ca.opening_date <= '2013-07-07'	
	AND ( ( ca.cus_acc_status <> 'I' AND ((ca.closing_date IS NULL) OR (ca.closing_date >= '2013-07-07')) )	
		OR ( (ca.cus_acc_status = 'I' AND ca.chg_stat_date >= '2013-05-08') ) )
	AND ca.bank_sign = 'RB'	
ORDER BY ca.cus_acc_id, cl.nnb_no_days desc with ur;		


---------------------------------


select * from CUSACC_BALANCE_LAST; 




select * from cusacc_balance where 
				CUR_ID = 63999
				AND cus_acc_id = 127098022;
                   

select * from customer_account where cus_acc_no = '3200000373';




select * from customer_account where cus_acc_no = '3200001567'; -- cus_acc_id = 198106022
-----------------------

select * from cusacc_balance where cus_acc_id = 198106022 ;   ---- ovdje æu testirati   cusacc_bal_id = 5316182797  -- pro_typ_id = 826,790,005   -- 826790005

--- promijeniti pro_typ_id - i onda æu vidjeti koju vrijednost dohvaæa



update cusacc_balance set balance = '58689.09' where cusacc_bal_id = 5316182797;    --  setiranje nove vrijednosti za - stara vrijednost 58,689.09


commit;   -- za aktiviranje novog sta


---------------
select * from customer_account; -- tu su brojevi raèuna iz datoteke

select * from customer_account where cus_acc_id = 198106022;  -- cus_acc_no = 3200001567

select * from cusacc_balance_last;


select *from cusacc_balance_last where cus_acc_id = 198106022  and cus_bal_last_id = 4784449797 ;   --- njega promijeniti

select * from  cusacc_balance_last where cus_bal_last_id = 4784449797;   -- pro_typ_id = 826,790,005

update cusacc_balance_last set balance = '58694.06' where cus_bal_last_id = 4784449797;   --- setiranje nove vrijednosti za - stara vrijednost 58694.06

commit;

---------------------------------------------------------------

select * from cusacc_balance_last where cus_bal_last_id = 4784449797;


--------------  slog koji treba ponovno vratiti u tablicu nakon brisanja sloga

INSERT INTO "SIDEV   "."CUSACC_BALANCE_LAST" (CUS_BAL_LAST_ID,CUS_ACC_ID,ACC_NUM_ID,CUS_ID,ACCOUNT,BAN_REL_TYPE,AMO_TYPE,PRO_TYP_ID,AVAIL_BALANCE_FLAG,CUS_ACC_REPORT,BALANCE_DATE,CUR_ID,DEBIT_TOTAL,CREDIT_TOTAL,BALANCE,ORD_NO,BANK_SIGN,CUS_ORG_UNI_ID,CUS_SUB_ACC_ID,CUS_SUB_ACC_TBL,CLAUSE_BALANCE,USER_LOCK) VALUES (4784449797,198106022,1015377005,466852251,'80434     ','912','100',826790005,'1','1',{d '2012-05-31'},63999,14598.76,73292.82,58694.06,1,'RB',null,null,null,0.00,{ts '2012-11-15 13:06:53.428749'});

commit;

--------------------------------------

delete from cusacc_balance_last where cus_bal_last_id = 4784449797;

commit;


