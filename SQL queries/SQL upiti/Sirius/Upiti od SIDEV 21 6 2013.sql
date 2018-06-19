
SET CURRENT_SCHEMA = SIDEV;




SELECT * FROM APP_USER WHERE USER_NAME  like 'Lovro Cvita*'

SELECT * FROM APP_USER;



SELECT * FROM batch_log where bat_def_id = 6441891704;   -- dohvat podataka obrade bl20

update BATCH_LOG set exec_status = '1' where bat_log_id = 6442952704;    --  setiranje nove vrijednosti za 

commit;   -- za aktiviranje novog statusa u bazi

SELECT * FROM event WHERE CMNT like 'BL20%';


SELECT * FROM event where EVE_TYP_ID = 6441971704;

SELECT * FROM batch_def where bat_def_id = 6441891704;




SELECT * FROM event where EVE_TYP_ID = 6441971704;


SELECT * FROM event where EVE_TYP_ID = 2999 and  EVENT_DATE = '2013-03-17';





SELECT * FROM BATCH_DEF  WHERE bat_def_id = 6441891704;

SELECT C.TABSCHEMA, C.TABNAME,              ---SQLERRMC=TBSPACEID=2, TABLEID=6674, COLNO=2     -- upit za grešku SQL 407 SqlIntegrityConstraintViolationException
C.COLNAME
FROM SYSCAT.TABLES AS T,
SYSCAT.COLUMNS AS C
WHERE C.TABSCHEMA = T.TABSCHEMA
AND C.TABNAME = T.TABNAME
AND T.TBSPACEID = 2
AND T.TABLEID = 6674
AND C.COLNO = 2








