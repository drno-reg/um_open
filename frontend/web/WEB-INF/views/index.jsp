<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<%--<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>--%>
<%--<c:set var="req" value="${pageContext.request}" />--%>
<%--<c:set var="uri" value="${req.requestURI}" />--%>
<%--<c:set var="url">${req.requestURL}</c:set>--%>

<%--<%--%>
  <%--String path = request.getContextPath();--%>
  <%--String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";--%>
<%--%>--%>


<html>
<head>
  <%--<base href="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}${req.contextPath}/">--%>
  <%--<base href="<%=basePath%>">--%>
  <title>Um.Open 1.2.1</title>
  <meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
    <script src="${pageContext.request.contextPath}/resources/jquery/js/jquery-2.0.0.js" type="text/javascript"></script>
  <script type="text/javascript">
      //      // Первоначальное формирование документа
      $(document).ready(function() {
          // для теста
          $('input[name="username"]').val('user1');
          $('input[name="password"]').val('1234');
//                document.getElementsByTagName("password").value="1234";
                var $path_href=window.location.href;
          var _path_location=window.location.pathname;
                console.log($path_href+" :: "+_path_location);
                //  window.location.assign(window.location.href);
                <%--console.log('<%=request.getContextPath()%>');--%>
           });
//      document.addEventListener("DOMContentLoaded", function(event) {
//      });
  </script>
  <LINK REL="SHORTCUT ICON" HREF="${pageContext.request.contextPath}/resources/ico/umbrella4.png">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/normalize/css/normalize.css">
  <script src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.js"></script>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bootswatch/cosmo/css/bootstrap.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/font-awesome/css/font-awesome.min.css">
</head>
<body>
<div class="container">
  <br>
  <br>
  <br>
  <br>
  <br>
  <br>
  <h4>Um.Open 1.2.1</h4>
  <br>


  <div id="Status"/>
  <!--  <form method="PUT" action="login.jsp" enctype="multipart/form-data"> -->
  <form method="POST" action="LoginUser" name="UserLoginForm">
    <table>
      <th></th>
      <th>Выполните вход:</th>
      <tr>
        <TD rowspan="3">
          <img src="${pageContext.request.contextPath}/resources/images/umbrella4.png" alt="" border="0" align="left" />
        </TD>
        <td>Имя пользователя:</td>
        <td> <input type="text" name="username" id="username" size="20" /></td>
      </tr>
      <tr>
        <td>Пароль:</td>
        <td><input type="password" name="password" id="password" /></td>
      </tr>
      <td><input type="submit" id="Login" value="Войти" /></td>
    </table>
    <br>

  </form>


</body>
</html>

