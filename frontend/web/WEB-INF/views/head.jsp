
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/jstl/sql.tld" prefix="sql" %>
<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl/fn.tld" prefix="fn"%>

<head>
    <title>Um.Open 1.2.1</title>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge"/>

    <script src="${pageContext.request.contextPath}/resources/jquery/js/jquery-2.1.3.min.js" type="text/javascript"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/normalize/css/normalize.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bootswatch/cosmo/css/bootstrap.min.css">
    <%--<link rel="stylesheet" href="resources/css/bootstrap-datetimepicker.css">--%>
    <script src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.js"></script>
    <script src="${pageContext.request.contextPath}/resources/moment/js/moment-with-locales.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/bootstrap-filestyle/js/bootstrap-filestyle.min.js"> </script>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/font-awesome/css/font-awesome.min.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/ico/umbrella2.png">

    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/bootstrap-select/js/bootstrap-select.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bootstrap-select/css/bootstrap-select.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.min.css">

    <!-- Custom styles for our template -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap-theme.css" media="screen" >
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/own/css/main.css">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <script src="${pageContext.request.contextPath}/resources/html5shiv/js/html5shiv.js"></script>
    <script src="${pageContext.request.contextPath}/resources/respond/js/respond.js"></script>
    <%--<script src="resources/js/json2.js" type="text/javascript"></script>--%>


</head>

<%
    String Solution;
    //allow access only if session exists
    // ФИО
    String user = (String) session.getAttribute("username");
    String DBMS=(String) session.getAttribute("DBMS");
    // имя пользователя
    String userName = null;
    String sessionID = null;
    Cookie[] cookies = request.getCookies();
    if(cookies !=null){
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("username")) userName = cookie.getValue();
            if(cookie.getName().equals("DBMS")) DBMS = cookie.getValue();
            if(cookie.getName().equals("JSESSIONID")) sessionID = cookie.getValue();
        }
    }
%>

<body>


<div class="navbar navbar-default navbar-fixed-top headroom" >
    <div class="container">
        <div class="navbar-header">
            <!-- Button for smallest screens -->
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse"><span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span> </button>
            <a class="navbar-brand" href="${pageContext.request.contextPath}/"><img src="${pageContext.request.contextPath}/resources/ico/umbrella5.png" width="35" height="30" border="0" alt="Um.Open 1.2.1"></a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav pull-right">

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">Мониторинг <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="${pageContext.request.contextPath}/Forward?URL=tasks.jsp">Просмотр выполнения заданий</a></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">Администрирование <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="${pageContext.request.contextPath}/Forward?URL=tasks_dir.jsp">Задания </a></li>
                        <li role="separator" class="divider"></li>
                        <li><a href="${pageContext.request.contextPath}/Forward?URL=users.jsp">Пользователи </a></li>
                        <li role="separator" class="divider"></li>
                        <li><a href="${pageContext.request.contextPath}/Forward?URL=settings.jsp">Свойства </a></li>
                        <li role="separator" class="divider"></li>
                        <li><a href="${pageContext.request.contextPath}/Forward?URL=examples.jsp">Примеры </a></li>
                        <li><a href="${pageContext.request.contextPath}/Forward?URL=examples2.jsp">Примеры Еще</a></li>
                        <li><a href="${pageContext.request.contextPath}/Forward?URL=examples3.jsp">Примеры На</a></li>
                        <li><a href="${pageContext.request.contextPath}/Forward?URL=examples4.jsp">Примеры По</a></li>
                        <li><a href="${pageContext.request.contextPath}/Forward?URL=upload.jsp">Загрузка </a></li>
                        <li><a href="${pageContext.request.contextPath}/Forward?URL=upload2.jsp">Посмотрим </a></li>
                        <li><a href="${pageContext.request.contextPath}/Forward?URL=audit.jsp">Аудит </a></li>
                        <li><a href="${pageContext.request.contextPath}/Forward?URL=load.jsp">Загрузка </a></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-user"></i> <b> <%=user %></b><span class="caret"></span></a>
                    <ul class="dropdown-menu" role="menu">
                        <c:set var="Solution" value="Выход"/>
                        <LI><a href="${pageContext.request.contextPath}/LogoutUser">${Solution}</a></li>

                        </li>
                    </ul>
            </ul>
        </div><!--/.nav-collapse -->
    </div>
</div>
