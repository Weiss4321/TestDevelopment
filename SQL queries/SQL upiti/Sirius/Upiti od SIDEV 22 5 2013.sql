SET CURRENT_SCHEMA = 'SIDEV'

;                                          
                                        

       SELECT * FROM SYSTEM_CODE_VALUE WHERE sys_cod_id like '%exec%';    

        SELECT * FROM  batch_log WHERE exec_status  = 'D';                      
                                  