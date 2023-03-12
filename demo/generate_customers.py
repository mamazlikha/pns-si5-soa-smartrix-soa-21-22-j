import datetime
import helper
import requests
from dateutil.relativedelta import relativedelta



def generate_data():
    helper.change_clock('2004-12-02T22:00:00.00Z')
    i = 0
    while i < 5000:
        b = helper.create_random_customer()
        if b!=None:
            i += 1
            helper.generate_random_history(b,datetime.datetime(2005,1,1,0),datetime.datetime(2005, 2, 10),step=datetime.timedelta(seconds=3600))

    
    print('Finished generating all customers data')
    start = datetime.datetime(2005, 1, 1,2,0)
    end = datetime.datetime(2005, 2, 10)
    

    
    print("  ...")
    cur = start
    while cur < end:
        helper.change_clock(cur.isoformat()+'Z')
        requests.get(f"{helper.API_MEASURES}/daily-scheduler")
        cur = cur+datetime.timedelta(days=1)


    
    

if __name__=='__main__':
    generate_data()
