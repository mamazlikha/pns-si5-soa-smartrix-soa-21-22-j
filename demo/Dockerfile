FROM python:3.8-bullseye

WORKDIR /app

RUN curl -o database-tools.deb https://fastdl.mongodb.org/tools/db/mongodb-database-tools-debian10-x86_64-100.5.1.deb

RUN apt install ./database-tools.deb

COPY . .

RUN python3 -m pip install --upgrade pip
RUN python3 -m pip install -r requirements.txt

RUN chmod +x start.sh

ENTRYPOINT [ "./start.sh" ] 
