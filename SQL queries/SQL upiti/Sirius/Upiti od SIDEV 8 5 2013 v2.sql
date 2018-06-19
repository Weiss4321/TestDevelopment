SET CURRENT_SCHEMA = DCDEV;



SELECT * FROM DCDEV.APP_USER WHERE USER_NAME LIKE 'Lovro%';

SELECT * FROM DCDEV.APP_USER;



select * from app_user where login='VU00301';

INSERT INTO "DCDEV   "."APP_USER" (USE_ID,USER_NAME,USER_NAME_SC,LOGIN,ABBREVIATION,GRO_ID,ACTIVE_FLAG,DATE_FROM,DATE_UNTIL,CUS_ID,BANK_SIGN,USER_LOCK) 
VALUES (469323003,'Lovro Cvitaš                                                   ','ORZURCZLXAW                                                     ','VU00301             ','301     ',999,'A',{d '2013-04-10'},{d '9999-12-31'},null,'UH',{ts '2013-04-10 15:23:51.253025'});
