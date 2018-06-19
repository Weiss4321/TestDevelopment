SET CURRENT_SCHEMA = SIDEV


			SELECT
                    ban_tar_user_code,
                    ban_tar_cod_dsc
                FROM
                    BANK_TARIFF_CODE;
-------------- dohvat je OK


			SELECT
                    ban_tar_user_code,
                    ban_tar_cod_dsc
                FROM
                    BANK_TARIFF_CODE              
                WHERE
                    ban_tar_code = '100163';

                    --------------------   primjer dohvata šifre i opisa

     		SELECT 
                    ban_tar_code              
                FROM 
                    turnover 


                    
			SELECT
                    ban_tar_user_code,
                    ban_tar_cod_dsc
                FROM
                    BANK_TARIFF_CODE              
                WHERE
                    ban_tar_code = null;


        
			SELECT
                    ban_tar_user_code,
                    ban_tar_cod_dsc
                FROM
                    BANK_TARIFF_CODE;        

                    


              select ban_tar_code from turnover;

		 select * from turnover where 	pro_typ_id =  41999 and ban_pro_typ_id = 2245837003;

		  select ban_tar_code from turnover where 	pro_typ_id =  41999 and ban_pro_typ_id = 2245837003;   --- ban_tar_code je svugdje null


		select * from BANK_RELATION_TYPE where bank_sign = 'RB';  -- veza izmeðu bank_relation_type i modula za koji puštam obradu

		select * from BANK_RELATION_TYPE where bank_sign = 'RB';  -- veza izmeðu bank_relation_type i modula za koji puštam obradu


		select * from turnover wher

----------------------------------------
			SELECT
                    ban_tar_user_code,
                    ban_tar_cod_dsc             
                FROM
                    BANK_TARIFF_CODE
                     WHERE
                    ban_tar_code= ''


                    
                WHERE
                    ban_tar_code = "" with ur;


SELECT
DUE_TER_TAB_ID,
INV_HEA_ID,
CUS_LET_ID,
TUR_ID,
CUS_ID,
CUS_ORG_UNI_ID,
CUS_CODE,
REGISTER_NO,
DOMICIL,
TURNOVER_REFERENCE,
ACCOUNT,
AMO_TYPE,
BAN_PRO_TYP_ID,
PRO_TYP_ID,
CUS_ACC_ID,
CUS_ACC_NO,
ACC_NUM_ID,
TUR_TYP_ID,
TURNOVER_DESC,
PROCESS_DATE,
VALUE_DATE,
MATURITY_DATE,
DUE_TER_TAB_CUR_ID,
DUE_DEBIT_AMO,
DUE_CREDIT_AMO,
REST_DUE_DEB_AMO,
REST_DUE_CRED_AMO,
LAST_MODIFIC_TIME,
RECORDING_TIME,
DUE_TERM_TABLE.BANK_SIGN,
DUE_TERM_TABLE.USER_LOCK,
DUE_CLO_CNT,
INVOICE_NO,
LAST_PAY_DATE,
PAY_CUS_ID,
VAT_INVOICE_NO
FROM SIDEV.DUE_TERM_TABLE, BANK_RELATION_TYPE
where DUE_TERM_TABLE.BAN_REL_TYPE between '250'
and '251'
AND BANK_RELATION_TYPE.MODULE_NAME = 'RET'
AND VALUE_DATE >= '01.09.2013'
AND VALUE_DATE <= '01.10.2013'
AND (REST_DUE_DEB_AMO <> 0 OR REST_DUE_CRED_AMO <> 0)
AND DUE_TERM_TABLE.BANK_SIGN = 'RB'
ORDER BY REGISTER_NO ASC, MATURITY_DATE DESC, PRO_TYP_ID ASC 

---- za testitranje prijave optimizacije bf55 
---- konto je account  , rest_due_deb_amo je neplaæeni iznos
----- podatak last_pay_date   
--- trebam dohvatiti koji je cus_let_id i ostali podaci
--- due_ter_tab_id = 6,487,739,797
--- last_pay_date je null 

--- napraviti æu update podataka za last_pay_date

select * from due_term_table where due_ter_tab_id = 6487739797  -- samo ovaj zapis æu ažurirati sa vrijednosti

-- UPDATE CUSACC_EXTERNAL set IBAN_ACC_NO = 'HR4524840081100001234' WHERE CUS_ACC_EXT_ID = '170865022';    ---  update IBAN_ACC_NO 
--commit;

 UPDATE due_term_table set last_pay_date = '23.10.2013' where due_ter_tab_id = 6487739797;

 commit;

 ---- ok ažurirao sam taj podatak - testirati da li æe se zapisati u datoteku
UPDATE due_term_table set invoice_no = '123456789' where due_ter_tab_id = 6487739797;

UPDATE due_term_table set vat_invoice_no = '987654321' where due_ter_tab_id = 6487739797; 

 commit;
 
select * from due_term_table;

select * from due_term_table where ban_rel_type between '250' and '251' and account = '1250172' and rest_due_deb_amo = '6010';





               