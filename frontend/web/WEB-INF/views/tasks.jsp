<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="head.jsp" %>
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
        $.getJSON( 'TasksResults', {
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
<script>
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


    function updateTableResult(json_result) {
        console.log(json_result);
        document.getElementById("nodes_table").deleteTHead();

        var table = document.getElementById("nodes_table");
        var thead = table.createTHead();
        thead.setAttribute('style', 'background-color: #2780e3; color: #fff;');
        var list_monikers = ["url", "status_code", "is_success", "message", "page_size", "name", "loading_time", "execute_started", "execute_end"];

        var list_thead = ["URL", "HTTP Status", "Успешно?", "Текст сообщения", "Размер страницы", "Шаблон проверки", "Загрузка выполнялась", "Начало теста", "Окончание теста"];
        var row = thead.insertRow(0), cell;
        for (var i = 0; i < list_thead.length; i++) {
            cell = row.insertCell(i);
            cell.innerHTML = list_thead[i];
        }

        var tbody = table.createTBody();
        for(var i in json_result){
           var r = tbody.insertRow(i);
           for(var j in list_monikers){
             r.insertCell(j).innerHTML = json_result[i][list_monikers[j]];    
           }
        }
    };
</script>
<body>
    <table id="nodes_table" class="table table-hover table-striped" border="1" style="margin-top: 100px;">
        
    </table>
</body>
</html>