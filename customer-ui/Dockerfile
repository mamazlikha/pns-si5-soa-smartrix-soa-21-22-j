FROM python:3.6

WORKDIR /app

COPY requirements.txt requirements.txt
COPY consultCustomer.py main.py
RUN pip3 install -r requirements.txt

COPY . .

CMD python3 main.py --customerName $DEFAULT_CUSTOMMER --graphType $DEFAULT_GRAPH_TYPE