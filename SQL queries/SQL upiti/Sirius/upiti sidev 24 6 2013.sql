
SET CURRENT_SCHEMA = SIDEV;

SELECT * FROM  organization_unit;

SELECT code FROM  organization_unit;  -- dohvat commona YXWC  

SELECT * FROM INCOME_HEAD;


SELECT MAX(value_date) FROM event;

SELECT * FROM turnover;

SELECT * FROM turnover_type;                        --- turnoover type tablica - gdje su zapisane vrste prometa koje trebam dodatno unjesti u doradu

SELECT * FROM turnover_type WHERE code like '5306c%';   

SELECT * FROM turnover_type WHERE name like 'R%';  

SELECT * FROM turnover_type WHERE name like 'W%';  


SELECT * FROM turnover_type WHERE tur_typ_id = 203992005;   -- Vanbilanèno knjiženje OD(C)     

SELECT * FROM turnover_type WHERE tur_typ_id = 3718517667;   -- Prij.javnobilj.tr. na utuž. potr 


SELECT * FROM turnover_type WHERE tur_typ_id =  1561010887; ----------- Knjiženje individ.rezervacije  

SELECT * FROM turnover_type WHERE tur_typ_id =  203990005;

SELECT * FROM turnover_type WHERE tur_typ_id =  272350005;

SELECT * FROM event_trx;    -- tablica sa transakcijskim podacima?







SELECT * FROM customer_account;  --- gledao sam cus_acc_status unutar ove tablice da vidim koji statusi postoje

SELECT * FROM customer_account WHERE CUS_ACC_STATUS = 'W';          ---- svi raèuni sa W statusom  

SELECT * FROM customer_account WHERE CUS_ACC_STATUS = 'I';     -- raèun inkativan



select * from event where eve_id = 6443248704   -- Prijenos na WO  -- tu imam kljuè eve_id i eve_typ_id  -- Bojan

select * from event where eve_id = 6443252704   -- gotovinska uplata na WO   -- tu imam kljuè eve_id i eve_typ_id  -- Bojan

SELECT * FROM turnover where eve_id = 6443248704;   -- iskljuèiti promete koji su kreditna analitika i koji nisu u skupu onoga što oni traže  - odobrenje koje ne sadrži tur_typ_id = 3994573797 i 3994572797 -- 3994573797,3994572797


SELECT * FROM turnover where eve_id = 6443248704 AND TUR_TYP_ID NOT IN (3994573797,3994572797); -- select prometa po doGAÐAJU eve_id = 6443248704 ali koji nije tur_typ_id ( ova dva sluèaja)


SELECT * FROM turnover where eve_id = 6443248704 AND TUR_TYP_ID NOT IN (3994573797,3994572797) AND DEBIT_AMOUNT <> 0 OR CREDIT_AMOUNT <> 0; -- dohvaæam previše toga  -- jer dohvaæam jedno ili drugo

SELECT * FROM turnover where eve_id = 6443248704 AND TUR_TYP_ID NOT IN (3994573797,3994572797) AND DEBIT_AMOUNT <> 0 AND CREDIT_AMOUNT <> 0; -- ne dohvatim ništa  -- jer ovakav sluèaj ne mogu imati zbog 




SELECT * FROM turnover where eve_id = 6443248704 AND TUR_TYP_ID NOT IN (3994573797,3994572797) AND CREDIT_AMOUNT <> 0; -- dohvaæam samo za sluèaj gdje je credit amount razlièit od nule  -- to bi trebalo biti to jer tu dohvaèam sluèaj kada imam promete gdje se odobrava raèun




SELECT * FROM turnover_type;                        --- turnoover type tablica - gdje su zapisane vrste prometa koje trebam dodatno unjesti u doradu

SELECT * FROM turnover_type WHERE code like '5306c%';   

SELECT CODE from TURNOVER_TYPE where tur_typ_id IN (3994573797,3994572797);

SELECT * from TURNOVER_TYPE where tur_typ_id IN (3994573797,3994572797);

SELECT CODE from TURNOVER_TYPE where tur_typ_id = 3994572797;


SELECT * FROM CURRENCY where CUR_ID = 63999;   --  191 HRK



