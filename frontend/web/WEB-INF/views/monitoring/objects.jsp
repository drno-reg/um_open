<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../head.jsp" %>

<html>

<style>
    table {
        border-width: 1px;
        border-style: solid;
        border-color: black;
        border-collapse: collapse;
    }
    table td {
        border-width: 1px;
        border-style: solid;
        border-color: black;
    }
    table th {
        border-width: 1px;
        border-style: solid;
        border-color: black;
        background-color: green;
    }
</style>



<script type="text/javascript">
    //      // Первоначальное формирование документа
    $(document).ready(function() {
        updateTableResult();
        resizeTable();
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
    <table id="objects_table" class="table table-hover table-striped" border="1" bordercolor="#white"/>
    <input type="button" value="Add Row" onclick="" class="data-button" id="add-row" />
</div>
<div id="Status"/>

<script type="text/javascript">
    function updateTableResult(json_result) {
        console.log(json_result);
        // удаляем Шапку таблицы
        document.getElementById("objects_table").deleteTHead();
        // создаем Шапку таблицы

//        var tr = document.getElementById('report_table').tHead.children[0],
//            th = document.createElement('th');
//        th.innerHTML = "Second";
//        tr.appendChild(th);

        var table = document.getElementById("objects_table");
        var thead = table.createTHead();
        thead.setAttribute('style', 'background-color: #2780e3; color: #fff;');

//        var th = document.createElement('TH');
//        th.innerHTML = "<TH>№ п\\п</TH>";
//        thead.appendChild(th);
//        th.innerHTML = "<TH>Хост</TH>";
//        thead.appendChild(th);
        var list_thead = ["№ п\\п", "Хост", "Описание","Шаблон","Статус"];
        var row = thead.insertRow(0), cell;
        for (var i = 0; i < list_thead.length; i++) {
            cell = row.insertCell(i);
            cell.innerHTML = list_thead[i];
        }

        //        cell.setAttribute('style', 'background-color: #2780e3; color: #fff;');
        var row = "";
    };
</script>

<script type="text/javascript">
    function resizeTable() {
        var thElm;
        var startOffset;

        Array.prototype.forEach.call(
            document.querySelectorAll("table th"),
            function (th) {
                th.style.position = 'relative';

                var grip = document.createElement('div');
                grip.innerHTML = "&nbsp;";
                grip.style.top = 0;
                grip.style.right = 0;
                grip.style.bottom = 0;
                grip.style.width = '5px';
                grip.style.position = 'absolute';
                grip.style.cursor = 'col-resize';
                grip.addEventListener('mousedown', function (e) {
                    thElm = th;
                    startOffset = th.offsetWidth - e.pageX;
                });

                th.appendChild(grip);
            });

        document.addEventListener('mousemove', function (e) {
            if (thElm) {
                thElm.style.width = startOffset + e.pageX + 'px';
            }
        });

        document.addEventListener('mouseup', function () {
            thElm = undefined;
        });
    };
</script>


</body>
</html>
