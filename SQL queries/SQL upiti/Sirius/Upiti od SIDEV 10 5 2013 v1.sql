
SET CURRENT_SCHEMA = SIDEV;

SELECT * FROM APP_USER WHERE USER_NAME  = 'Bojan%' ;	 -- ne radi

SELECT * FROM APP_USER WHERE USER_NAME  like 'Bojan Debel*' ;	-- radi

SELECT * FROM APP_USER WHERE USER_NAME  = 'Bojan Debeljak' ;	


select LOGIN, USER_NAME, USE_ID from APP_USER
WHERE ((RTRIM(LOGIN) LIKE ?) and (USER_NAME_SC like ?)) AND (bank_sign = '"+ bs +"')order by 2;



SELECT LOGIN, USER_NAME, USE_ID, USER_NAME_SC FROM APP_USER;  -- prikaz user name i user name sc

SELECT * FROM APP_USER WHERE USER_NAME  like 'Lovro Cvita*' ;	

SELECT * FROM APP_USER;