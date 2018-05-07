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
import re
from prometheus_client import CollectorRegistry, start_http_server, Summary, Gauge, Counter, Histogram

REQUEST_TIME = Summary('request_processing_seconds', 'Time spent processing request')
@REQUEST_TIME.time()

def find_str(s, char):
    index = 0
    if char in s:
        c = char[0]
        for ch in s:
            if ch == c:
                if s[index:index+len(char)] == char:
                    return index

            index += 1
    return -1

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
    datasorce=yaml_cfg["db_exporter_for_prometheus"]["DATASOURCE"]

    # print(metrics)
    # print(datasorce)

    datasource_array=[]
    metrics_array=[]
    metrics_value=[]
    conn = jaydebeapi.connect(yaml_cfg["db_exporter_for_prometheus"]["CLASS"], URL_CONNECTION,[USERNAME,PASSWORD], PATH_LIB)
    for i in range(0, len(datasorce)):
        print(datasorce[i])
        if datasorce[i] in metrics:
            print(len(metrics[datasorce[i]]))
            print(metrics[datasorce[i]])
            for y in range(0, len(metrics[datasorce[i]])):
                print(metrics[datasorce[i]][y])
                sql_script="""
                    show partitions %s
                    """ % (metrics[datasorce[i]][y])
                print(sql_script)
                cursor = conn.cursor()
                # cursor.execute(sql_script[y].get("sql"))
                cursor.execute(sql_script)
                result=[]
                for row in cursor.fetchall():
                    result.append(row[0])
                    # print(row[0])
                    # if (find_str(result[len(result)-1],'/')==-1):
                    # разбор business_dt=2015-10-01
                    if (result[len(result)-1].count('/')==0):
                        # print("Подневное партиционирование v1: ", result[len(result)-1])
                        date_time_string=result[len(result)-1][find_str(result[len(result)-1],'=')+1:]
                        # print(date_time_string)
                        metrics_time = datetime.datetime.strptime(date_time_string, '%Y-%m-%d')
                        # print(metrics_time)
                        now_datetime=datetime.datetime.now()
                        delta_time=now_datetime-metrics_time
                        # print(delta_time.days)
                        datasource_array.append(datasorce[i])
                        metrics_array.append(metrics[datasorce[i]][y])
                        metrics_value.append(int(delta_time.days))
                        # g.labels(datasorce[i], metrics[datasorce[i]][y]).set(int(delta_time.days))
                    # if (find_str(result[len(result)-1],'/')==1):
                    # разбор t_start_date_ymd=20170620/t_start_date_ymdh=2017062001
                    if (result[len(result)-1].count('/')==1):
                        # print("Почасовое партиционирование: ", result[len(result)-1])
                        date_time_string=result[len(result)-1][:find_str(result[len(result)-1],'/')]
                        date_time_string=date_time_string[find_str(date_time_string,'=')+1:]
                        # print(date_time_string)
                        metrics_time = datetime.datetime.strptime(date_time_string, '%Y%m%d')
                        # print(metrics_time)
                        now_datetime=datetime.datetime.now()
                        delta_time=now_datetime-metrics_time
                        # print(delta_time.days)
                        datasource_array.append(datasorce[i])
                        metrics_array.append(metrics[datasorce[i]][y])
                        metrics_value.append(int(delta_time.days))
                        # g.labels(datasorce[i], metrics[datasorce[i]][y]).set(int(delta_time.days))
                    if (result[len(result)-1].count('/')>1):
                    # разбор yyyy=2017/mm=10/dd=1
                    # if (find_str(result[len(result)-1],'/')>1):
                    #     print("Подневное партиционирование v2: ", result[len(result)-1])
                        # date_time_string=result[len(result)-1][:find_str(result[len(result)-1],'/')]
                        # date_time_string=date_time_string[find_str(date_time_string,'=')+1:]
                        r=re.compile(r"[^0-9-]")
                        date_time_string=r.sub("",result[len(result)-1])
                        # print(date_time_string)
                        metrics_time = datetime.datetime.strptime(date_time_string, '%Y%m%d')
                        print(metrics_time)
                        now_datetime=datetime.datetime.now()
                        delta_time=now_datetime-metrics_time
                        # print(delta_time.days)
                        datasource_array.append(datasorce[i])
                        metrics_array.append(metrics[datasorce[i]][y])
                        metrics_value.append(int(delta_time.days))
                        # g.labels(datasorce[i], metrics[datasorce[i]][y]).set(int(delta_time.days))

    cursor.close()
    for labels_array in range(0, len(datasource_array)):
        # print(datasource_array[labels_array], ": ",metrics_array[labels_array],": ", metrics_value[labels_array])
        g.labels(datasource_array[labels_array], metrics_array[labels_array]).set(metrics_value[labels_array])

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
    g = Gauge(yaml_cfg["db_exporter_for_prometheus"]["PROJECT"], 'Description of gauge', ['datasource', 'key_'])
    PORT_NUMBER=int(argv[2])
    LATENCY_VALUE=int(argv[3])
    # Start up the server to expose the metrics.
    start_http_server(PORT_NUMBER)
    # Generate some requests.
    while True:
        process_request(LATENCY_VALUE)