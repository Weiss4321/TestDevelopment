SET CURRENT_SCHEMA = 'SIDEV'



select *from batch_log where bat_def_id = 6253448704;

update batch_log set exec_status = '1' where bat_log_id = 6500725704;

commit;


select * from craftsman;

--  uzeti æu jedan raèun za testiranje Mikec Ana -- cus_id = 30008998

-- dohvat za legal entity
select * from legal_entity;

select * from legal_entity where cus_id = '30008998';    -- tu imamo cus_id  a

select * from customer where basel_cus_type = '20' and cus_id = '30008998'; -- za njega mogu testirati

select * from customer;

select * from customer_account;

select * from cust_org_unit;

select cus_org_uni_id from customer_account;

where cus_org_uni_id  not null;

--- sQL koji dohvaæa nešto na SIDEV-u

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
                c.cus_id,
                c.code cus_code,
                c.register_no,
                c.name
            FROM
                customer_account ca,
                bank_product_type bpt,
                customer c
                LEFT OUTER JOIN LEGAL_ENTITY le ON c.cus_id = le.cus_id
                LEFT OUTER JOIN CRAFTSMAN cr ON c.cus_id = cr.cus_id
            WHERE                
                --AND ca.cus_id = c.cus_id
                ca.cus_acc_status NOT IN ('I', 'X', 'C', 'E')
                --AND ca.org_uni_id = :(orgUniId)
                AND ca.opening_date <= '14.06.2013'
                AND ( ca.closing_date IS NULL OR ca.closing_date >= '14.10.2013' )
                AND ca.bank_sign = 'RB'
                --AND ca.ban_pro_typ_id IN (24999, 37722005, 795985005, 795986005, 23999, 2245837003, 2245840003, 4662964704, 4662965704, 4662968704, 4662969704, 4662970704, 4662971704, 4662972704, 4662979704, 4663049704, 4663050704, 4663051704, 4663053704, 4663054704, 4663057704, 4663058704, 4663060704, 4663061704, 4663063704, 4663064704, 4663065704, 4663066704, 4663068704, 4663069704, 4663071704)
                AND  ca.ban_pro_typ_id IN (37722005,37723005,2245837003,2245840003,2550768003)
                AND (c.add_data_table='LEGAL_ENTITY' AND le.pay_list_flag='1' OR c.add_data_table='CRAFTSMAN' AND cr.pay_list_flag='1')
                AND c.add_data_table IN ('LEGAL_ENTITY', 'CRAFTSMAN')
                AND c.basel_cus_type = '20';


-------------------------------------

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
                c.cus_id,
                c.code cus_code,
                c.register_no,
                c.name
            FROM
                customer_account ca,
                bank_product_type bpt,
                customer c
                LEFT OUTER JOIN LEGAL_ENTITY le ON c.cus_id = le.cus_id
                LEFT OUTER JOIN CRAFTSMAN cr ON c.cus_id = cr.cus_id
            WHERE                
                 ca.cus_id = c.cus_id
               and ca.cus_acc_status NOT IN ('I', 'X', 'C', 'E')
                AND ca.org_uni_id  IN (0,1253,2345,2346,3253,4253,5253,6253,8253,9253,9553,10253,11253,12253,13253,14253,15253,16253,17253,
			18253,19253,20253,21253,22253,23253,24253,25253,26253,27253,28253,30253,31253,32253,33253,34253,35253,
			36253,37253,38253,39253,40253,41253,42253,43253,44253,45253,46253,47253,48253,49253,50253,51253,52253,
			53253,1032003,1033003,1034003,1035003,1036003,1059003,1061003,1062003,1063003,1064003,1065003,1066003,
			1067003,1136003,1171003,1172003,1177003,1321003,1322003,1424003,1540003,1548003,1550003,1558003,1622003,
			1919003,1995003,19901003,19961003,20212203,20213203,20214203,20215203,20216203,20218203,20220203,20222203,
			20223203,20224203,20225203,20226203,20227203,20228203,20229203,20230203,20231203,20232203,20233203,20234203,
			20235203,20236203,20237203,20238203,20239203,20240203,20241203,20243203,20244203,20245203,20246203,20247203,
			20248203,20249203,20250203,20251203,20252203,20253203,20254203,20255203,20256203,20257203,20258203,20259203,
			20260203,20261203,20262203,20263203,20264203,20265203,20266203,20267203,20268203,20269203,20270203,20271203,
			20272203,20273203,20274203,28137203,37873503,49996503,49997503,49998503,148547503,192672503,321418303,
			348326303,373580303,1050800999,1059900999,1617336003,1617337003,1669107003,1707948003,1707949003,1707950003,
			1707951003,1707952003,1707953003,1707954003,1921853003,1999900999,2136495601,2139474601,2139497601,2779607003,
			3001691554,3029417824,3236250003,3236261003,3236271003,3236281003,3886438274,5635826003,5635827003,5635828003,
			5635829003,5635830003,5635831003,5635832003,5635833003,5635834003,5635835003,5635836003,5635837003,5635838003,
			5635839003,5635840003,5635841003,5635842003,5635843003,5635844003,27991265303,920645187003,1286624286003)
                AND ca.opening_date <= '28.06.2013'
                AND ( ca.closing_date IS NULL OR ca.closing_date >= '28.10.2013' )
                AND ca.bank_sign = 'RB'
                --AND ca.ban_pro_typ_id IN (24999, 37722005, 795985005, 795986005, 23999, 2245837003, 2245840003, 4662964704, 4662965704, 4662968704, 4662969704, 4662970704, 4662971704, 4662972704, 4662979704, 4663049704, 4663050704, 4663051704, 4663053704, 4663054704, 4663057704, 4663058704, 4663060704, 4663061704, 4663063704, 4663064704, 4663065704, 4663066704, 4663068704, 4663069704, 4663071704)
                AND  ca.ban_pro_typ_id IN (37722005,37723005,2245837003,2245840003,2550768003)
                AND (c.add_data_table='LEGAL_ENTITY'  OR c.add_data_table='CRAFTSMAN' )
                AND c.add_data_table IN ('LEGAL_ENTITY', 'CRAFTSMAN')
                AND c.basel_cus_type = '20';

