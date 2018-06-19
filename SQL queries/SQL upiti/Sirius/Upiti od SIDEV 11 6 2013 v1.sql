



SET CURRENT_SCHEMA = SIDEV;


SELECT pro_typ_id, pro_typ_code,  name FROM procedure_type WHERE pro_typ_code = '472';

SELECT pro_typ_id, pro_typ_code,  name FROM procedure_type;



-----        za ekran Stavka paketa -- podaci u tablici income_subtype su bezveze         ----      CR FBPr200019006 - 13-01141-001-01 Standardizacija isplate osobnih primanja
-----       Unijela sam sad na SIDEV novu vrstu prihoda 20 ZBROJNI NALOG PLAÆE I OSTALA PRIMANJA
-----       Našla sam nekakvu unesilicu koja visi na Nini u razvoju po imenima i èini se sasvim pristojna.    to je income_type  tablica

SELECT * FROM income_type;

----     income_subtype je neka tablica æiji æe se podaci morati doraditi    -- incomecode je polje na ekranu -> Vrsta Primanja koje trebam popuniti sa 

SELECT * FROM income_subtype;

SELECT inc_sub_typ_code, inc_sub_typ_desc from income_subtype;

SELECT * FROM EVE_TYP_GROUP;  

SELECT * FROM INCOME_ITEM WHERE IBAN = 'HR011234123412341234';

SELECT * FROM INCOME_ITEM WHERE IBAN = 'HR011234123412341234';

SELECT BENEF_CUS_NAME FROM INCOME_ITEM WHERE INC_ITE_ID = 455830019 ;   -- select imena

SELECT BENEF_CUS_NAME FROM INCOME_ITEM WHERE INC_ITE_ID = 455830019 ;



--- testiram dohvat u vjeverici

SELECT 
ii.inc_ite_id,
ii.benef_cus_code,
cu.name
FROM 
income_item ii
 LEFT OUTER JOIN customer cu ON ii.benef_cus_id = cu.cus_id;

SELECT INC_TYP_DESC from INCOME_TYPE;
 
SELECT INC_SUB_TYP_DESC FROM income_subtype;

SELECT * FROM INCOME_ITEM WHERE IBAN = 'HR011234123412341234';



SELECT * FROM INCOME_ITEM;

SELECT * FROM income_subtype;---- ovo je podtip income typea

UPDATE INCOME_SUBTYPE set INC_SUB_TYP_DESC = '5555' where INC_TYP_ID = 120675019;  -- update stupca INC_SUB_TYP_DESC

commit;

 ----------------------------------------------------------------------   ovo je dohvat dijela podataka iz tablica income_item ii   i   currency cr
		SELECT   		ii.inc_hea_id,              
					ii.item_ord_no,
					ii.benef_cus_id,
					ii.benef_cus_code,
					ii.benef_reg_no,
					cu.name,
					ii.benef_cus_acc_no,
					ii.benef_cus_acc_id,
					ii.cur_id,
					cr.code_num,
					cr.code_char,
					ii.amount,
					ii.pay_period,
					ii.cus_org_uni_ID,
					ii.cus_org_uni_code,
					ii.posting_date,
					ii.value_date,	
					ii.item_status
				FROM 
					currency cr
					, income_item ii 
					LEFT OUTER JOIN customer cu
						ON ii.benef_cus_id = cu.cus_id
				WHERE 
					cr.cur_id = ii.cur_id;
					
					
SELECT INC_TYP_DESC from INCOME_TYPE;

SELECT * FROM income_type;

SELECT * FROM income_item;	-----  ako je   inc_sub_type_code null onda ništa 

SELECT * FROM income_subtype;	

------------------------------------------------------------------------------------------------------------------------------

		SELECT 
		INC_TYP_DESC
		FROM
		income_type
		WHERE inc_typ_code = '20';   --- dohvat zbojnog naloga plaæe opis

-----------------------------------------------------------------------------------------------------------------------------
---- INC_SUB_TYPE_CODE je popunjen sa vrijednosti 678  za Tajek Sanju

SELECT a.benef_cus_name,a.INC_SUB_TYPE_CODE,a.benef_cus_address,a.iban,a.benef_cus_city,a.benef_country_code,a.credit_ref_typ,a.credit_ref_no,a.benef_swift_address,a.benef_bank_name,a.benef_bank_address,
a.BENEF_BANK_CITY,a.BENEF_BANK_COU_COD,a.BENEF_TYPE,a.amount,a.DEBIT_REF_TYP,a.PURPOSE_CODE,a.CREDIT_REF_TYP
FROM income_item a
WHERE a.inc_ite_id = 455830019;  	

-- Tajek Sanja idem testirati   - dohvat za njega
SELECT * FROM income_item WHERE inc_ite_id = 455830019; 

-- Tajek Štefa idem testirati   - dohvat za njega
SELECT * FROM income_item WHERE inc_ite_id = 455837019; 


SELECT a.benef_cus_name,a.INC_SUB_TYPE_CODE,a.benef_cus_address,a.iban,a.benef_cus_city,a.benef_country_code,a.credit_ref_typ,a.credit_ref_no,a.benef_swift_address,a.benef_bank_name,a.benef_bank_address,
a.BENEF_BANK_CITY,a.BENEF_BANK_COU_COD,a.BENEF_TYPE,a.amount,a.DEBIT_REF_TYP,a.PURPOSE_CODE,a.CREDIT_REF_TYP
FROM income_item a
WHERE a.inc_ite_id = 455837019; 


