SET CURRENT_SCHEMA = 'SIP'


select * from income_subtype;

select * from bank_product_type;

select * from rbhr_gpc_prod_sch;   --- - kodovi 

select * from rbhr_gpc_prod_sch where gpc_code like  'CA0300' and RBHR_CODE like '006-07010400';   -- ban_pro_typ_id = 790,662,005



select * from customer_account;

select * from customer_account where iban_acc_no like 'HR3024840081105882356' with ur;  --- dohvaæam jedan od raèuna 

------ svi ostali raèuni iz prijave
select * from customer_account where iban_acc_no like 'HR2424840081100452113' with ur;   -- ban_pro_typ_id 23999
select * from customer_account where iban_acc_no like 'HR8824840081105759674' with ur;  -- ban_pro_typ_id 23999
select * from customer_account where iban_acc_no like 'HR3024840081105882356' with ur;  -- ban_pro_typ_id 23999

select * from bank_product_type where  ban_pro_typ_id = '23999' ;  -- 800 Redovan devizni raèun  pravne osobe 



select BAN_PRO_TYP_ID, pur_sub_id, pro_cat_id from customer_account where iban_acc_no like 'HR3024840081105882356' with ur;  --- dohvaæam jedan od raèuna 

select * from bank_product_type where code = '800';  -- Redovan devizni raèun  pravne osobe   -- ban_pro_typ_id = 23999, abn_rel_type = 001,  pur_id = 6999

---- sa kojim atributima sve napadamo tablicu rbhr_gpc_prod_sch  ili Tablica služi za povezivanje Sirius proizvoda (ali ne samo proizvoda veæ i nižu razradu od proizvoda ) na GPC i RBHR DWH šifre

select * from rbhr_gpc_prod_sch where ban_pro_typ_id = 23999;   --- - kodovi po ban_pro_typ_id-u  -- nema
---- za 800 

select * from rbhr_gpc_prod_sch where ban_pro_typ_id = 790662005; 

select * from bank_product_type where ban_pro_typ_id = 790662005;  --- to je vrsta proizvoda devizni raèun CODE - 941 a ne 800 redovan devizni raèun

select * from bank_product_type where   code  = '941' ;   --- ban_pro_typ_id = 790662005


 ------ SQL iz commona YNZC0.sqlj   --- za navedene parametre nema dohvata podataka  --- 

			SELECT
                    RBH_GPC_PRO_SCH_ID,
                    BAN_PRO_TYP_ID,
                    PUR_SUB_ID,
                    PRO_CAT_ID,
                    PRO_MEM_BIN_CLA_ID,
                    OVE_TYP_ID,
                    GPC_CODE,
                    RBHR_CODE,
                    BANK_SIGN,
                    USER_LOCK
                FROM
                    RBHR_GPC_PROD_SCH
                WHERE
                    (BAN_PRO_TYP_ID = '790662005' OR BAN_PRO_TYP_ID IS NULL)      --- 	ID proizvoda - iz bank_product_type_code
                  AND (PUR_SUB_ID = null OR PUR_SUB_ID IS NULL)                  --- ID poslovne namjene     -- iz customer account
                   AND (PRO_CAT_ID = '3999' OR PRO_CAT_ID IS NULL)                  --- ID knjigovtsvene namjene  -- iz customer account
                    AND PRO_MEM_BIN_CLA_ID IS NULL
                    AND OVE_TYP_ID IS NULL
                    AND BANK_SIGN = 'RB'
 
--------------------------------------------

	SELECT
                    RBH_GPC_PRO_SCH_ID,
                    BAN_PRO_TYP_ID,
                    PUR_SUB_ID,
                    PRO_CAT_ID,
                    PRO_MEM_BIN_CLA_ID,
                    OVE_TYP_ID,
                    GPC_CODE,
                    RBHR_CODE,
                    BANK_SIGN,
                    USER_LOCK
                FROM
                    RBHR_GPC_PROD_SCH
                WHERE
                    (BAN_PRO_TYP_ID = '23999' OR BAN_PRO_TYP_ID IS NULL)      --- 	ID proizvoda - iz bank_product_type_code
                  --AND (PUR_SUB_ID = null OR PUR_SUB_ID IS NULL)                  --- ID poslovne namjene     -- iz customer account
                  -- AND (PRO_CAT_ID = '3999' OR PRO_CAT_ID IS NULL)                  --- ID knjigovtsvene namjene  -- iz customer account
                    AND PRO_MEM_BIN_CLA_ID IS NULL
                    AND OVE_TYP_ID IS NULL
                    AND BANK_SIGN = 'RB'


-----------------------------------
-- prijava 5817

select * from customer;

select * from customer where register_no = 4867;

select * from income_item;

--- sumnjivi raèuni 025566197 i 

SELECT * FROM CUSTOMER_account;



SELECT * FROM CUSTOMER_account where iban_acc_no like '23900011100019224';

                    