SELECT * FROM CUSTOMER_ACCOUNT WHERE ban_pro_typ_id = 790661005;

SELECT ban_pro_typ_id FROM CUSTOMER_ACCOUNT WHERE ban_pro_typ_id = 790661005;


SELECT * FROM event_trx;     -- tra_typ_id   

SELECT * FROM event_trx WHERE try_typ_id != 1426436003 OR try_typ_id = 2793621003 ; 


select t.PROCESS_DATE, t.VALUE_DATE, cu.CODE_NUM, cu.CODE_CHAR, t.ACCOUNT, ac.NAME, t.DEBIT_AMOUNT, t.CREDIT_AMOUNT 
from CUSTOMER_ACCOUNT ca, turnover t, turn_customer tc, currency cu , account ac 
where
ca.iban_acc_no = 'HR2524840083205002216'
and  tc.process_date between '2013-03-01' and '2013-03-31'
and tc.TUR_ID=t.TUR_ID
and cu.CUR_ID=t.CUR_ID
and tc.ACCOUNT=ac.ACCOUNT
order by t.PROCESS_DATE desc





-- sql za vježbu dohvat podataka
---Treba dohvatiti sve aktivne obrtnike (jmbg, sifru komitenta i naziv)  koji imaju racun u blokadi.

---tablice
--CUSTOMER - komitenti
--CUSTOMER_TYPE - tip komitenta
--CUSTOMER_ACCOUNT - racuni komitenta



SELECT c.code, c.register_no, c.name from customer c, customer_account ca 
WHERE c.cus_typ_id = 999 and ca.cus_acc_status = 'B'
AND c.cus_id=ca.cus_id
ORDER BY 
c.code, c.register_no, c.name;

-- novi SQL problem  28 6 2013
--Treba dohvatiti sve promete po racunu HR2524840083205002216 (to je IBAN) koji su se dogodili u trecem mjesecu ove godine. Izvjestaj mora sadrzavati
--datum kada se promet dogodio   -- OK
--datum valute prometa          -- da li je to neki VALUE DATE????  VALUE DATE iz tablice turnover -- OK
--sifra konta                --- to je polje ACCOUNT iz tablice ACCOUNT  -- OK
--naziv konta			     --- to je polje NAME iz tablice ACCOUNT  -- OK
--numericka sifra valute     -- to je polje CODE_NUM iz tablice CURRENCY  -- imam CUR_ID
--slovcana sifra valute		-- to je polje CODE_CHAR iz tablice CURRENCY  -- OK
--iznos zaduzeja              --- to je polje DEBIT_AMOUNT iz tablice TURNOVER  -- OK
--iznos odobrenja             --  to je polje CREDIT_AMOUNT iz tablice TURNOVER -- OK

--U sql-u moraju biti varijable iban i period datuma


--Tablice:
--TURNOVER - prometi
--TURN_CUSTOMER - podanalitika, tj dodatni podaci o prometu
--ACCOUNT - sifre konta, tj kontni plan banke
--CURRENCY -  valute


SELECT * FROM TURNOVER;   --- ima vezu na TURN_CUSTOMER tako da u polju SUBTURN_TBLNAME se poziva na ovu tablicu,
						---turnover ima debit i credit amount gdje se vide neki prometi , takoðer imam konto ACCOUNT
						
SELECT a.account from turnover a, account b WHERE a.account = b.account; -- OK to vraæa rezultat

SELECT * from turnover a, account b WHERE a.account = b.account; -- OK to vraæa rezultat  -- account 

SELECT * FROM ACCOUNT;

SELECT * FROM CURRENCY;

SELECT * FROM TURN_CUSTOMER;

SELECT * FROM CUSTOMER_ACCOUNT;   -- u njemu imam IBAN_ACC_NO

SELECT iban_acc_no, contract_no FROM CUSTOMER_ACCOUNT WHERE IBAN_ACC_NO like 'HR2524840083205002216';  --- tu dohvaæam IBAN

SELECT * FROM CUSTOMER_ACCOUNT WHERE IBAN_ACC_NO like 'HR2524840083205002216';  --- tu dohvaæam IBAN   -- da li mi možda treba ba_pro_typ_id ???


