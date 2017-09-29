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
        $.getJSON( 'TasksResults', {
            get_info: "IS_NAME",
            user_id: 1
        }).done(function( jsondata ) {
               updateTableResult(jsondata);               }
        })

            .fail(function(status) {
                document.getElementById("Status").innerHTML=status.responseText;
            });

    });
</script>
<script>
    function updateTableResult(json_result) {
        console.log(json_result);
        document.getElementById("nodes_table").deleteTHead();

        var table = document.getElementById("nodes_table");
        var thead = table.createTHead();
        thead.setAttribute('style', 'background-color: #2780e3; color: #fff;');
        var list_monikers = ["url", "status_code", "is_success", "message", "page_size", "javascript_errors", "name", "loading_time", "execute_started", "execute_end"];

        var list_thead = ["URL", "HTTP Status", "Успешно?", "Текст сообщения", "Размер страницы", "Ошибок javascript", "Шаблон проверки", "Загрузка выполнялась", "Начало теста", "Окончание теста"];
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
    <table id="nodes_table" class="table table-hover table-striped" border="1">
        
    </table>
</body>
</html>