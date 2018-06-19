SET CURRENT_SCHEMA = DCDEV;

select NAME from COUNTRY;

select eab.name,
       eab.email,
       c.shortcut_char,
       au.user_name from edu_add_book eab left outer join country c on eab.cou_id=c.cou_id 
                                          left outer join app_user au on eab.use_id=au.use_id;
                                        

select eab.name,
       eab.email,
       c.shortcut_char,
       au.user_name from edu_add_book eab left outer join country c on eab.cou_id=c.cou_id 
                                          left outer join app_user au on eab.use_id=au.use_id

select * from EDU_ADD_BOOK;



