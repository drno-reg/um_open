#!/usr/local/bin/python
# coding: utf-8

from prometheus_client import CollectorRegistry, start_http_server, Summary, Gauge, Counter, Histogram
import random
import time
import warnings
import requests
import contextlib
import simplejson as json
import datetime
import http.server
import yaml
from sys import argv

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


# Create a metric to track time spent and requests made.
REQUEST_TIME = Summary('request_processing_seconds', 'Time spent processing request')
# c = Counter('my_failures_total', 'Description of counter')
# g = Gauge('my_inprogress_requests', 'Description of gauge')
# c1 = Counter('zabbix_metrics', 'HTTP Failures', ['method', 'endpoint'])
# metrics = Counter('zabbix_metrics', 'HTTP Failures', ['hostname', 'key_'])
# number_add=random.random()
# h = Histogram('request_latency_seconds', 'Description of histogram')



# @g.track_inprogress()
# def f():
#     pass
#
# with g.track_inprogress():
#     pass

# Decorate function with metric.
@REQUEST_TIME.time()
def process_request(t):
    """A dummy function that takes some time."""
    # number_add=random.random()
    # c.inc()     # Increment by 1
    # c.inc(number_add)  # Increment by given value

    # g.inc()      # Increment by 1
    # g.dec(10)    # Decrement by given value
    # g.set(4.2)   # Set to a given value
    # g.set_to_current_time()   # Set to current unixtime

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
        # print("По группе %s нашел %s хостов: %s" % (yaml_cfg["zabbix_exporter_for_prometheus"]["GroupName"], len(result_hosts), result_hosts))
        # print("Фильтр: %s" % (yaml_cfg["zabbix_exporter_for_prometheus"]["HostName"]))

        hostids=[]
        if (yaml_cfg["zabbix_exporter_for_prometheus"]["Keys"].find("~")==-1):
            for x in range(0, len(result_hosts)):
               hostids.append(result_hosts[x].get("hostid"))
        else:
            hostids=yaml_cfg["zabbix_exporter_for_prometheus"]["HostIDs"].split(", ")
        hostnames={}
        hostname_and_ids_filter={}
        hostnames_filter=[]
        # проверяем есть ли фильтр по имени
        if (yaml_cfg["zabbix_exporter_for_prometheus"]["HostName"]!=None):
            for x in range(0, len(result_hosts)):
                hostnames[result_hosts[x].get("hostid")] = result_hosts[x].get("host")
            hostnames_filter=yaml_cfg["zabbix_exporter_for_prometheus"]["HostName"].split(", ")
            print("В вашем фильтре заявлено %s хоста(ов): %s" % (len(hostnames_filter), hostnames_filter))
            # коллекция типа словарь для того, чтобы подающийся на вход список хостов можно было бы покрыть Id для того, чтобы передать в API Zabbix
            for y in range(0, len(hostnames_filter)):
               # print(hostnames_filter[y])
               for x in range(0, len(hostnames)):
                   if (result_hosts[x].get("host").find(hostnames_filter[y])!=-1):
                      hostname_and_ids_filter[result_hosts[x].get("host")] = result_hosts[x].get("hostid")

        # если проверяем все найденные хосты
        else:
            # print("Ищем по всем хостам")
            for y in range(0, len(result_hosts)):
                # print(hostnames_filter[y])
                hostname_and_ids_filter[result_hosts[y].get("host")] = result_hosts[y].get("hostid")
                hostnames_filter.append(result_hosts[y].get("host"))

        # print(hostname_and_ids_filter)

        zabbix_get= \
                {
                    "jsonrpc": "2.0",
                    "method": "item.get",
                    "params": {
                        "output": "extend",
                        "hostids": hostids,
                        # "host": hostnames_filter, #только по одному имени, списком только hostid
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
        Keys=yaml_cfg["zabbix_exporter_for_prometheus"]["Keys"].split(", ")

        # print(hostnames_filter)
        # print(Keys)

        zabbix_get= \
            {
                "jsonrpc": "2.0",
                "method": "item.get",
                "params": {
                    "output": "extend",
                    "filter": {
                        "host": hostnames_filter,
                        "key_": Keys,
                    },
                    "sortfield": "name"
                },
                "auth": authToken.get("result"),
                "id": authToken.get("id")
            }

        GET_request = requests.get(yaml_cfg["zabbix_exporter_for_prometheus"]["URL"], data=json.dumps(zabbix_get), headers=headers);
        GET_request.encoding = 'utf-8';
        result_items_by_hostsid=GET_request.json().get("result")
        # for y in range(0, len(hostids)):
        #     # print(hostids[y])
        #     for x in range(0, len(result_items_by_hostsid)):
        #         if (result_items_by_hostsid[x].get("hostid").find(hostids[y])!=-1):
        #         # print(result_items_by_hostsid[x].get("hostid")," равно ли ",hostids[y])
        #             if (yaml_cfg["zabbix_exporter_for_prometheus"]["Keys"]!="*"):
        #                  output="<br>zabbix_metrics{hostname=\"%s\", key_=\"%s\"} %s" % (hostnames.get(hostids[y]), result_items_by_hostsid[x].get("key_"), result_items_by_hostsid[x].get("lastvalue"))
        #                  stdout="{'hostname' : '%s', 'key_' : '%s'} : %s" % (hostnames.get(hostids[y]), result_items_by_hostsid[x].get("key_"), result_items_by_hostsid[x].get("lastvalue"))
        #                  if (yaml_cfg["zabbix_exporter_for_prometheus"]["Keys"].find(result_items_by_hostsid[x].get("key_"))!=-1):
        #                     # metrics.labels(hostnames.get(hostids[y]), result_items_by_hostsid[x].get("key_")).inc((float((result_items_by_hostsid[x].get("lastvalue")))))
        #                     print(stdout)
        #                     # registry = CollectorRegistry()
        #                     # g1 = Gauge('zabbix_metrics', 'help', ['hostname', 'key_'], registry=registry)
        #                     # g1.labels('value1', 'value2').set(10)
        #                     g.labels(hostnames.get(hostids[y]), result_items_by_hostsid[x].get("key_")).set((float((result_items_by_hostsid[x].get("lastvalue")))))
        #                     # g.set(float((result_items_by_hostsid[x].get("lastvalue"))))   # Set to a given value
        #                     # h.observe(float((result_items_by_hostsid[x].get("lastvalue"))))
        #                     #     s.wfile.write("<br>node_filesystem_avail{host=".encode()+hostnames[x].encode()+", key_=".encode()+result_items_by_hostsid[x].get("key_").encode()+"}".encode()+result_items_by_hostsid[x].get("lastvalue").encode())
        #             else:
        #                 print(stdout)
        #                 # metrics.labels(hostnames.get(hostids[y]), result_items_by_hostsid[x].get("key_")).inc(float((result_items_by_hostsid[x].get("lastvalue"))))
        #                 g.labels(hostnames.get(hostids[y]), result_items_by_hostsid[x].get("key_")).set((float((result_items_by_hostsid[x].get("lastvalue")))))
        # print("Нашел метрик: ", len(result_items_by_hostsid))
        # print(result_items_by_hostsid)
        result=[]
        # отсутсвие фильтра по именам хостов
        if (yaml_cfg["zabbix_exporter_for_prometheus"]["HostName"]!=None):
            for y in range(0, len(hostnames_filter)):
                print(hostnames_filter[y])
                for x in range(0, len(result_items_by_hostsid)):
                    if (result_items_by_hostsid[x].get("hostid").find(hostname_and_ids_filter.get(hostnames_filter[y]))!=-1):
                       # print(result_items_by_hostsid[x].get("hostid")," равно ли ",hostnames_filter[y])
                       if (yaml_cfg["zabbix_exporter_for_prometheus"]["Keys"]!=None):
                           if (yaml_cfg["zabbix_exporter_for_prometheus"]["Keys"].find(result_items_by_hostsid[x].get("key_"))!=-1):
                               print("hostname: \"",hostnames_filter[y], "\", "+result_items_by_hostsid[x].get("key_"), ": \"",result_items_by_hostsid[x].get("lastvalue"), "\"")
                               g.labels(hostnames_filter[y], result_items_by_hostsid[x].get("key_")).set((float((result_items_by_hostsid[x].get("lastvalue")))))
                       else:
                           print("hostname: \"",hostnames_filter[y], "\", "+result_items_by_hostsid[x].get("key_"), ": \"",result_items_by_hostsid[x].get("lastvalue"), "\"")
                           g.labels(hostnames_filter[y], result_items_by_hostsid[x].get("key_")).set((float((result_items_by_hostsid[x].get("lastvalue")))))
        # если есть фильтр
        else:
            # print("Example, full dic")
            # print(hostname_and_ids_filter)
            # print("filter list")
            # print(hostnames_filter)
            # print("full item list")
            # print(result_items)
            for y in range(0, len(hostnames_filter)):
                # print(hostname_and_ids_filter.get(hostnames_filter[y]))
                for x in range(0, len(result_items_by_hostsid)):
                    # print(result_items[x])
                    if (result_items_by_hostsid[x].get("hostid").find(hostname_and_ids_filter.get(hostnames_filter[y]))!=-1):
                       # print(result_items[x].get("hostid").find(hostname_and_ids_filter.get(hostnames_filter[y])))
                       # print(result_items_by_hostsid[x].get("hostid")," равно ли ",hostids[y])
                       if (yaml_cfg["zabbix_exporter_for_prometheus"]["Keys"]!=None):
                           if (yaml_cfg["zabbix_exporter_for_prometheus"]["Keys"].find(result_items_by_hostsid[x].get("key_"))!=-1):
                              print("hostname: \"",hostnames_filter[y], "\", "+result_items_by_hostsid[x].get("key_"), ": \"",result_items_by_hostsid[x].get("lastvalue"), "\"")
                              g.labels(hostnames_filter[y], result_items_by_hostsid[x].get("key_")).set((float((result_items_by_hostsid[x].get("lastvalue")))))
                       else:
                           print("hostname: \"",hostnames_filter[y], "\", "+result_items_by_hostsid[x].get("key_"), ": \"",result_items_by_hostsid[x].get("lastvalue"), "\"")
                           g.labels(hostnames_filter[y], result_items_by_hostsid[x].get("key_")).set((float((result_items_by_hostsid[x].get("lastvalue")))))




    # method=['get', 'post']
    # endpoint=['/', '/submit']
    # for i in range(0,len(method)):
    #     # print(method[i], endpoint[i])
    #     metrics.labels(method[i], endpoint[i]).inc(number_add)
    # c1.labels(, ).inc(number_add)
    # c1.labels(, ).inc(number_add)
    # Increment when entered, decrement when exited.
    time.sleep(t)

if __name__ == '__main__':
    # на вход подаем информацию
    # путь к yaml файлу
    # имя хоста
    # порт
    # вариант загрузки из YAML
    filepath=argv[1]
    with open(filepath, 'r') as f:
        yaml_cfg = yaml.load(f)
    g = Gauge('zabbix_metrics', 'Description of gauge', ['hostname', 'key_'])
    PORT_NUMBER=int(argv[2])
    LATENCY_VALUE=int(argv[3])
    # Start up the server to expose the metrics.
    start_http_server(PORT_NUMBER)
    # Generate some requests.
    while True:
        process_request(LATENCY_VALUE)
        # process_request(random.random())