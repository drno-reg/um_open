#!/usr/local/bin/python
# coding: utf-8

import yaml
import time
from sys import argv
import datetime
import os
import io
import requests
import re

from prometheus_client import CollectorRegistry, start_http_server, Summary, Gauge, Counter, Histogram

REQUEST_TIME = Summary('request_processing_seconds', 'Time spent processing request')
@REQUEST_TIME.time()

def process_request(t):
    date_time_string='2017-02-28 12:00'
    today_date = datetime.date.today()
    date_time = datetime.datetime.strptime(date_time_string, '%Y-%m-%d %H:%M')
    start_time=datetime.datetime.now()
    print ("Начало. Время: ",start_time)

    URL_CONNECTION=yaml_cfg["url_request_for_prometheus"]["URL"]

    HTTP_PROXY=yaml_cfg["url_request_for_prometheus"]["HTTP_PROXY"]

    STRING_TEMPLATE=yaml_cfg["url_request_for_prometheus"]["STRING_TEMPLATE"]

    proxyDict = {
        "http"  : HTTP_PROXY,
        # "https" : https_proxy,
        # "ftp"   : ftp_proxy
    }

    # подключаемся к URL ресурсу через прокси
    try:
        r = requests.get(yaml_cfg["url_request_for_prometheus"]["URL"])
        HTML_BODY=r.text
        # print(HTML_BODY)

        # print(STRING_TEMPLATE)
        # в связи с тем, что при после считывания списка паметров сзади добавляется символ переноса строки, то необходимо его убрать .rstrip('\n')
        FIND_VALUE='%s%s' % (STRING_TEMPLATE[0].get("str").rstrip('\n'),(datetime.datetime.now() + datetime.timedelta(days=-1)).strftime(STRING_TEMPLATE[1].get("str").rstrip('\n')))
        # print(datetime.datetime.now()+ datetime.timedelta(days=1))
        # FIND_VALUE='>result-2018-05-07'
        print("Ищем: ", FIND_VALUE)
        RESULT = re.search(FIND_VALUE, HTML_BODY)

        print(RESULT)
        print(RESULT.start())
        print(RESULT.end())

        try:
           print("Обработка статистики")
           FILENAME=yaml_cfg["url_request_for_prometheus"]["URL"]+HTML_BODY[RESULT.start()+1:RESULT.end()+len(STRING_TEMPLATE[3].get("str").rstrip('\n'))]
           print(FILENAME)
           HTML_TEXT = requests.get(FILENAME)
           print(HTML_TEXT.text)
           HTML_TEXT.encoding = 'utf-8'

           FIND_VALUE="profiles_msisdn_exebid"
           RESULT = re.search(FIND_VALUE, HTML_TEXT.text)
           print(HTML_TEXT.text[RESULT.start():RESULT.end()])
           print(RESULT.start(),RESULT.end()+len('_22-00.txt'))
           METRICS=yaml_cfg["url_request_for_prometheus"]["METRICS"]
           searchObj = re.search( r'(.*)%s(.*)' % (METRICS[0]), HTML_TEXT.text, re.M|re.I)
           print(searchObj)
           if searchObj:
              Value1=int(searchObj.group(0)[len(METRICS[0]):len(searchObj.group(0))])
              print(Value1)
              g.labels(yaml_cfg["url_request_for_prometheus"]["COLLECTOR_NAME"],METRICS[0]).set(Value1)

           searchObj = re.search( r'(.*)%s(.*)' % (METRICS[1]), HTML_TEXT.text, re.M|re.I)
           print(searchObj)
           if searchObj:
              Value1=int(searchObj.group(0)[len(METRICS[1]):len(searchObj.group(0))])
              print(Value1)
              g.labels(yaml_cfg["url_request_for_prometheus"]["COLLECTOR_NAME"],METRICS[1]).set(Value1)
        except BaseException as error:
           print("Ошибка анализа информации %s: ".format(error) % (yaml_cfg["url_request_for_prometheus"]["URL"]))

    except BaseException as error:
        print("Ошибка подключения к ресурсу %s: ".format(error) % (yaml_cfg["url_request_for_prometheus"]["URL"]))



    end_time=datetime.datetime.now()
    print ("Окончание. Время: ",end_time)
    delta_time=end_time-start_time;
    print("На подключение потрачено: ",delta_time.total_seconds(), " или ", delta_time.microseconds/1000000)
    time.sleep(t)


if __name__ == '__main__':
    # на вход подаем информацию
    # путь к yaml файлу
    # имя хоста
    # порт
    # вариант загрузки из YAML

    filepath=argv[1]
    print(argv[1])
    # with open(filepath, 'r') as f:
    with io.open(filepath, encoding='utf-8') as file:
        yaml_cfg = yaml.load(file)
    print(yaml_cfg["url_request_for_prometheus"]["PROJECT"])
    g = Gauge(yaml_cfg["url_request_for_prometheus"]["PROJECT"], 'Description of gauge', ['name', 'key_'])
    PORT_NUMBER=int(argv[2])
    LATENCY_VALUE=int(argv[3])
    # Start up the server to expose the metrics.
    start_http_server(PORT_NUMBER)
    # Generate some requests.
    while True:
        process_request(LATENCY_VALUE)