select name, account from account where account = '679260';  -- dohvat imena i broja raèuna



SELECT cus_code FROM TURNOVER WHERE accounting_date  between '2013-03-1' and '2013-03-31' ;   --   format datuma mora biti toèan
   
SELECT * FROM TURN_CUSTOMER where cus_acc_no like '%3205002216%' and process_date between '2013-03-1' and '2013-03-31' ;  -- ok imam ga


SELECT distinct a.account,a.process_date
FROM TURN_CUSTOMER a, ACCOUNT b 
where a.cus_acc_no like '%3205002216%' 
and a.process_date between '2013-03-1' and '2013-03-31' 
order by a.account,a.process_date;  -- podupit account i process_date   - vraæa nazad broj raèuna i process_date


-- spojiti dohvat ibana sa contract_no i zatim dohvatom raèuna account

SELECT iban_acc_no, contract_no FROM CUSTOMER_ACCOUNT WHERE IBAN_ACC_NO like 'HR2524840083205002216'; -- tu dobijam contract_no sa kojim mogu 

SELECT distinct a.account,a.process_date, c.iban_acc_no,c.contract_no
FROM TURN_CUSTOMER a, ACCOUNT b, CUSTOMER_ACCOUNT c 
where c.contract_no like '3205002216%'
---where a.cus_acc_no like '3205002216%' 
and a.process_date between '2013-03-1' and '2013-03-31'
order by  a.account,a.process_date, c.iban_acc_no,c.contract_no; -- dohvaæam sve raèune, process_date , IBAN i Contract_no


-- ****************************************************************************************************************---


SELECT distinct a.account,a.process_date,b.name, c.iban_acc_no,c.contract_no,d.value_date,d.debit_amount,d.credit_amount,e.code_num,e.code_char  -- code_num i code_char mi treba  -- distinct nije potreban
FROM TURN_CUSTOMER a, ACCOUNT b, CUSTOMER_ACCOUNT c ,TURNOVER d, CURRENCY e
where c.iban_acc_no like 'HR2524840083205002216'
and d.cur_id = e.cur_id
and a.tur_id = d.tur_id
and b.account = d.account
---and a.process_date between '2013-03-1' and '2013-03-31'   -- ovaj uvjet nije potreban 
and d.value_date between '2013-03-1' and '2013-03-31'
order by  a.account,a.process_date, c.iban_acc_no,c.contract_no,d.VALUE_DATE,d.debit_amount,d.credit_amount,e.code_num,e.code_char; -- dohvaæam sve raèune, process_date , IBAN i Contract_no  -- order by nije potreban je trošim resurse

--- **********************************************************************************************************************************---- novi SQL upit sa preinakama


SELECT a.account,a.process_date,b.name, c.iban_acc_no,c.contract_no,d.value_date,d.debit_amount,d.credit_amount,e.code_num,e.code_char,a.cus_acc_id  -- code_num i code_char mi treba  -- distinct nije potreban
FROM TURN_CUSTOMER a, ACCOUNT b, CUSTOMER_ACCOUNT c ,TURNOVER d, CURRENCY e
where c.iban_acc_no = 'HR2524840083205002216'
and d.cur_id = e.cur_id
and a.tur_id = d.tur_id
and b.account = d.account
and a.process_date between '2013-03-1' and '2013-03-31'    
----and d.value_date between '2013-03-1' and '2013-03-31' -- ovaj uvjet nije potreban
order by  a.process_date desc; -- dohvaæam sve raèune, process_date , IBAN i Contract_no  -- order by nije potreban je trošim resurse



select * from batch_def;   -- tablica sa definicijama batcheva koja se upisuju preko unesilice za batcheve

select * from batch_log where bat_def_id = 6266762704;  -- tablica gdje se upisuju logovi o batchevima koji su bili pokretani  -- bfR4 je ovaj bat_def_id

select * from batch_log where bat_def_id = 6441891704;

select * from batch_log where RECORDING_TIME between '2013-06-1 11.00.00.000000' and '2013-06-27 11.00.00.000000'   ;     

select * from batch_def  where bat_def_dsc like'bf%';    



