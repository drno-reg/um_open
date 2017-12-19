#!/usr/local/bin/python
# coding: utf-8

import warnings
import requests
import contextlib
import simplejson as json

# загружаем конфиг
# пример конфига
# URL=https://server_zabbix/api_jsonrpc.php
# User=zabbix_api_user
# Password=zabbix_api_user
# GroupId=1945
# GroupName=Group1

filepath="C:\\Server\\Repositories\\Projects\\um_open\\templates\\Integration\\Monitoring_Systems\\Zabbix\\config\\zabbix_connection.cfg"
f = open(filepath, 'r')
connection = {}
for line in f:
    k, v = line.strip().split('=')
    connection[k.strip()] = v.strip()
f.close()



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

# with no_ssl_verification():
#     requests.get(connection.get("URL"))
#     print('It works')
#
# try:
#     requests.get(connection.get("URL"))
# except requests.exceptions.SSLError:
#     print('It broken')

# подключаемся к Zabbix API без учета SSL сертификата
with no_ssl_verification():
    headers = {"content-type": "application/json-rpc"}
    # получаем токен для Zabbix API
    zabbix_authToken=\
        {"params": {"password": connection.get("Password"), "user": connection.get("User")}, "jsonrpc":"2.0", "method": "user.login", "id": 0}
    GET_request=requests.get(connection.get("URL"), data=json.dumps(zabbix_authToken), headers=headers)
    print(GET_request.json())
    authToken=GET_request.json()
    print(authToken.get("result"))
    # пример запроса для получения информации по Zabbix серверам
    zabbix_get= \
        {
            "jsonrpc": "2.0",
            "method": "host.get",
            "params": {
                "filter": {
                    "host": [
                        "Zabbix server",
                        "Linux server"
                    ]
                }
            },
            "auth": authToken.get("result"),
            "id": authToken.get("id")
        }
    # пример запроса для получения информации по всем хостам, которые есть
    zabbix_get= \
        {
            "jsonrpc": "2.0",
            "method": "host.get",
            # "method": "item.get",
            "params": {
                # "output": ["hostid"],
                # "output": ["name", "available"],
                # "output": ["itemid"],
                "output": ["name"],
                "sortfield": "name",
                # "output": "extend",
            },
            "auth": authToken.get("result"),
            "id": authToken.get("id")
        }
    # GET_request = requests.get(connection.get("URL"), data=json.dumps(zabbix_get), headers=headers);
    # GET_request.encoding = 'utf-8';
    # # сохранение результата в JSON
    # result_hosts=GET_request.json().get("result")
    # print(result_hosts)
    # # считаем количество хостов
    # print("Нашел хостов: ",len(result_hosts))
    # # имя первого хоста
    # print(result_hosts[1].get("name"))

    # пример запроса для получения GroupId по имени
    zabbix_get= \
        {
            "jsonrpc": "2.0",
            "method": "hostgroup.get",
            "params": {
                "output": "extend",
                "filter": {
                    "name": [
                        connection.get("GroupName")
                    ]
                }
            },
            "auth": authToken.get("result"),
            "id": authToken.get("id")
        }
    GET_request = requests.get(connection.get("URL"), data=json.dumps(zabbix_get), headers=headers);
    GET_request.encoding = 'utf-8';
    # сохранение результата в JSON
    result_GroupId=GET_request.json().get("result")
    # print(result_hosts)
    print(GET_request.json())
    print(result_GroupId[0].get("groupid"))

    # пример запроса для получения информации по группе GroupId
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
    GET_request = requests.get(connection.get("URL"), data=json.dumps(zabbix_get), headers=headers);
    GET_request.encoding = 'utf-8';
    # сохранение результата в JSON
    result_hosts=GET_request.json().get("result")
    print(result_hosts)
    # перегоняем значания id и name хостов в словарь для того, чтобы появилась возможность по hostid itemsid
    hostnames={}
    for x in range(0, len(result_hosts)):
        hostnames[result_hosts[x].get("hostid")] = result_hosts[x].get("host")

    # print("Hostname по id: ", hostnames.get("38464"))

    print(hostnames)

    # print(d.get("38203"))
    # print(GET_request.json())
    # считаем количество хостов
    print("Нашел хостов: ",len(result_hosts))

    zabbix_get=\
    {
        "jsonrpc": "2.0",
        "method": "item.get",
        "params": {
            "output": "extend",
            "hostids": connection.get("HostIDs"),
            # "host": "server01" только по одному имени, списком только hostid
            "search": {
                # "key_": "vfs.fs.size"
                "description": "Имя ФС: /data"
            },
            "sortfield": "name"
        },
        "auth": authToken.get("result"),
        "id": authToken.get("id")
    }
    #пример запроса поиска значений по конкретному полю - в данном случае по ключу
    zabbix_get= \
        {
            "jsonrpc": "2.0",
            "method": "item.get",
            "params": {
                "output": "extend",
                "host": connection.get("HostName"),
                # "host": "server01" только по одному имени, списком только hostid
                "search": {
                    "key_": 'vfs.fs.size[/data,free]'
                    # "description": "Имя ФС: /data"
                },
                "sortfield": "name"
            },
            "auth": authToken.get("result"),
            "id": authToken.get("id")
        }

    hostids=connection.get("HostIDs").split(",")
    # print(hostids)

    zabbix_get= \
        {
            "jsonrpc": "2.0",
            "method": "item.get",
            "params": {
                "output": "extend",
                # "host": connection.get("HostName"),
                "hostids": hostids,
                # "host": "server01" только по одному имени, списком только hostid
                # "search": {
                    # "itemid": '3294956',
                    # "key_": "vfs.fs.size[/data,free]",
                    # "key_": 'vfs.fs.size[/data,free]'
                    # "description": "Имя ФС: /data"
                # },
            "sortfield": "name"
        },
        #     "method": "history.get",
        #         "params": {
        #             "output": "extend",
        #             "history": 0,
        #             "itemids": "3294956",
        #             "sortfield": "clock",
        #             "sortorder": "DESC",
        #             "limit": 10
        #         },
            "auth": authToken.get("result"),
            "id": authToken.get("id")
        }

    zabbix_get= \
        {
            "jsonrpc": "2.0",
            "method": "item.get",
            "params": {
                "output": "extend",
                "hostids": hostids,
                # "search": {
                #     "key_": "system"
                # },
                "sortfield": "name"
            },
            "auth": authToken.get("result"),
            "id": authToken.get("id")
        }

    zabbix_get= \
        {
            "jsonrpc": "2.0",
            "method": "item.get",
            "params": {
                "output": "extend",
                "host": connection.get("HostName"),
                "filter": {
                    "key_": [
                        "vfs.fs.size[/data,free]",
                        "system.cpu.util.usage"
                    ]
                },
                "sortfield": "name"
            },
            "auth": authToken.get("result"),
            "id": authToken.get("id")
        }

    GET_request = requests.get(connection.get("URL"), data=json.dumps(zabbix_get), headers=headers);
    GET_request.encoding = 'utf-8';
    # сохранение результата в JSON
    result_items_by_hostsid=GET_request.json().get("result")
    # print(result_hosts)
    print(result_items_by_hostsid)
    # print("Значение: ", result_items_by_hostsid[0].get("lastvalue"))
    # считаем количество хостов
    # print("Нашел хостов: ",len(result_hosts))
    # получаем информацию о всех метриках если значение  Keys отсутствует или равно * если указан фильтр то будут возвращаться только необходимые значения
    print("Нашел метрик: ", len(result_items_by_hostsid))
    result=[]
    for y in range(0, len(hostids)):
        # print(hostids[y])
        for x in range(0, len(result_items_by_hostsid)):
            if (result_items_by_hostsid[x].get("hostid").find(hostids[y])!=-1):
                # print(result_items_by_hostsid[x].get("hostid")," равно ли ",hostids[y])
                if (connection.get("Keys")!="*"):
                    if (connection.get("Keys").find(result_items_by_hostsid[x].get("key_"))!=-1):
                       print("hostname: \"",hostnames.get(hostids[y]), "\", "+result_items_by_hostsid[x].get("key_"), ": \"",result_items_by_hostsid[x].get("lastvalue"), "\"")
                else:
                    print("hostname: \"",hostnames.get(hostids[y]), "\", "+result_items_by_hostsid[x].get("key_"), ": \"",result_items_by_hostsid[x].get("lastvalue"), "\"")


