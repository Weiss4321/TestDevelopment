SET CURRENT_SCHEMA = SIDEV




select * from income_item

select * FROM APP_USER where user_name like '%Cvitaš%';


delete from app_user where use_id = (469323003)


select * from income_item;

SELECT sum
(ii.amount)
FROM  income_item ii
WHERE 
ii.inc_ite_id = 10790791 and ii.inc_ite_id = 10807951


select * from
income_item  
where
inc_hea_id = '743094421' 
and iban = 'HR2924840083205006121' --- ANTE VILET - prema njegovom IBANU trebam moæi pretraživati
or iban_or_acc_no = 'HR2924840083205006121';
----------------------
select * from
income_item  
where
inc_hea_id = '743094421' 
and iban_or_acc_no = 'HR2924840083205006121';
-------------------------------------

select * from
income_item  
where
inc_hea_id = '743094421' ;



update income_item set iban_or_acc_no = 'HR2924840083205006121' and iban = 'HR2924840083205006121';

commit;


-- paket gdje se nalaze sve 4 stavke
select * from income_item  where inc_hea_id = '743094421'


 

select * from customer_account;

select * from turnover;
---------------------------------------------------------
select * from turnover where tur_id = 1507644415321;


select * from turnover_desc;
----------------------------------
select * from event_trx;

select * from event_trx where tra_typ_id = 1507644433321;

select * from event_trx where eve_id = 1145549948303;

-----------------------------------------------
select * from event;

select * from event where eve_typ_id = 1145549948303;



								SELECT 
								ii.inc_ite_id
								, ii.item_ord_no
								, ii.benef_cus_code
								, cu.name
								, ii.benef_cus_acc_no
								, cr.code_char
								, ii.amount
								, ii.item_status							
								, ii.financial_status
								, ii.error_code
								, ii.error_desc							
								, ii.eve_id
								, ii.external_flag
                                		, ii.iban
							FROM 
								currency cr
								, system_code_value scv1
								, system_code_value scv2
								, income_item ii 
								LEFT OUTER JOIN customer cu
									ON ii.benef_cus_id = cu.cus_id
							WHERE 
								ii.inc_hea_id = 803106421
								AND ii.iban_or_acc_no like 'HR2924840083205006121' 				        		
							
-------------------------------     ovakav upit radi 
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
							FROM 
								currency cr
								, system_code_value scv1
								, system_code_value scv2
								, income_item ii 
								LEFT OUTER JOIN customer cu
									ON ii.benef_cus_id = cu.cus_id
							WHERE 
								ii.inc_hea_id = 803106421
								AND cr.cur_id = ii.cur_id
								AND scv1.sys_code_value = ii.item_status
								AND scv1.sys_cod_id = 'item_status       '
								AND scv2.sys_code_value = ii.financial_status
								AND scv2.sys_cod_id = 'pckg_i_status     '
								AND (ii.benef_cus_acc_no like 'HR2924840083205006121'	 OR ii.iban like 'HR2924840083205006121')
					            	AND (0 = 0 OR (ii.iban_or_acc_no like 'HR2924840083205006121'))										            
								AND ii.item_status like '%'
								AND ii.financial_status like '%'
								AND ii.bank_sign = 'RB'
							ORDER BY ii.item_ord_no; 
---------------------------- sql sa or uvjetom

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
								ii.inc_hea_id = 743094421
								AND cr.cur_id = ii.cur_id
								AND scv1.sys_code_value = ii.item_status
								AND scv1.sys_cod_id = 'item_status       '
								AND scv2.sys_code_value = ii.financial_status
								AND scv2.sys_cod_id = 'pckg_i_status     '
								AND (ii.benef_cus_acc_no like 'HR2924840083205006121'	 OR ii.iban like '%')
					            	AND (0 = 0 OR (ii.iban_or_acc_no like 'HR2924840083205006121' OR (ii.iban_or_acc_no like 'HR2924840083205006121' OR ii.iban like '%')))									            
								AND ii.item_status like '%'
								AND ii.financial_status like '%'
								AND ii.bank_sign = 'RB'
							ORDER BY ii.item_ord_no; 
							
