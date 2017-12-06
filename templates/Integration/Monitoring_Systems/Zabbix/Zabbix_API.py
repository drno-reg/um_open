#!/usr/local/bin/python
# coding: utf-8

import warnings
import requests
import contextlib
import simplejson as json


# загружаем конфиг
# пример конфига
# url=
# user=
# password=

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
#     requests.get(connection.get("url"))
#     print('It works')
#
# try:
#     requests.get(connection.get("url"))
# except requests.exceptions.SSLError:
#     print('It broken')

# подключаемся к Zabbix API без учета SSL сертификата
with no_ssl_verification():
    headers = {"content-type": "application/json-rpc"}
    # получаем токен для Zabbix API
    zabbix_authToken=\
        {"params": {"password": connection.get("user"), "user": connection.get("password")}, "jsonrpc":"2.0", "method": "user.login", "id": 0}
    GET_request=requests.get(connection.get("url"), data=json.dumps(zabbix_authToken), headers=headers)
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
    GET_request = requests.get(connection.get("url"), data=json.dumps(zabbix_get), headers=headers);
    GET_request.encoding = 'utf-8';
    # сохранение результата в JSON
    result_hosts=GET_request.json().get("result")
    print(result_hosts)
    # считаем количество хостов
    print("Нашел хостов: ",len(result_hosts))
    # имя первого хоста
    print(result_hosts[1].get("name"))



