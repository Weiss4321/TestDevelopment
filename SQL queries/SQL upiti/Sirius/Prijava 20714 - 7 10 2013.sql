
SET CURRENT_SCHEMA = SIDEV

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
DUE_TERM_TABLE.BAN_REL_TYPE,
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
FROM DUE_TERM_TABLE, BANK_RELATION_TYPE
where 
  DUE_TERM_TABLE.BAN_REL_TYPE  between '250' and '251'  
 --and BANK_RELATION_TYPE.MODULE_NAME = 'bos'
 and VALUE_DATE >= '01.09.2013'
AND VALUE_DATE <= '01.10.2013'
AND (REST_DUE_DEB_AMO <> 0 OR REST_DUE_CRED_AMO <> 0)
AND DUE_TERM_TABLE.BANK_SIGN = 'RB'
ORDER BY REGISTER_NO ASC, MATURITY_DATE DESC, PRO_TYP_ID ASC 

-- dohvat radi ako zakomentiram gore navedene dijelove iz where uvjeta  -- dohva�am previ�e podataka
---- --- BANK_RELATION_TYPE.BAN_REL_TYPE --   npr  
----  DUE_TERM_TABLE.BAN_REL_TYPE  between '250' and '251'  
----  DUE_TERM_TABLE.BAN_REL_TYPE  = '251'  

select * from bank_relation_type where bank_sign = 'RB' and aplic_code = 'SIR'

select * from bank_relation_type where bank_sign = 'RB' 

select * from bank_relation_type where ban_rel_type = '250'  -- okviri toga ima puno

select * from bank_relation_type where ban_rel_type = '251' 

select * from due_term_table;




select * from batch_log where bat_def_id = 1569874003  with ur;  -- i sortirati prema recording time


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
DUE_TERM_TABLE.BAN_REL_TYPE,
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
AND VALUE_DATE >= '15.09.2013'
AND VALUE_DATE <= '01.10.2013'
AND (REST_DUE_DEB_AMO <> 0 OR REST_DUE_CRED_AMO <> 0)
AND DUE_TERM_TABLE.BANK_SIGN = 'RB'
ORDER BY REGISTER_NO ASC, MATURITY_DATE DESC, PRO_TYP_ID ASC  




select * from bank_tariff_code;



