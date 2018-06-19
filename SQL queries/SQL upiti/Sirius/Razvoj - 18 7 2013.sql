

SET CURRENT_SCHEMA = 'SIDEV';

select * from income_head;

Select * from SYSTEM_CODE_VALUE;

Select sys_code_desc from SYSTEM_CODE_VALUE where sys_code_value = '0' ; -- sys_code_desc like 'bpo%' 

SELECT * from system_code_value where sys_code_value ='1' AND  SYS_COD_ID= 'bpo_priority_flag';       -- to je TO - za Oznaku hitnosti

SELECT * from system_code_value where SYS_COD_ID= 'bpo_priority_flag';  

SELECT * from system_code_value where SYS_COD_ID= 'income_QBE_src_cod';   -- dodane vrijednosti u unesilici za polja Izvor šifra

select SYS_COD_VAL_ID, SYS_CODE_VALUE, SYS_CODE_DESC from SYSTEM_CODE_VALUE;  -- ovo je upit iz SysCodeValueLookUp.java koji dohvaæa podatke za lookup

select * from currency  where code_char like 'DEM';

select * from country;

select * from currency;

select * from transaction_fee;
select * from transaction_type;
select * from fee_code;


select * from customer_account;

select * from rbhr_gpc_prod_sch;

select * from RBHR_GPC_PROD_SCH where rbh_gpc_pro_sch_id = 2888337823;  --- ovakav zapis postoji na SIDEVU --OK   -- GPC_CODE  - FD0100    i RBHR_CODE   - 004-01030305


select * from education_1;

/*++++++*/







