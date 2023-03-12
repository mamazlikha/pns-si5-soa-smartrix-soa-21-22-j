FROM python:3.6

WORKDIR /app

COPY requirements.txt requirements.txt
COPY ceo-ui.py main.py
RUN pip3 install -r requirements.txt

COPY . .

CMD python3 main.py $CLIENTS_NUMBER --default_custommer $DEFAULT_CUSTOMMER