SET CURRENT_SCHEMA = SIDEV;







SELECT * FROM APP_USER;



SELECT * FROM INCOME_TYPE;  -- tu se primaju xml datoteke

SELECT * FROM COUNTRY;

SELECT COU_ISO_CODE
FROM COUNTRY 
INNER JOIN INCOME_ITEM
ON COUNTRY.COU_ISO_CODE = INCOME_ITEM.BENEF_COUNTRY_CODE;

SELECT  NAME FROM COUNTRY;

SELECT * FROM CURRENCY; 

SELECT * FROM CITIZEN;   -- podaci o osobama

SELECT a.benef_cus_name into :() FROM income_item a WHERE a.inc_ite_id = 782136;

SELECT * FROM INCOME_ITEM;


SELECT CUR_ID FROM INCOME_ITEM;

--- ana Mikec koju testiram u aplikaciji
SELECT a.benef_cus_name,a.benef_cus_address,a.iban,a.benef_cus_city,a.benef_country_code,a.credit_ref_typ,a.credit_ref_no,a.benef_swift_address,a.benef_bank_name,a.benef_bank_address,
a.BENEF_BANK_CITY,a.BENEF_BANK_COU_COD,a.BENEF_TYPE,a.amount,a.DEBIT_REF_TYP,a.PURPOSE_CODE,a.PAY_TYPE_DESC,a.INC_SUB_TYPE_CODE,a.DEBIT_REF_NO,a.CHARGING_OPTION,a.CUR_ID,
a.PAYMENT_DESC,a.PRIORITY_FLAG
FROM income_item a
WHERE a.inc_ite_id = 455830019;   --- to je id od Tajek Sanje

SELECT  * FROM COUNTRY;

---- pravim update income_item tablice polje adresa na ekranu
-- IBAN neki primjer HRkk bbbb bbbc cccc cccc HR011234123412341234 

UPDATE INCOME_ITEM set IBAN = 'HR011234123412341234' where inc_ite_id = 455830019;
UPDATE INCOME_ITEM set BENEF_CUS_ADDRESS = 'ulica' where inc_ite_id = 455830019;
UPDATE INCOME_ITEM set BENEF_CUS_City = 'Zagreb' where inc_ite_id = 455830019;
UPDATE INCOME_ITEM set BENEF_COUNTRY_CODE = 'HR' where inc_ite_id = 455830019;
UPDATE INCOME_ITEM set CREDIT_REF_NO = '12345' where inc_ite_id = 455830019;
UPDATE INCOME_ITEM set BENEF_SWIFT_ADDRESS = 'RZBHHR2X' where inc_ite_id = 455830019;
UPDATE INCOME_ITEM set BENEF_BANK_NAME = 'Raiffeisenbank Austria d.d.' where inc_ite_id = 455830019;
UPDATE INCOME_ITEM set BENEF_BANK_ADDRESS = 'Petrinjska 59' where inc_ite_id = 455830019;
UPDATE INCOME_ITEM set BENEF_BANK_CITY = 'Zagreb' where inc_ite_id = 455830019;
UPDATE INCOME_ITEM set BENEF_BANK_COU_COD = 'HR' where inc_ite_id = 455830019;
UPDATE INCOME_ITEM set BENEF_TYPE = '1' where inc_ite_id = 455830019;
UPDATE INCOME_ITEM set DEBIT_REF_TYP = '1234' where inc_ite_id = 455830019;
UPDATE INCOME_ITEM set PURPOSE_CODE = 'abcd' where inc_ite_id = 455830019;
UPDATE INCOME_ITEM set CREDIT_REF_TYP = '5555' where inc_ite_id = 455830019;
UPDATE INCOME_ITEM set PAY_TYPE_DESC = 'opis vrste primanja ' where inc_ite_id = 455830019;
UPDATE INCOME_ITEM set INC_SUB_TYPE_CODE = '678' where inc_ite_id = 455830019;
UPDATE INCOME_ITEM set DEBIT_REF_NO = '333' where inc_ite_id = 455830019;
UPDATE INCOME_ITEM set CHARGING_OPTION = '1' where inc_ite_id = 455830019;
UPDATE INCOME_ITEM set PAYMENT_DESC = 'opis placanja' where inc_ite_id = 455830019;
UPDATE INCOME_ITEM set PRIORITY_FLAG = '1' where inc_ite_id = 455830019;



commit;


-- primjer selecta iz dvije tablice gdje tražim country

SELECT it.BENEF_COUNTRY_CODE, c.NAME, c.COU_ISO_CODE
FROM INCOME_ITEM it
    LEFT OUTER JOIN COUNTRY c 
        ON it.BENEF_COUNTRY_CODE = c.COU_ISO_CODE
WHERE INC_ITE_ID in (10790791, 455830019);

SELECT COU_ISO_CODE FROM COUNTRY WHERE COU_ISO_CODE LIKE 'HR%'; -- samo dohvat HR

SELECT COU_ISO_CODE  FROM COUNTRY;

SELECT *FROM COUNTRY;    -- dohvat cijele tablice

SELECT * FROM system_code_value;  -- dohvat cijele tablice






