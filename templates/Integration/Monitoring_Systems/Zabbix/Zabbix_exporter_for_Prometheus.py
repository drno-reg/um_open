#!/usr/local/bin/python
# coding: utf-8

import time
# import BaseHTTPServer
import http.server

import warnings
import requests
import contextlib
import simplejson as json
import datetime
# устанавливаем PyYaml
import yaml

from sys import argv

import sys

# from importlib import reload

import os
import platform

# HOST_NAME = 'localhost' # !!!REMEMBER TO CHANGE THIS!!!
# PORT_NUMBER = 8001 # Maybe set this to 9000.

# загружаем конфиг
# пример конфига
# URL=https://server_zabbix/api_jsonrpc.php
# User=zabbix_api_user
# Password=zabbix_api_user
# GroupId=1945
# GroupName=Group1

start_time=datetime.datetime.now()

# вариант загрузки из конфига
# filepath="C:\\Server\\Repositories\\Projects\\um_open\\templates\\Integration\\Monitoring_Systems\\Zabbix\\config\\zabbix_connection.cfg"
# f = open(filepath, 'r')
# connection = {}
# for line in f:
#     k, v = line.strip().split('=')
#     connection[k.strip()] = v.strip()
# f.close()

try:
    from functools import partialmethod
except ImportError:
    # Python 2 fallback: https://gist.github.com/carymrobbins/8940382
    from functools import partial

    class partialmethod(partial):
        def __get__(self, instance, owner):
            if instance is None:
                return self

            return partial(self.func, instance, *(self.args or ()), **(self.keywords or {}))

@contextlib.contextmanager
def no_ssl_verification():
    old_request = requests.Session.request
    requests.Session.request = partialmethod(old_request, verify=False)
    warnings.filterwarnings('ignore', 'Unverified HTTPS request')
    yield
    warnings.resetwarnings()

    requests.Session.request = old_request

