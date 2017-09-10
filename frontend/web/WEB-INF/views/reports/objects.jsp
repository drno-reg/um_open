<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../head.jsp" %>

<html>

<style>

</style>

<script src="${pageContext.request.contextPath}/resources/highcharts/highcharts.js"></script>
<script src="${pageContext.request.contextPath}/resources/highcharts/modules/exporting.js"></script>
<script src="${pageContext.request.contextPath}/resources/export-csv/js/export-csv.js"></script>

<div id="container" style="width: 600px; height: 400px; margin: 0 auto">
</div>

<script type='text/javascript'>
    $(function () {<!--   www  . j  av  a  2s .com-->
        var $container3 = $('<div>').appendTo(document.body);

        window.chart = new Highcharts.Chart({
            chart: {
                renderTo: $container3[0],
                height: 400
            },
            xAxis: {
                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
            },

            series: [{
                name: 'ненужное',
                data: [29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]
            }]
        });
        var container = document.createElement('div');
        document.body.appendChild(container);

        window.chart = new Highcharts.Chart({
            chart: {
                renderTo: container,
                height: 400
            },
            xAxis: {
                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
            },

            series: [{
                name: 'фигня',
                data: [129.9, 171.5, 106.4, 19.2, 14.0, 176.0, 35.6, 148.5, 16.4, 194.1, 5.6, 154.4]
            }]
        });

        var container10 = document.createElement('div');
        document.body.appendChild(container10);
var values=[94, "95", "95", "95", "94", "94", "94", "94", "94", "94", "94", "94", "94", "94", "94", "94", "94", "94", "72", "69", "69", "69", "69", "68", "68", "66", "67", "70", "70", "70", "70", "70", "70", "70", "70", "70"];
var times=["07:15:00 07.09.17", "07:20:00 07.09.17", "07:25:00 07.09.17", "07:30:00 07.09.17", "07:35:00 07.09.17", "07:40:00 07.09.17", "07:45:00 07.09.17", "07:50:00 07.09.17", "07:55:00 07.09.17", "08:00:00 07.09.17", "08:05:00 07.09.17", "08:10:00 07.09.17", "08:15:00 07.09.17", "08:20:00 07.09.17", "08:25:00 07.09.17", "08:30:00 07.09.17", "08:35:00 07.09.17", "08:40:00 07.09.17", "08:45:00 07.09.17", "08:50:00 07.09.17", "08:55:00 07.09.17", "09:00:00 07.09.17", "09:05:00 07.09.17", "09:10:00 07.09.17", "09:15:00 07.09.17", "09:20:00 07.09.17", "09:25:00 07.09.17", "09:30:00 07.09.17", "09:35:00 07.09.17", "09:40:00 07.09.17", "09:45:00 07.09.17", "09:50:00 07.09.17", "09:55:00 07.09.17", "10:00:00 07.09.17", "10:05:00 07.09.17", "10:10:00 07.09.17"];

        window.chart = new Highcharts.Chart({
            chart: {
                renderTo: container10,
                height: 400
            },
            xAxis: {
                categories: times
            },

            series: [{
                name: 'фигня',
                data: values
            }]
        });

    });
</script>

<script type="text/javascript">

    //    var myChart = Highcharts.chart('container', {
//        chart: {
//            type: 'bar'
//        },
//        title: {
//            text: 'Fruit Consumption'
//        },
//        xAxis: {
//            categories: ['Apples', 'Bananas', 'Oranges']
//        },
//        yAxis: {
//            title: {
//                text: 'Fruit eaten'
//            }
//        },
//        series: [{
//            name: 'Jane',
//            data: [1, 0, 4]
//        }, {
//            name: 'John',
//            data: [5, 7, 3]
//        }]
//    });
//    myChart.renderTo('container');

</script>

