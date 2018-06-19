SET CURRENT_SCHEMA = 'SITP';

-- ispitati na nestoru

select * from RBHR_GPC_PROD_SCH where rbh_gpc_pro_sch_id = 2888337823;    --- ban_pro_typ_id mu je 790659005  -- pur_sub_id = 1605370003  -- pro_cat_id = 1605383003
---- rbh_gpc_pro_sch_id = 2888337823 zadani id 

--- veza izmeðu tablica je ban_pro_typ_id
select * from RBHR_GPC_PROD_SCH;


select GPC_code, RBHR_code from RBHR_GPC_PROD_SCH where ban_pro_typ_id = 790659005;    --- jedan  RBHR_CODE odstupa od GPC_CODE = FD0100 i RBHR_CODE = 004-01030305  - to je RBHR_CODE kreiran 19 7 2013

select * from RBHR_GPC_PROD_SCH where ban_pro_typ_id = 790659005;

 

select * from customer_account where ban_pro_typ_id = 790659005 and pur_sub_id = 1605370003 and pro_cat_id = 1605383003;   --- imam 858 raèuna i za njih bi trebali biti RBHR_CODE  - 004-01030305 i  FD0100 FD0100


select * from batch_log where bat_def_id  = 6253448704;  -- bfR2

select * from batch_log where bat_def_id  = 6266762704;  -- bfR3

select * from batch_log where bat_def_id  = 6259167704;  -- bfR4

---- pomoæni upit------------------------------------------------------------------------------------------------------------------------------------------------- vestigo sirius - nestor 92

select * from RBHR_GPC_PROD_SCH where rbh_gpc_pro_sch_id = 2888337823;    --- ban_pro_typ_id mu je 790659005  -- pur_sub_id = 1605370003  -- pro_cat_id = 1605383003
---- rbh_gpc_pro_sch_id = 2888337823 zadani id 

--- veza izmeðu tablica je ban_pro_typ_id
select * from RBHR_GPC_PROD_SCH;


select GPC_code, RBHR_code from RBHR_GPC_PROD_SCH where ban_pro_typ_id = 790659005;    --- jedan  RBHR_CODE odstupa od GPC_CODE  - to je RBHR_CODE kreiran 19 7 2013

select * from RBHR_GPC_PROD_SCH where ban_pro_typ_id = 790659005;

 

select * from customer_account where ban_pro_typ_id = 790659005 and pur_sub_id = 1605370003 and pro_cat_id = 1605383003;



----- dodati u uvjet ova dva uvjeta za RBHR_CODE  - 004-01030305 i  FD0100 FD0100 u bfR2   --- puštanje za PAY
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
                c.name,
                ca.pur_sub_id,
                ca.pro_cat_id
            FROM
                customer_account ca,
                bank_product_type bpt,
                customer c
                LEFT OUTER JOIN LEGAL_ENTITY le ON c.cus_id = le.cus_id
                LEFT OUTER JOIN CRAFTSMAN cr ON c.cus_id = cr.cus_id    
                --LEFT OUTER JOIN RBHR_GPC_PROD_SCH rg ON  ca.ban_pro_typ_id =  rg.ban_pro_typ_id  -- dodan redak 
            WHERE
                ca.ban_pro_typ_id = bpt.ban_pro_typ_id
                --AND ca.pur_sub_id = 1605370003           -- 
                --AND ca.pro_cat_id = 1605383003 
                AND ca.cus_id = c.cus_id
                AND ca.cus_acc_status NOT IN ('I', 'X', 'C', 'E')
                AND ca.opening_date <= '2013-07-19'
                AND ( ca.closing_date IS NULL OR ca.closing_date >= '2013-07-19' )
                AND ca.bank_sign = 'RB'
                AND ca.ban_pro_typ_id IN (24999, 37722005, 795985005, 795986005, 23999, 2245837003, 2245840003, 4662964704, 4662965704, 4662968704, 4662969704, 4662970704,
                4662971704, 4662972704, 4662979704, 4663049704, 4663050704, 4663051704, 4663053704, 4663054704, 4663057704, 4663058704, 4663060704, 4663061704, 4663063704,
                4663064704, 4663065704, 4663066704, 4663068704, 4663069704, 4663071704)
                 AND (c.add_data_table='LEGAL_ENTITY' AND le.pay_list_flag='1' OR c.add_data_table='CRAFTSMAN' AND cr.pay_list_flag='1')
                 AND c.add_data_table IN ('LEGAL_ENTITY', 'CRAFTSMAN')
                AND c.basel_cus_type = '20';         ---- do ovdje je originalni upit
		
                  
-------------------------------------------------------  upit    --------------  bfr2    ----  sada trebam pustiti obradu bfR2    --- 
--- obrada je puštena  RC-100


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
                c.name,
                ca.pur_sub_id,
                ca.pro_cat_id
            FROM
                customer_account ca,
                bank_product_type bpt,
                RBHR_GPC_PROD_SCH rgps,
                customer c
                LEFT OUTER JOIN LEGAL_ENTITY le ON c.cus_id = le.cus_id
                LEFT OUTER JOIN CRAFTSMAN cr ON c.cus_id = cr.cus_id    
            WHERE
                ca.ban_pro_typ_id = bpt.ban_pro_typ_id
                AND rgps.BAN_PRO_TYP_ID=ca.BAN_PRO_TYP_ID
                AND ca.cus_id = c.cus_id
                AND ca.cus_acc_status NOT IN ('I', 'X', 'C', 'E')
                AND ca.opening_date <= '2013-07-19'
                AND ( ca.closing_date IS NULL OR ca.closing_date >= '2013-07-19' )
                AND ca.bank_sign = 'RB'
                AND rgps.RBHR_CODE='004-01030305'  --- ovaj uvjet izbaciti van
                AND (c.add_data_table='LEGAL_ENTITY' AND le.pay_list_flag='1' OR c.add_data_table='CRAFTSMAN' AND cr.pay_list_flag='1')
                AND c.add_data_table IN ('LEGAL_ENTITY', 'CRAFTSMAN')
                AND c.basel_cus_type = '20';
             
                
select * from customer_account where ban_pro_typ_id = 790659005 and pur_sub_id = 1605370003 and pro_cat_id = 1605383003;   --- imam 858 raèuna i za njih bi trebali biti RBHR_CODE  - 004-01030305 i  FD0100 FD0100




             