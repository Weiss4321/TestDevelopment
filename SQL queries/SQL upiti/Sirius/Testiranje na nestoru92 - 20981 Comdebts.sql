

SET CURRENT_SCHEMA = SITP;


select * from customer_account; 

select * from customer_account where cus_acc_no = '3251006691'; 

select * from cusacc_balance_last where cus_acc_id = 12192719610;  -- stanje BALANCE= -10

select * from cusacc_balance where cus_acc_id = 12192719610  ; -- stanje BALANCE= -10


---- i Session -> Scripts -> create data script from sql

select * from cusacc_balance_last where cus_acc_id = 12192719610;


INSERT INTO "SITP    "."CUSACC_BALANCE_LAST" (CUS_BAL_LAST_ID,CUS_ACC_ID,ACC_NUM_ID,CUS_ID,ACCOUNT,BAN_REL_TYPE,AMO_TYPE,PRO_TYP_ID,AVAIL_BALANCE_FLAG,CUS_ACC_REPORT,BALANCE_DATE,CUR_ID,DEBIT_TOTAL,CREDIT_TOTAL,BALANCE,ORD_NO,BANK_SIGN,CUS_ORG_UNI_ID,CUS_SUB_ACC_ID,CUS_SUB_ACC_TBL,CLAUSE_BALANCE,USER_LOCK) VALUES (2238762607,12192719610,2238757607,13234912605,'80404     ','912','100',826790005,'1','1',{d '2013-03-08'},63999,10.00,0.00,-10.00,1,'RB',null,null,null,0.00,{ts '2013-03-08 08:17:48.996402'});

commit;
-------------

-------
select * from customer_account where cus_acc_id = 12192719610;

select *  from cusacc_balance where cus_acc_id = 12192719610;

select *  from cusacc_balance_last where cus_bal_last_id = 2238762607;

-------
delete from cusacc_balance_last where cus_bal_last_id = 2238762607;

commit;


