SET CURRENT_SCHEMA = SIDEV

--- da konzultiraju se paramteri 

-----    parametar 1 
SELECT fil_tra_id, code, name, cmnt, source_module, target_module                  
                  FROM file_transfer;

-----  parametar 2 
 				SELECT 
                   c.name              AS param_name,
                      b.parameter_value   AS param_value
                  FROM param_file_tran b
                      INNER JOIN param_file_tran_dict c ON b.par_fil_tra_dic_id = c.par_fil_tra_dic_id
                  WHERE b.fil_tra_id IS NULL
                   AND CURRENT DATE BETWEEN b.date_from AND b.date_until with ur;
                 
 -------

 			SELECT  pa.parameter_value
               FROM
                      param_application pa
                      INNER JOIN params_dictionary pd ON pa.par_dic_id = pd.par_dic_id
                  WHERE
                      pd.name = 'interface_flag';
---------------------

SELECT  *
               FROM
                      param_application pa
                      INNER JOIN params_dictionary pd ON pa.par_dic_id = pd.par_dic_id
                  WHERE
                      pd.name = 'interface_flag';

------------------------------------------------
select * from income_head;

select * from income_head where inc_fil_id  = 1010011421;

select * from income_head where inc_fil_id IS NOT NULL;


select * from income_file;

select * from income_file where file_name like 'UN20130911 - MirnaVBDI_Pravi.txt';


select input_time_MQ from income_file;   -- to je timestamp

---- P130000000391  na njemu testirati

select recording_time from income_file where file_name like 'UN20130911 - MirnaVBDI_Pravi.txt';  -- selektiram timestamp


select (TIMESTAMP_FORMAT(recording_time, 'DD/MM/RRRR HH24:MI')) from income_file where file_name like 'UN20130911 - MirnaVBDI_Pravi.txt';  -- selektiram timestamp


select
varchar_format (recording_time, 'YYYY-MM-DD HH24:MI:SS')
from
income_file;

select
varchar_format (recording_time, 'YYYY-MM-DD HH24:MI:SS')
from
income_file
where file_name like 'UN20130911 - MirnaVBDI_Pravi.txt';

select
varchar_format (recording_time, 'HH24:MI:SS'), input_time_MQ, recording_time
from
income_file
where file_name like 'UN20130911 - MirnaVBDI_Pravi.txt';    ---- to je to 



SELECT
                    if.file_name,
                    if.file_source,
                    if.file_date,
                    if.order_type,
                    if.doc_source,
                    if.exec_type,
                    if.ib_reference,
                    if.trans_code_mq,
                    if.user_id_mq,
                    if.input_time_mq,
                    if.channel_mq,
                    if.cust_acc_mq,
                    if.exec_type_mq,
                    if.exec_date_mq,
                    if.ccust_acc_mq,
                    if.ccust_name_mq,
                    if.ccust_city_mq,
                    if.amount_mq,
                    if.pay_purpose_mq,
                    if.credit_ref_typ_mq,
                    if.credit_ref_no_mq,
                    if.debit_ref_typ_mq,
                    if.debit_ref_no_mq,
                    if.eve_id,
                    if.error_code,
                    if.error_desc,
                    if.payor_tax_num,
                    if.payor_code,
                    if.payor_register_no,
                    if.midpayor_tax_num
               
                FROM
                    INCOME_FILE if
                WHERE 
                    if.inc_fil_id = 1011864421;



				SELECT
                    if.file_name,
                    if.file_source,
                    if.file_date,
                    if.order_type,
                    if.doc_source,
                    if.exec_type,
                    if.ib_reference,
                    if.trans_code_mq,
                    if.user_id_mq,
                    varchar_format(if.input_time_mq,'HH24:MI:SS') as input_time_mq,
                    if.channel_mq,
                    if.cust_acc_mq,
                    if.exec_type_mq,
                    if.exec_date_mq,
                    if.ccust_acc_mq,
                    if.ccust_name_mq,
                    if.ccust_city_mq,
                    if.amount_mq,
                    if.pay_purpose_mq,
                    if.credit_ref_typ_mq,
                    if.credit_ref_no_mq,
                    if.debit_ref_typ_mq,
                    if.debit_ref_no_mq,
                    if.eve_id,
                    if.error_code,
                    if.error_desc,
                    if.payor_tax_num,
                    if.payor_code,
                    if.payor_register_no,
                    if.midpayor_tax_num             
                FROM
                    INCOME_FILE if
                WHERE 
                    if.inc_fil_id = 1011864421; ---- ok  

  
		select * from CUSACC_REL_PERSON


		SELECT
                  crp.CUS_REL_PER_ID,
                  crp.REL_PER_TYP_ID,
                  c.REGISTER_NO
              FROM
                  CUSACC_REL_PERSON crp,
                  CITIZEN c
              WHERE
                  crp.REL_PER_CUS_ID = c.CUS_ID
                  AND crp.CUS_ACC_ID = :cus_acc_id
                  AND crp.DATE_FROM <= :process_date
                  AND crp.DATE_UNTIL >= :process_date


     select * from CUSTOMER_ACCOUNT;   

     
     select * from CUSTOMER_ACCOUNT where contract_no = '3205002675';      -- cus_id        

     SELECT
                      BALANCE,
                      BALANCE_DATE
                  
                  FROM
                      CUSACC_BALANCE_LAST
                  WHERE                     
                       CUS_ID = 33972514
                      --CUR_ID = :cur_id
                      --AND CUS_ACC_ID = :cus_acc_id
                     -- AND PRO_TYP_ID = :pro_typ_id   
              

		SELECT
                  crp.CUS_REL_PER_ID,
                  crp.REL_PER_TYP_ID,
                  c.REGISTER_NO
              FROM
                  CUSACC_REL_PERSON crp,
                  CITIZEN c
              WHERE
                  crp.REL_PER_CUS_ID = 33972514
                 -- AND crp.CUS_ACC_ID = 38660052
                  --AND crp.DATE_FROM <= '01.01.2013'
                  --AND crp.DATE_UNTIL >= '03.10.2013'

 select * from CUSACC_REL_PERSON;   -------  Tablica predstavlja evidenciju vezanih osoba uz ugovor

 select * from rel_person_type;  ---- vrste stranih osoba (03 opunomoæenik i slièno) -- rel_per_typ_id 

select * from customer_account where cus_acc_no = '3205002675';  -- raèun koji ima vrstu strane osobe 03 - -opunomoæenik -- cus_acc_id = 38660052

 select * from CUSACC_REL_PERSON where cus_acc_id = '38660052'; --- dohvat dvije osobe opunomoæenika na jedan raèun rel_per_typ_id  = 1999

 

 


 
                      