<script type="text/javascript">
    //      // Первоначальное формирование документа
    $(document).ready(function() {



//        $.getJSON('http://www.highcharts.com/samples/data/jsonp.php?filename=aapl-c.json&callback=?', function (data)    {
//            // Create the chart
//            var dataObject = {
//                rangeSelector: {
//                    selected: 1,
//                    inputEnabled: $('#chart1').width() > 480
//                },
//
//                title: {
//                    text: 'AAPL Stock Price'
//                },
//
//                series: [{
//                    name: 'AAPL',
//                    data: data,
//                    tooltip: {
//                        valueDecimals: 2
//                    }
//                }],
//
//                chart: {
//                    renderTo: 'chart1'
//                }
//
//            };
//
//            var chart = new Highcharts.StockChart(dataObject);
//        });

        // получаем данные по наименованию системы
        $.getJSON( '../MonitoringObjects', {
            get_info: "IS_NAME"
        }).done(function( jsondata ) {
//             console.log(jsondata[0].IS_NAME);
//              var result=jsondata;
//             console.log(result);
//            console.log(result[0].IS_NAME);
//                for (var i = 0; i < jsondata.length; i++) {
//                    //   $('#main_selectpicker').append('<option value="'+jsondata.divisions[i].name+'">'+jsondata.divisions[i].name+'</option>');
//                    //  console.log(jsondata.hostnames[i].name);
//                    items[i]=jsondata[i].IS_NAME;
//                }
            // items=['a06-duty02', 'a00-duty01', 'a03-duty02'];
//            $("#main_selectpicker")
            //  .html('<option>'+jsondata.divisions[i].name+'</option>')
//                .selectpicker('refresh');
//
//                function split( val ) {
//                    return val.split( /,\s*/ );
//                }
//                function extractLast( term ) {
//                    return split( term ).pop();
//                }
//
//                $( "#search" )
//                    .autocomplete({
//                        minLength: 0,
//                        source: function(request, response) {
//                            var results = $.ui.autocomplete.filter(items, request.term);
//                            response(results.slice(0, 15));
//                        },
//
//                        //   source: function( request, response ) {
//                        //       response( $.ui.autocomplete.filter(
//                        //           items, extractLast( request.term ) ) );
//                        //  },
//                        focus: function() {
//                            return false;
//                        },
//                        select: function( event, ui ) {
//                            var terms = split( this.value );
//                            // remove the current input
//                            terms.pop();
//                            // add the selected item
//                            terms.push( ui.item.value );
//                            // add placeholder to get the comma-and-space at the end
//                            terms.push("");
//                            //this.value = terms.join(", ");
//                            this.value = terms.join("");
//                            return false;
//                        }
        })
        //            document.getElementById("loading").style.display = "none";
        //                document.getElementById("search").placeholder = "Введите наименование системы...";

        //            })
            .fail(function(status) {
                document.getElementById("Status").innerHTML=status.responseText;
            });

    });
</script>
<div class="container">
<br>
<br>
<br>
    <br>

<div class="panel panel-default">
    <div class="panel-heading">Список объектов</div>
</div>

</div>
<div id="Status"/>

<script type="text/javascript">
    $(function () {

    });

</script>

<script type="text/javascript">

</script>

<script type="text/javascript">
    $(function () {
        $('#container2').highcharts({
            chart: {
                zoomType: 'x'
            },
            title: {
                text: 'График'
            },
            //   subtitle: {
            //       text: document.ontouchstart === undefined ?
            //               'Есть возможность увеличить масштаб простым выделением интересующего участка графика' :
            //               'Pinch the chart to zoom in'
            //   },
            xAxis: {
                //  type: 'datetime',
                // minRange: 14 * 24 * 3600000 // fourteen days
                categories: ["00:00","01:00"]
            },
            yAxis: {
                title: {
                    text: ''
                }
            },
            // legend: {
            //     enabled: false
            //  },
            plotOptions: {
                area: {
                    fillColor: {
                        linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
                        stops: [
                         //   [0, Highcharts.getOptions().colors[0]],
                         //   [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                        ]
                    },
                    marker: {
                        radius: 2
                    },
                    lineWidth: 1,
                    states: {
                        hover: {
                            lineWidth: 1
                        }
                    },
                    threshold: null
                }
            },

            series: [{
                //  type: 'area',
                name: 'херь',
                //  pointInterval: 24 * 3600,
                //  pointStart: Date.UTC(2006, 0, 1),
                data: [1,2]
            }]
        });
    });
</script>

<div id="container2" style="min-width: 410px; height: 550px; margin: 0 auto"></div>

<%--<div id="chart1"></div>--%>

<div id="container1" style="min-width: 310px; height: 400px; margin: 0 auto"></div>


<div id="chart1" style="min-width: 310px; height: 400px; margin: 0 auto"></div>

</body>
</html>
