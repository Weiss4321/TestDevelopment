SET CURRENT_SCHEMA = 'SIDEV'


select * from counter_list;   --- counter lista 
--- raèuni  3208260147 i 3216047015

--Datoteka stanja tekuæih raèuna iz obrade BFR3 nema podatke o danu kašnjenja. 
-- 3208260147 ima nedopušteno prekoraèenje od 16.07.2010 i broje mu se dani 
-- no stanje raèuna u datotekama stanja je 0.00. 
--Isti sluèaj je i sa stanje TR 3216047015. 

--Oba raèuna su blokirana. Molim hitno provjeru i ispravak. 

select * from counter_list where cus_acc_no = '3208260147'; -- daje rezultate  -- 2260 zapisa u bazi 

select * from counter_list where cus_acc_no = '3216047015'; -- daje rezultate

select * from counter_list where cus_acc_no = '3216047015' and pro_met_code = 'HNB';


select * from currency where code_char ='HRK';  -- curid = 63999

select * FROM procedure_type;

select * from CUSACC_BALANCE_LAST; 

select * from account where account IN (5040597, 647000)    ; --- 5040597 ispravak vrijednosti i 647000 nominalni iznos gubitka 

select * FROM procedure_type where PRO_TYP_ID = 826790005; --- glavnica kunskog Glavnica kunskog transakcijskog raèuna fizièke osobe  -- po njemu pretražujemo

select * FROM procedure_type where PRO_TYP_ID = 1494786157; --- Ispravak vrijednosti-nedozvoljena prekoraèenja-kn  -- po njemu pretražujemo i nije ušao zbog Pro_typ_ID-a

select * FROM procedure_type where PRO_TYP_ID = 1494826197; --- Trošak rezerviranja-glavnica  -- po njemu pretražujemo i nije ušao zbog Pro_typ_ID-a

select * from CUSACC_BALANCE_LAST where PRO_TYP_ID = 826790005;


select * from CUSACC_BALANCE_LAST where  CUS_ACC_ID = 160238003;  --- rezultat imamo --- 

select * from customer_account where cus_acc_no = '3216047015';

select * from customer_account where cus_acc_no = '3208260147';



select * from customer where cus_id = '6833996551';

select * from CUSACC_BALANCE_LAST where  CUS_ACC_ID = 160238003 AND CUR_ID = 63999  AND CUS_ID = 6833996551;  --- AND PRO_TYP_ID = 826790005;

select balance, balance_date from CUSACC_BALANCE_LAST where  CUS_ACC_ID = 160238003 AND CUR_ID = 63999  AND CUS_ID = 6833996551;  --- AND PRO_TYP_ID = 826790005;

select balance, balance_date from CUSACC_BALANCE_LAST where  CUS_ACC_ID = 160238003 AND CUR_ID = 63999  AND CUS_ID = 6833996551 AND PRO_TYP_ID = 826790005;



 			SELECT
                    Balance
                FROM
                    CUSACC_BALANCE_LAST
                WHERE
                    CUR_ID = 63999
                    AND CUS_ID = 6833996551
                   AND CUS_ACC_ID = 160238003
                   AND PRO_TYP_ID = 826790005 with ur;   


select * FROM procedure_type where PRO_TYP_ID = 826790005 ; ------- Glavnica kunskog transakcijskog raèuna fizièke osobe

-----------  bank_product_type


select * from bank_product_type;


select * from bank_product_type where BAN_PRO_TYP_ID = 826790005;

-------------------------------------------------    cusacc_balance_last je vjerojatno nastao nakon zadnjeg promenta u tablici cuascc_balance
-- cus_id 6,833,996,551 ZRNIÆ MILENA

select * from cusacc_balance where 
				CUR_ID = 63999
                    AND CUS_ID = 6833996551
                   AND CUS_ACC_ID = 160238003
                   AND PRO_TYP_ID = 826790005;   -- zadnji datum balance date je 1.1.2013 sa -92  (22.9.2010 je bila zadnja promjena po raèunu)

 --------------------------------------

--- cus_id 1,718,521,551   - MANDARIÆ MELITA

select * from cusacc_balance where 
				CUR_ID = 63999
                    AND CUS_ID = 1718521551
                  	AND CUS_ACC_ID = 82495003
                  	AND PRO_TYP_ID = 826790005; -- zadnji datum balance date je 1.1.2013 sa -94  (1.1.2011 je bila zadnja promjena po raèunu). 

 
                   
SELECT
                REL_PER_TYP_ID,
                CODE
           	 FROM
                REL_PERSON_TYPE
-------------  to su vrste vezanih osoba




                

		SELECT
               *
           	 FROM
                REL_PERSON_TYPE

    select * from customer_account;    

 select * from customer;

  select * from customer where code = '2002058';
    
    select * from customer_account where cus_acc_no = '3200001542';    -- cus_id = '6999853' 

  select * from cusacc_rel_person;


   select * from cusacc_rel_person where cus_id = '466543251' ;  -- dohvat za dva raèuna


    select * from customer_account where cus_acc_no = '3205002013';   

select * from citizen;   

  			SELECT
                crp.CUS_REL_PER_ID,
                crp.REL_PER_TYP_ID,
                c.REGISTER_NO
            FROM
                CUSACC_REL_PERSON crp,
                CITIZEN c
            WHERE
                crp.REL_PER_CUS_ID = 6999853
                AND crp.CUS_ACC_ID = 9619611
              

  select * from customer_account where cus_acc_no like '012-30-000%';   

  select * from batch_log;        

 select * from batch_def;   --- bat_def_id = 1569874003

 select * from batch_def where bat_def_id = 1569874003;  --- bf55 generianje CSV datoteke iz rokovnika   


  select * from batch_log where bat_def_id = 6259167704 ;  -- bfR3

 

     select * from batch_log where bat_def_id = 1569874003;      

    -- obrade je rasporeðena -- ovo su parametri - RB#RET;Cp1250;2;X;X;X;X;X;X;X;X;X;X;X;X
                  

   select * from income_head;

   
  select * from income_head where inc_hea_no = 'P130000000474';  --- postoji 01397214 payor_code ali se ne puni


  --SELECT inc_typ_id , inc_typ_code, inc_typ_desc, payor_register_no FROM income_type WHERE inc_typ_code = ? AND inc_typ_id NOT IN (153467019) ORDER BY inc_typ_code



select * from system_code_value where sys_cod_id = 'exec_type'
  

