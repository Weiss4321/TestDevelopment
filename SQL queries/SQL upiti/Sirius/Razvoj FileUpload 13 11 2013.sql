SET CURRENT_SCHEMA = 'SIDEV'

-------------   dohvat za provjeru u�itane datoteke
select * from income_file where date( RECORDING_TIME) = current_date ;