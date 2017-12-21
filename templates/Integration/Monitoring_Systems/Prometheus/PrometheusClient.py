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

g = Gauge('zabbix_metrics', 'Description of gauge', ['hostname', 'key_'])


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
        hostids=[]
        if (yaml_cfg["zabbix_exporter_for_prometheus"]["Keys"].find("*")!=-1):
            for x in range(0, len(result_hosts)):
                hostids.append(result_hosts[x].get("hostid"))
        else:
            hostids=yaml_cfg["zabbix_exporter_for_prometheus"]["HostIDs"].split(",")
        hostnames={}
        for x in range(0, len(result_hosts)):
            hostnames[result_hosts[x].get("hostid")] = result_hosts[x].get("host")
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
        for y in range(0, len(hostids)):
            # print(hostids[y])
            for x in range(0, len(result_items_by_hostsid)):
                if (result_items_by_hostsid[x].get("hostid").find(hostids[y])!=-1):
                # print(result_items_by_hostsid[x].get("hostid")," равно ли ",hostids[y])
                    if (yaml_cfg["zabbix_exporter_for_prometheus"]["Keys"]!="*"):
                         output="<br>zabbix_metrics{hostname=\"%s\", key_=\"%s\"} %s" % (hostnames.get(hostids[y]), result_items_by_hostsid[x].get("key_"), result_items_by_hostsid[x].get("lastvalue"))
                         stdout="{'hostname' : '%s', 'key_' : '%s'} : %s" % (hostnames.get(hostids[y]), result_items_by_hostsid[x].get("key_"), result_items_by_hostsid[x].get("lastvalue"))
                         if (yaml_cfg["zabbix_exporter_for_prometheus"]["Keys"].find(result_items_by_hostsid[x].get("key_"))!=-1):
                            # metrics.labels(hostnames.get(hostids[y]), result_items_by_hostsid[x].get("key_")).inc((float((result_items_by_hostsid[x].get("lastvalue")))))
                            print(stdout)
                            # registry = CollectorRegistry()
                            # g1 = Gauge('zabbix_metrics', 'help', ['hostname', 'key_'], registry=registry)
                            # g1.labels('value1', 'value2').set(10)
                            g.labels(hostnames.get(hostids[y]), result_items_by_hostsid[x].get("key_")).set((float((result_items_by_hostsid[x].get("lastvalue")))))
                            # g.set(float((result_items_by_hostsid[x].get("lastvalue"))))   # Set to a given value
                            # h.observe(float((result_items_by_hostsid[x].get("lastvalue"))))
                            #     s.wfile.write("<br>node_filesystem_avail{host=".encode()+hostnames[x].encode()+", key_=".encode()+result_items_by_hostsid[x].get("key_").encode()+"}".encode()+result_items_by_hostsid[x].get("lastvalue").encode())
                    else:
                        print(stdout)
                        # metrics.labels(hostnames.get(hostids[y]), result_items_by_hostsid[x].get("key_")).inc(float((result_items_by_hostsid[x].get("lastvalue"))))
                        g.labels(hostnames.get(hostids[y]), result_items_by_hostsid[x].get("key_")).set((float((result_items_by_hostsid[x].get("lastvalue")))))

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
    # HOST_NAME=argv[2]
    PORT_NUMBER=int(argv[3])
    # Start up the server to expose the metrics.
    start_http_server(8000)
    # Generate some requests.
    while True:
        process_request(7)
        # process_request(random.random())