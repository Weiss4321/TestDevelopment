SET CURRENT_SCHEMA = SIDEV;

SELECT * from customer_subtype;



---    Treba dohvatiti sve aktivne obrtnike (jmbg, sifru komitenta i naziv)  koji imaju racun u blokadi.

SELECT * from customer;    --- cus_typ_id = 999     to je obrtnik

SELECT * from customer_type;   -- treba mi cus_typ_id = 999     to je obrtnik

SELECT * from customer_account;   -- tu mi treba polje Cus_acc_status    --> status B - blocked

-- code je jmbg, sifra komitenta je register no naziv je name

--- customer_type b


SELECT cus_acc_name,cus_acc_status from customer_account WHERE cus_acc_status = 'B';   --- samo dohvat imena sa statusom B
SELECT name from customer_type WHERE cus_typ_id = 999;  ---  samo dohvat imena gdje je cus typ id 999  -- Obrtnik

--- dohvat loš pokušaj - fali mi cus_id 

SELECT c.code, c.register_no, c.name from customer c, customer_account ca
WHERE c.cus_typ_id = 999 and ca.cus_acc_status = 'B';

-- dohvat sa cus_id-om   -- fali mi status "Aktivni" obrtnik  -- to je u tablici Customer i kolona Status -- 0 Aktivan

SELECT c.code, c.register_no, c.name from customer c, customer_account ca 
WHERE 
	c.cus_typ_id = 999
	AND ca.cus_acc_status = 'B' 
	AND c.status = '0'
	AND c.cus_id=ca.cus_id

-- 2 sluèaj za domaæu pravnu osobu   SELECT * from customer_type;   -- treba mi cus_typ_id = 2999      -- tu je problem jer imam više istih dohvata koji se nisu manifestirali u prvom sluèaju

SELECT c.code, c.register_no, c.name from customer c, customer_account ca
WHERE 
	c.cus_typ_id = 2999
	AND ca.cus_acc_status = 'B' 
	AND c.status = '0'
	AND c.cus_id=ca.cus_id
	
--- moguæe rješenje u dohvatu  -- sa * dohvaæam sve i pokušavam uoèiti po kojem id-u mogu bolje pretražiti ovaj skup  -- samo napravim group by  ali bolje je sa distinct

SELECT distinct c.code, c.register_no, c.name, c.address from customer c, customer_account ca
WHERE 
	c.cus_typ_id = 2999
	AND ca.cus_acc_status = 'B' 
	AND c.status = '0'
	AND c.cus_id=ca.cus_id
	--GROUP BY  c.code, c.register_no, c.name, c.address