class MyHandler(http.server.BaseHTTPRequestHandler):
    def do_HEAD(s):
        s.send_response(200)
        s.send_header("Content-type", "text/html")
        s.end_headers()
    def do_GET(s):
        # """Respond to a GET request."""
        s.send_response(200)
        s.send_header("Content-type", "text/html")
        s.end_headers()

        with no_ssl_verification():
            headers = {"content-type": "application/json-rpc"}
            # получаем токен для Zabbix API
            zabbix_authToken= \
                {"params": {"password": yaml_cfg["zabbix_exporter_for_prometheus"]["Password"], "user": yaml_cfg["zabbix_exporter_for_prometheus"]["User"]}, "jsonrpc":"2.0", "method": "user.login", "id": 0}
            # {"params": {"password": connection.get("Password"), "user": connection.get("User")}, "jsonrpc":"2.0", "method": "user.login", "id": 0}
            GET_request=requests.get(yaml_cfg["zabbix_exporter_for_prometheus"]["URL"], data=json.dumps(zabbix_authToken), headers=headers)
            # print(GET_request.json())
            authToken=GET_request.json()
            # print(authToken.get("result"))

        html_page="<html><head><title>Zabbix Metrics Exporter For Prometheus.</title></head>"
        html_page=html_page+"<body><p># Zabbix metrics for Prometheus</p>"
        html_page=html_page+"<br># Begin time: "+start_time.strftime('%H:%M:%S %d.%m.%Y')
        # html_page=html_page+"<br># In Zabbix by GroupName %s" % (connection.get("GroupName"))
        html_page=html_page+"<br># In Zabbix by GroupName %s" % (yaml_cfg["zabbix_exporter_for_prometheus"]["GroupName"])
        # s.wfile.write("<br>Begin time: ".encode(),().encode())
        # s.wfile.write(authToken.get("result").encode())
        with no_ssl_verification():
            # пример запроса для получения GroupId по имени
            zabbix_get= \
                {
                    "jsonrpc": "2.0",
                    "method": "hostgroup.get",
                    "params": {
                        "output": "extend",
                        "filter": {
                            "name": [
                                yaml_cfg["zabbix_exporter_for_prometheus"]["GroupName"]
                            ]
                        }
                    },
                    "auth": authToken.get("result"),
                    "id": authToken.get("id")
                }
            GET_request = requests.get(yaml_cfg["zabbix_exporter_for_prometheus"]["URL"], data=json.dumps(zabbix_get), headers=headers);
            GET_request.encoding = 'utf-8';
            # сохранение результата в JSON
            result_GroupId=GET_request.json().get("result")
            # print(GET_request.json())
            if (platform.system().find("Windows")!=-1):
                print("По имени группы %s нашел ее id %s" % (yaml_cfg["zabbix_exporter_for_prometheus"]["GroupName"], result_GroupId[0].get("groupid")))
            else:
                print("Search by GroupName %s found id %s" % (yaml_cfg["zabbix_exporter_for_prometheus"]["GroupName"], result_GroupId[0].get("groupid")))

            # print(result_GroupId[0].get("groupid"))
            # s.wfile.write((GET_request.json()))
            # пример запроса для получения информации по группе при наличии GroupId
            zabbix_get= \
                {
                    "jsonrpc": "2.0",
                    "method": "host.get",
                    "params": {
                        "output": ["host"],
                        "groupids": result_GroupId[0].get("groupid"),
                    },
                    "auth": authToken.get("result"),
                    "id": authToken.get("id")
                }
            GET_request = requests.get(yaml_cfg["zabbix_exporter_for_prometheus"]["URL"], data=json.dumps(zabbix_get), headers=headers);
            GET_request.encoding = 'utf-8';
            # сохранение результата в JSON
            result_hosts=GET_request.json().get("result")
            if (platform.system().find("Windows")!=-1):
                print("Найдено хостов: %s. Подробности: %s" % (len(result_hosts), result_hosts))
            else:
                print("Founded hosts: %s. Specification: %s" % (len(result_hosts), result_hosts))
            print(GET_request.json())
            # формируем список hostids, к сожалению по имени нельзя, ждем возможно в новой версии
            hostids=[]
            if (yaml_cfg["zabbix_exporter_for_prometheus"]["Keys"].find("*")!=-1):
                for x in range(0, len(result_hosts)):
                    hostids.append(result_hosts[x].get("hostid"))
            else:
                 hostids=yaml_cfg["zabbix_exporter_for_prometheus"]["HostIDs"].split(",")
            hostnames={}
            for x in range(0, len(result_hosts)):
               hostnames[result_hosts[x].get("hostid")] = result_hosts[x].get("host")
            print(hostnames)
            # считаем количество хостов
            if (platform.system().find("Windows")!=-1):
                print("Нашел хостов: ",len(hostids)," детализация: ",hostids)
            else:
                print("Found hosts: ",len(hostids)," Specification: ",hostids)
            zabbix_get= \
            {
            "jsonrpc": "2.0",
            "method": "item.get",
            "params": {
                "output": "extend",
                "hostids": hostids,
                # "host": "server01" только по одному имени, списком только hostid
                # "search": {
                    #  "key_": connection.get("Keys")
                    # "key_": "vfs.fs.size[/data,free]",
                    # "description": "Имя ФС: /data"
                # },
                "sortfield": "name"
            },
            "auth": authToken.get("result"),
            "id": authToken.get("id")
            }
            GET_request = requests.get(yaml_cfg["zabbix_exporter_for_prometheus"]["URL"], data=json.dumps(zabbix_get), headers=headers);
            GET_request.encoding = 'utf-8';
            result_items_by_hostsid=GET_request.json().get("result")
            # for x in range(0, len(result_items_by_hostsid)):

            # получаем информацию о всех метриках если значение  Keys отсутствует или равно * если указан фильтр то будут возвращаться только необходимые значения
            if (platform.system().find("Windows")!=-1):
                print("Нашел метрик: ", len(result_items_by_hostsid))
            else:
                print("Founded metrics: ", len(result_items_by_hostsid))
            html_page=html_page+"<br># Found hosts: %s" % len(hostids)
            s.wfile.write(html_page.encode())
            result=[]
            for y in range(0, len(hostids)):
            # print(hostids[y])

                for x in range(0, len(result_items_by_hostsid)):
                    if (result_items_by_hostsid[x].get("hostid").find(hostids[y])!=-1):
                        # print(result_items_by_hostsid[x].get("hostid")," равно ли ",hostids[y])
                        if (yaml_cfg["zabbix_exporter_for_prometheus"]["Keys"]!="*"):
                            output="<br>zabbix_metrics{host=\"%s\", key_=\"%s\"} %s" % (hostnames.get(hostids[y]), result_items_by_hostsid[x].get("key_"), result_items_by_hostsid[x].get("lastvalue"))
                            stdout="{'hostname' : '%s', 'key_' : '%s'} : %s" % (hostnames.get(hostids[y]), result_items_by_hostsid[x].get("key_"), result_items_by_hostsid[x].get("lastvalue"))
                            if (yaml_cfg["zabbix_exporter_for_prometheus"]["Keys"].find(result_items_by_hostsid[x].get("key_"))!=-1):
                                print(stdout)
                                #     s.wfile.write("<br>node_filesystem_avail{host=".encode()+hostnames[x].encode()+", key_=".encode()+result_items_by_hostsid[x].get("key_").encode()+"}".encode()+result_items_by_hostsid[x].get("lastvalue").encode())
                                s.wfile.write(output.encode())
                        else:
                            print(stdout)
                            s.wfile.write(output.encode())

            # If someone went to "http://something.somewhere.net/foo/bar/",
            # then s.path equals "/foo/bar/".
        # s.wfile.write("<br>Found hosts in Zabbix: %s".encode() % host_count.encode())
        html_page="<br># You accessed path: %s" % s.path
        end_time=datetime.datetime.now()
        html_page=html_page+"<br># End time: "+end_time.strftime('%H:%M:%S %d.%m.%Y')
        delta_time=end_time-start_time
        html_page=html_page+"<br># Delta time: "+str(delta_time.total_seconds())
        html_page=html_page+"</body></html>"
        s.wfile.write(html_page.encode())

if __name__ == '__main__':
    print(os.name,": ",platform.system(),": ", platform.release())
    # reload(sys)
    # sys.setdefaultencoding('utf8')
    # на вход подаем информацию
    # путь к yaml файлу
    # имя хоста
    # порт
    # вариант загрузки из YAML
    filepath=argv[1]
    with open(filepath, 'r') as f:
       yaml_cfg = yaml.load(f)
    HOST_NAME=argv[2]
    PORT_NUMBER=int(argv[3])
    server_class = http.server.HTTPServer
    httpd = server_class((HOST_NAME, PORT_NUMBER), MyHandler)
    print(time.asctime(), "Server Starts - %s:%s" % (HOST_NAME, PORT_NUMBER))
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass
        httpd.server_close()
        print(time.asctime(), "Server Stops - %s:%s" % (HOST_NAME, PORT_NUMBER))