------------   (2245837003,37722005,37723005,2245840003,2550768003)


-- dohvat organizacijske jedinice - getOrgUniIter  -- dohvaæam 200 org jedinica

		SELECT
                org_uni_id,
                code
            FROM
                organization_unit
-----
select * from bank_product_type where ban_rel_type IN ('005','250');

--- 005 to su akreditivi
--- 250 to su okviri

-----

select * from customer_account; 

select * from customer where register_no = '185304' ;  -- dohvat jednog od raèuna iz datoteke 20131025


select * from customer_account where cus_acc_no = '6000002233';

select * from customer_account where cus_id = 75402352;


			SELECT
                    term_date_until
                FROM
                    cusacc_term
                WHERE 
                    cus_acc_id = 395761921322
                    AND '25.10.2013' BETWEEN date_from AND date_until
                    AND p_contract_id = 'exp_mat_date'

--------------------

select * from bank_product_type;

select * from bank_product_type where ban_rel_type IN('005','250');   -- vrste proizvoda za dohvat
-- ban_pro_typ_id =37,722,005    -- za ove parametre 005, 806, nostro akreditiv  - 

--- nostro akreditiv -- 005 ban_rel_type 

select * from customer_account where ban_pro_typ_id = 37722005;

--- ispitivanje tipova raèuna koji su dohvaæeni u datoteci ST_PR_SIR_M 20131027.txt

--- 1 raèun -- BAN_PRO_TYP_ID = 2245837003
select * from customer_account where cus_acc_no  = '6000000483';

select * from bank_product_type where BAN_PRO_TYP_ID = 2245837003;  -- 250, 860 Kunski okvir


---- ok dohvaæam podatke 
		SELECT
                 cusacc_amo_id,
                 cus_acc_id,
                 amount,
                 cur_id,
                 amo_offset_low,
                 amo_offset_high,
                 date_from,
                 date_until
             FROM
                 cusacc_amount
             WHERE
                     cus_acc_id = 87517022
                 AND '27.10.2013' BETWEEN date_from AND date_until
             ORDER BY
                 date_until DESC


                 SELECT
                 cusacc_amo_id,
                 cus_acc_id,
                 amount,
                 cur_id,
                 amo_offset_low,
                 amo_offset_high,
                 date_from,
                 date_until
             FROM
                 cusacc_amount
             WHERE
                     cus_acc_id = 87517022


select * from customer where cus_id = 68850352 ;  -- ovaj podatak se dohvaæa

select * from customer_account  where cus_acc_id = 882674653 ;   --- cus_id mu je = 68850352


SELECT current_date FROM sysibm.sysdummy1 ;  -- dohvat trenutnog datuma


select * from customer_account where cus_acc_id = 263449022;  -- ima cus_id??????

select * from bank_product_type;

---------------------- test datoteke
select * from customer_account;

select * from customer_account where cus_acc_no = '8000253544'; 

--- test je ok za 27-04-2013 za cus_acc_id = 86500022
-- tesz ok nema datuma za raèun cus_acc_id = 286028022
-- test za raèun cus_acc_no = '8000253690'  -- cus_acc_id = 1116232263

			SELECT
                    term_date_until
                FROM
                    cusacc_term
                WHERE 
                    cus_acc_id = 841061653
                    AND '29.10.2013' BETWEEN date_from AND date_until
                    AND p_contract_id = 'exp_mat_date'



-----

------------------------------  bfR3

select *from batch_log where bat_def_id = 6259167704 order by recording_time desc;

------------------------------  bfR4

select *from batch_log where bat_def_id = 6266762704 order by recording_time desc;


                