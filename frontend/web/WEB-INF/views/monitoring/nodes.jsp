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
        resizeTable();
        // получаем данные по наименованию системы
        $.getJSON( 'MonitoringObjects', {
            get_info: "IS_NAME",
            user_id: 4
        }).done(function( jsondata ) {
               updateTableResult(jsondata);
        })

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
    <div class="panel-heading">Список нод</div>
</div>
    <table id="nodes_table" class="table table-hover table-striped" border="1" bordercolor="#white"></table>
    Нода
    <input type="button" value="Обновить" onclick="updaterow();" class="data-button" id="upd-row" />
    <input type="button" value="Добавить" onclick="addnewrow();" class="data-button" id="add-row" />
    <input type="button" value="Удалить" onclick="deleterow();" class="data-button" id="del-row" />
</div>
<div id="Status"/>

<script type="text/javascript">
    function deleterow() {
        console.log("Удаляем");
        [].forEach.call(document.getElementsByTagName("input"), function (elements_form) {
            if (([elements_form.getAttribute('id')].toString()).indexOf("CHECKBOX_")!=-1) {
                if (document.getElementById(elements_form.getAttribute('id')).checked) {
                    const tableRows = [].slice.call(document.querySelectorAll('#nodes_table tr'))



                    console.log(elements_form.getAttribute('id'));

                    var myTable = document.getElementById("nodes_table");
                    var rowCount = myTable.rows.length;
                    var countrows=$('#nodes_table tr').length;
                    var oRows = document.getElementById('nodes_table').getElementsByTagName('tr');
                    var iRowCount = oRows.length;
                           console.log("Удаляем, считаем rowCount :: "+rowCount+" или "+countrows+" или "+iRowCount);
                    if (myTable.rows.length>1){
                      //  for (var x=rowCount-1; x>0; x--) {
                      //      myTable.deleteRow(x);
                      //  }
                    }
                    var checkboxes = document.getElementsByTagName("input");

                    for (var i = 0; i < checkboxes.length; i++) {
                        var checkbox = checkboxes[i];
                        checkbox.onclick = function() {
                            var currentRow = this.parentNode.parentNode;
                            var secondColumn = currentRow.getElementsByTagName("td")[1];

                           // alert("My text is: " + secondColumn.textContent );
                            console.log("My text is: " + secondColumn.textContent);
                        };
                    }

                }
            }
        });
    }
    function updaterow() {
        console.log("Обновляем");
    }
    function SELECT_ALL_CHECKBOX(){
        var arrayList = [];
        if (document.getElementById('SELECT_ALL_CHECKBOX').checked)
        {
            [].forEach.call(document.getElementsByTagName("input"), function (elements_form) {
                if (([elements_form.getAttribute('id')].toString()).indexOf("CHECKBOX_")!=-1) {
//                    console.log(elements_form.getAttribute('id'));
                    document.getElementById(elements_form.getAttribute('id')).checked=true;
                }
            });
        }
        else{
            [].forEach.call(document.getElementsByTagName("input"), function (elements_form) {
                if (([elements_form.getAttribute('id')].toString()).indexOf("CHECKBOX_")!=-1) {
                    document.getElementById(elements_form.getAttribute('id')).checked=false;
                }
            });
        }

    }

    function addnewrow(){
        console.log("Добавляем новую строку");
        var myTable = document.getElementById("nodes_table");
        var rowCount = myTable.rows.length;
        console.log("Добавляем, считаем rowCount :: "+rowCount);
        var row="<thead class='thead-inverse' style='background: #F8F8FF'> <tr id='node_row_"+rowCount+"'>";
        row=row+"<td><input type='checkbox' id='CHECKBOX_"+rowCount+"'></td><td></td><td></td><td></td>";
//        for (var i = 3; i < Object.keys(jsondata[jsondata.length-1]).length; i++) {
//            row=row+"<td id='Constant_Weight_"+Object.keys(jsondata[jsondata.length-1])[i]+"'>"+jsondata[jsondata.length-1][Object.keys(jsondata[jsondata.length-1])[i]]+"</td>";
//            summa_const.push(jsondata[jsondata.length-1][Object.keys(jsondata[jsondata.length-1])[i]]);
//            // console.log(Object.keys(jsondata[jsondata.length-1])[i]);
//        }
        row=row+"<td/></tr></thead>";
        $("#nodes_table").append(row);
    }

    function updateTableResult(json_result) {
        console.log(json_result);
        // удаляем Шапку таблицы
        document.getElementById("nodes_table").deleteTHead();
        // создаем Шапку таблицы

//        var tr = document.getElementById('report_table').tHead.children[0],
//            th = document.createElement('th');
//        th.innerHTML = "Second";
//        tr.appendChild(th);

        var table = document.getElementById("nodes_table");
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
            if (i==0){
                cell.innerHTML = "<input type='checkbox' id='SELECT_ALL_CHECKBOX' onclick='SELECT_ALL_CHECKBOX();'>"+" "+list_thead[i];
            }
            else {
                cell.innerHTML = list_thead[i];
            }
        }

        //        cell.setAttribute('style', 'background-color: #2780e3; color: #fff;');
        var tbody = table.createTBody();
        var row_count=0;
        for(var i in json_result){
           row_count=row_count+1;
           var r = tbody.insertRow(i);
           r.insertCell(0).innerHTML = "<input type='checkbox' id='CHECKBOX_"+json_result[i].id+"'>"+" "+row_count;
           r.insertCell(1).innerHTML = json_result[i].hostname;
           r.insertCell(2).innerHTML = json_result[i].description;
           r.insertCell(3).innerHTML = json_result[i].name;
           r.insertCell(4).innerHTML = json_result[i].status;
        }
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
<%= (new Date()).getTime()%>

</body>
</html>