---UPDATE INCOME_ITEM set IBAN = 'HR011234123412341234' where inc_ite_id = 455837019;
UPDATE INCOME_ITEM set BENEF_CUS_ADDRESS = 'ulica Tajeka Štefa' where inc_ite_id = 455837019;
UPDATE INCOME_ITEM set BENEF_CUS_City = 'Zagreb' where inc_ite_id = 455837019;
UPDATE INCOME_ITEM set BENEF_COUNTRY_CODE = 'HR' where inc_ite_id = 455837019;
UPDATE INCOME_ITEM set CREDIT_REF_TYP = '63' where inc_ite_id = 455837019;
UPDATE INCOME_ITEM set CREDIT_REF_NO = '56789' where inc_ite_id = 455837019;
UPDATE INCOME_ITEM set BENEF_SWIFT_ADDRESS = 'RZBHHR2X' where inc_ite_id = 455837019;
UPDATE INCOME_ITEM set BENEF_BANK_NAME = 'Raiffeisenbank Austria d.d.' where inc_ite_id = 455837019;
UPDATE INCOME_ITEM set BENEF_BANK_ADDRESS = 'Petrinjska 59' where inc_ite_id = 455837019;
UPDATE INCOME_ITEM set BENEF_BANK_CITY = 'Zagreb' where inc_ite_id = 455837019;
UPDATE INCOME_ITEM set BENEF_BANK_COU_COD = 'HR' where inc_ite_id = 455837019;
UPDATE INCOME_ITEM set BENEF_TYPE = '1' where inc_ite_id = 455837019;
UPDATE INCOME_ITEM set DEBIT_REF_TYP = '5678' where inc_ite_id = 455837019;
UPDATE INCOME_ITEM set PURPOSE_CODE = 'efgh' where inc_ite_id = 455837019;
UPDATE INCOME_ITEM set CREDIT_REF_TYP = '5678' where inc_ite_id = 455837019;
UPDATE INCOME_ITEM set INC_SUB_TYPE_CODE = '124' where inc_ite_id = 455837019; 


UPDATE INCOME_ITEM set IBAN = 'HR011234123412341230' where inc_ite_id = 455830019; 

commit;

select * from income_item where  inc_ite_id = 455837019; 







UPDATE INCOME_ITEM set INC_SUB_TYPE_CODE = '124' where inc_ite_id = 455830019; 

UPDATE INCOME_SUBTYPE set INC_SUB_TYP_CODE = '124' where INC_TYP_ID = 120675019;  -- update stupca INC_SUB_TYP_CODE
UPDATE INCOME_SUBTYPE set INC_SUB_TYP_DESC = 'Testiranje opisa' where INC_TYP_ID = 120675019;  -- update stupca INC_SUB_TYP_DESC

UPDATE INCOME_SUBTYPE set INC_SUB_TYP_DESC = ' Opis primanja 2' where INC_SUB_TYP_CODE = '124';  -- update stupca INC_SUB_TYP_DESC

commit;						

SELECT * FROM income_subtype;	

     SELECT INC_SUB_TYP_DESC
	FROM
	income_subtype
	WHERE inc_typ_id = 1999;

---- potrebni dohvat za polje Vrsta Primanja

 	SELECT a.INC_SUB_TYP_DESC,a.INC_SUB_TYP_CODE
	FROM
	income_subtype a,
	income_item b
	WHERE a.INC_SUB_TYP_CODE = b.INC_SUB_TYPE_CODE;
	
-----------------------------------------------------------------
--- ovdje vraæam opis vrste primanja koji mi je potreban kod odabira 20  - Zbrojni nalog
	SELECT a.INC_SUB_TYP_DESC
	FROM
	income_subtype a,
	income_item b
	WHERE a.INC_SUB_TYP_CODE = b.INC_SUB_TYPE_CODE
	AND a.INC_TYP_ID;

SELECT * FROM income_item;	

SELECT * FROM income_subtype;

-----a.inc_ite_id, b.inc_typ_id

SELECT a.inc_ite_id,a.inc_hea_id, b.inc_typ_id, b.inc_sub_typ_code, b.inc_sub_typ_desc
FROM
	income_subtype b,
	income_item a
	WHERE 
		b.inc_typ_id=120675019;

		
sELECT bat_def_dsc from batch_def  where BAT_DEF_DSC = 'bl20';

sELECT * from batch_def  where BAT_DEF_DSC = 'bl20';


SELECT * FROM Organization_unit where org_uni_id = 53253;   --- opæeniti id za batch obrade  - to je zapravo dummy za batch obrade i sve obrade imaju istu organizacijsku jedinicu



 		SELECT  
 		  a.iban,
 		  a.iban_or_acc_no,
            a.benef_cus_name, 
            a.benef_cus_address,
            a.debit_ref_typ,
            a.debit_ref_no,
            a.credit_ref_typ,          
            a.amount,
            a.purpose_code,
            a.iban_or_acc_no,
            a.benef_cus_city,
            a.benef_country_code,
            a.benef_swift_address,
            a.benef_bank_name, 
            a.benef_bank_address,     
            a.benef_bank_city,  
            a.benef_bank_cou_cod, 
            a.benef_type,
            a.payment_desc,
            a.pay_type_desc,
            a.inc_sub_type_code,
            a.debit_ref_no,
            a.priority_flag,
            a.credit_ref_no,
            a.charging_option,
            a.cur_id
		 FROM income_item a      
        WHERE a.inc_ite_id = 455837019;

select * from customer_account;


select * from income_item;


select
iban_acc_no
FROM customer_account
where cus_acc_no = '3205000286';   --cus_acc_no ili benef_cus_acc_no

select
iban_acc_no
FROM customer_account
where cus_acc_no = '3205000286';


-- to je iban koji mi treba  dohvat po broju raèuna  veæ imam popunjen benef_cus_acc_no



