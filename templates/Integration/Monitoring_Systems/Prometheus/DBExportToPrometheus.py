#!/usr/local/bin/python
# coding: utf-8

import yaml
import time
from sys import argv
import jpype
import jaydebeapi
import datetime
import os
import io
from prometheus_client import CollectorRegistry, start_http_server, Summary, Gauge, Counter, Histogram

REQUEST_TIME = Summary('request_processing_seconds', 'Time spent processing request')
@REQUEST_TIME.time()

def process_request(t):
    date_time_string='2017-02-28 12:00'
    today_date = datetime.date.today()
    date_time = datetime.datetime.strptime(date_time_string, '%Y-%m-%d %H:%M')
    start_time=datetime.datetime.now()
    print ("Начало. Время: ",start_time)

    USERNAME=yaml_cfg["db_exporter_for_prometheus"]["USERNAME"]
    PASSWORD=yaml_cfg["db_exporter_for_prometheus"]["PASSWORD"]
    URL_CONNECTION=yaml_cfg["db_exporter_for_prometheus"]["URL"]
    PATH_LIB=yaml_cfg["db_exporter_for_prometheus"]["LIB_PATH"]

    jHome = jpype.getDefaultJVMPath()
    sql_script=yaml_cfg["db_exporter_for_prometheus"]["SCRIPT"]
    metrics=yaml_cfg["db_exporter_for_prometheus"]["METRICS"]
    dbname=yaml_cfg["db_exporter_for_prometheus"]["DBNAME"]

    conn = jaydebeapi.connect(yaml_cfg["db_exporter_for_prometheus"]["CLASS"], URL_CONNECTION,[USERNAME,PASSWORD], PATH_LIB)

    for y in range(0, len(sql_script)):
        # print(sql_script[y].get("sql"))

        cursor = conn.cursor()
        cursor.execute(sql_script[y].get("sql"))

        for row in cursor.fetchall():
            print(dbname[y], metrics[y], row[0])
            g.labels(dbname[y],metrics[y]).set(int(str(row[0])))
    cursor.close()

    end_time=datetime.datetime.now()
    print ("Окончание. Время: ",end_time)
    delta_time=end_time-start_time;
    print("На подключение к БД потрачено: ",delta_time.total_seconds(), " или ", delta_time.microseconds/1000000)

    time.sleep(t)


if __name__ == '__main__':
    # на вход подаем информацию
    # путь к yaml файлу
    # имя хоста
    # порт
    # вариант загрузки из YAML

    filepath=argv[1]
    with io.open(filepath, encoding='utf-8') as file:
        yaml_cfg = yaml.load(file)
    g = Gauge(yaml_cfg["db_exporter_for_prometheus"]["PROJECT"], 'Description of gauge', ['dbname', 'key_'])
    PORT_NUMBER=int(argv[2])
    LATENCY_VALUE=int(argv[3])
    # Start up the server to expose the metrics.
    start_http_server(PORT_NUMBER)
    # Generate some requests.
    while True:
        process_request(LATENCY_VALUE)