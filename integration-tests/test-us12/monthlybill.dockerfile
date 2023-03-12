FROM python:3.8

WORKDIR /app

COPY requirements.txt requirements.txt
RUN python3 -m pip install -r requirements.txt

COPY monthlybill.py main.py
COPY start.sh start.sh



RUN chmod +x start.sh

ENTRYPOINT [ "./start.sh" ] 