---------   kada unesem IBAN primatelja   dobijem nazad 3 vrijednosti 
----
-- paket gdje se nalaze sve 4 stavke
select * from income_item  where inc_hea_id = '743094421'     --- inc_ite_id = 743088421      i inc_hea_id = '743094421' 

select * from income_item  where inc_ite_id = '743088421'  -- stavka sa ibanom i iban_or_acc_no


SELECT 
								ii.inc_ite_id
								, ii.inc_hea_id
								, ii.item_ord_no
								, ii.benef_cus_code
								, ii.iban
                               		, ii.iban_or_acc_no
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
							FROM 
								currency cr
								, system_code_value scv1
								, system_code_value scv2
								, income_item ii 
								LEFT OUTER JOIN customer cu
									ON ii.benef_cus_id = cu.cus_id
							WHERE 
								ii.inc_hea_id = 743094421
								AND cr.cur_id = ii.cur_id
								AND scv1.sys_code_value = ii.item_status
								AND scv1.sys_cod_id = 'item_status       '
								AND scv2.sys_code_value = ii.financial_status
								AND scv2.sys_cod_id = 'pckg_i_status     '
								--AND (ii.benef_cus_acc_no like '%'	 OR ii.iban like '%')   ---
								AND ( ii.iban_or_acc_no like 'HR2924840083205006121' OR ii.iban like 'HR2924840083205006121')	
					            	--AND (0 = 0 OR (ii.iban_or_acc_no like 'HR2924840083205006121' OR (ii.iban_or_acc_no like 'HR2924840083205006121' OR ii.iban like '%')))									            
								AND ii.item_status like '%'
								AND ii.financial_status like '%'
								AND ii.bank_sign = 'RB'
							ORDER BY ii.item_ord_no; 
--------

SELECT 
								ii.inc_ite_id
								, ii.inc_hea_id
								, ii.item_ord_no
								, ii.benef_cus_code
								, ii.iban
                               		, ii.iban_or_acc_no
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
							FROM 
								currency cr
								, system_code_value scv1
								, system_code_value scv2
								, income_item ii 
								LEFT OUTER JOIN customer cu
									ON ii.benef_cus_id = cu.cus_id
							WHERE 
								ii.inc_hea_id = 743094421 
								AND cr.cur_id = ii.cur_id
								AND scv1.sys_code_value = ii.item_status
								AND scv1.sys_cod_id = 'item_status       '
								AND scv2.sys_code_value = ii.financial_status
								AND scv2.sys_cod_id = 'pckg_i_status     '
								AND (ii.benef_cus_acc_no like '%'  OR ii.iban like '%')   
					            	AND ( 0 = 0 AND (ii.iban_or_acc_no like 'HR2924840083205006121' OR ii.iban like 'HR2924840083205006121' ))			-- 	iban_or_acc_no  - IBAN primatelja					            
								AND ii.item_status like '%'
								AND ii.financial_status like '%'
								AND ii.bank_sign = 'RB'
							ORDER BY ii.item_ord_no; 
---   ima ukupno 4 stavke 



select * from event_tariff;

--------------------------------------------   3.9.2013
select * from income_head where inc_hea_no = 'P080000000044'     --- inc_typ_id  = 56322019

select * from income_item  where inc_hea_id = '455637019'  -- pay_type promijeniti u paket vrste 20   --- inc_ite_id = '455640019'


update income_head set inc_typ_id = '1008282421'  where inc_hea_id  = '455637019';

update income_item set pay_type = '20' where inc_hea_id = '455827019';   -- inc_ite_id = '455640019'  -- inc_hea_id = '455637019'

commit;

--------------------------------------   mijenjam ž-æutura trgovina 01 plaæe u 20 ZN

select * from income_head where inc_hea_no = 'P080000000052';

update income_head set inc_typ_id = '1008282421'  where inc_hea_id  = '455827019';

commit;

select * from income_item where inc_hea_id  = 455827019 and inc_ite_id = 455830019;

select * from income_head;




