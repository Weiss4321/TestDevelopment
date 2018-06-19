
SET CURRENT_SCHEMA = 'SITP'

select * from counter_list where cus_acc_no = 3200125745 and process_date between '01.08.2013' and '01.10.2013'

---- bFR2 stanja tekuæih raèuna prijava 5817

select * from batch_log where bat_def_id = 6253448704 order by exec_start_time desc ;

select * from bank_product_type;

select * from customer_account;

select * from customer_account where cus_acc_no like '800010860%';  -- ok

select acc_cur_id from customer_account where cus_acc_no = '8000108607';  -- ok  -- to su euri i oni bi trebali biti u datoteci - Zašto nisu????

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

select * from amount_type;  --- opisi vrsta iznosa ( naknadne naplate, provizije, potraživanja i sl)