SELECT SYS_CODE_VALUE,SYS_COD_ID,SYS_CODE_DESC FROM SYSTEM_CODE_VALUE WHERE SYS_CODE_DESC LIKE 'Pr%' ;

SELECT SYS_CODE_VALUE,SYS_COD_ID,SYS_CODE_DESC FROM SYSTEM_CODE_VALUE WHERE SYS_COD_ID LIKE 'customer_typ%' ;   -- dohvat fizièke i pravne osobe

SELECT SYS_CODE_VALUE,SYS_COD_ID,SYS_CODE_DESC FROM system_code_value WHERE SYS_CODE_DESC LIKE '%OUR%' ;   -- nema   na teret ili podijeljeni

SELECT SYS_CODE_VALUE,SYS_COD_ID,SYS_CODE_DESC FROM system_code_value WHERE SYS_COD_ID LIKE 'swift%' ;   -- pretraga swih swift kodova

SELECT SYS_CODE_VALUE,SYS_COD_ID,SYS_CODE_DESC FROM system_code_value WHERE SYS_COD_ID LIKE 'swift_code_bp%';   -- pretraga samo swift code bpo koji su mi potrebni za Troškovnu opciju  

SELECT SYS_CODE_VALUE,SYS_CODE_DESC,SYS_COD_ID FROM system_code_value WHERE SYS_COD_ID LIKE 'swift_code_bp%';   -- pretraga samo swift code bpo koji su mi potrebni za Troškovnu opciju  OVO bez sys_cod_id getCostOption

SELECT SYS_CODE_VALUE FROM system_code_value WHERE SYS_COD_ID LIKE 'swift_code_bp%';  -- samo dohvaæam sys-code-value

SELECT SYS_COD_ID FROM SYSTEM_CODE_VALUE WHERE SYS_CODE_DESC LIKE 'VK%';

SELECT  NAME,COU_ISO_CODE FROM COUNTRY WHERE COU_ISO_CODE LIKE 'HR%';    --- to je bio upit za dohvat za hr        



SELECT a.SYS_CODE_VALUE,a.SYS_COD_ID,a.SYS_CODE_DESC,b.charging_option
FROM a.system_code_value,b.income_item
WHERE SYS_COD_ID LIKE 'charging_option%';  



SELECT sys_code_desc from system_code_value where sys_code_value = '1' AND SYS_COD_ID = 'swift_code_bpo';  -- to je TO


SELECT sys_code_desc,sys_cod_id from system_code_value where sys_code_value = '1' AND SYS_COD_ID = 'swift_code_bpo';  -- test

SELECT SYS_CODE_VALUE,SYS_COD_ID,SYS_CODE_DESC FROM SYSTEM_CODE_VALUE WHERE SYS_COD_ID LIKE 'customer_typ%' ;   -- dohvat fizièke i pravne osobe  za upisati novu metodu  - neko dohvaæanje

SELECT SYS_CODE_VALUE,SYS_COD_ID,SYS_CODE_DESC FROM SYSTEM_CODE_VALUE WHERE SYS_COD_ID LIKE 'customer_typ%' ;   -- dohvat fizièke i pravne osobe  za upisati novu metodu  - dohvat ispravnih podataka


SELECT benef_type from income_item where inc_ite_id = 455830019;   --- vrsta strane osobe

SELECT SYS_CODE_VALUE,SYS_COD_ID,SYS_CODE_DESC FROM SYSTEM_CODE_VALUE WHERE SYS_COD_ID LIKE 'customer_typ%';

SELECT SYS_CODE_DESC FROM SYSTEM_CODE_VALUE WHERE sys_code_value ='1' AND SYS_COD_ID LIKE 'customer_type     ';  --odabir pravne osobe TO Je TO

SELECT sys_code_desc from system_code_value where sys_code_value = '1' AND SYS_COD_ID = 'swift_code_bpo';  -- to je TO



SELECT priority_flag from income_item;   -- ok 

SELECT * from system_code_value;

SELECT sys_code_desc,sys_cod_id from system_code_value where sys_code_desc LIKE 'redovn%';  -- nema

SELECT sys_code_desc from system_code_value where sys_code_desc LIKE 'hit%';   -- nema

SELECT sys_cod_id from system_code_value where sys_cod_id like 'acc_priority_fla%';

SELECT sys_cod_id,sys_code_value,sys_code_desc from system_code_value where sys_cod_id like 'bpo_priority_fla%';

SELECT sys_code_desc from system_code_value where sys_code_value ='1' AND  SYS_COD_ID= 'bpo_priority_flag';       -- to je TO - za Oznaku hitnosti



sELECT cus_acc_no,iban_acc_no,bank_sign FROM customer_account with ur; 


--- trebat æu dohvaæati kao hidden INC_SUB_TYP_ID i INC_TYP_ID
---  te kao vidljiva polja INC_SUB_TYP_CODE i INC_SUB_TYP_DESC

SELECT * from income_subtype with ur;        --- 


SELECT * FROM CUSTOMER_ACCOUNT;

SELECT CUS_ACC_NO from CUSTOMER_ACCOUNT;






