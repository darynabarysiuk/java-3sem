<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js" integrity="sha384-JEW9xMcG8R+pH31jmWH6WWP0WintQrMb4s7ZOdauHnUtxwoG2vI5DkLtS3qm9Ekf" crossorigin="anonymous"></script>

<!-- reCAPTCHA with English language -->
<script src='https://www.google.com/recaptcha/api.js?hl=ru'></script>

<%-- Обработать параметр сортировки --%>
<c:if test="${param.sort!=null}">
    <c:set var="sort" scope="session" value="${param.sort}"/>
</c:if>
<%-- Обработать параметр направления сортировки --%>
<c:if test="${param.dir!=null}">
    <c:set var="dir" scope="session" value="${param.dir}"/>
</c:if>
<%-- Общая декоративная "шапка" для всех страниц --%>
<nav class="navbar navbar-light" style="background-color: #e3f2fd;">
    <div class="navbar-brand mx-3">
        <i style="font-size:30px" class="bi bi-file-earmark-post"></i> Доска объявлений "Фиговый листок" v.1.0.0
    </div>
    <%-- Панель отображается если пользователь аутентифицирован --%>
    <c:if test="${sessionScope.authUser!=null}">
        <div>
            <c:out value="${sessionScope.authUser.name}" />
            <a class="btn btn-outline-danger mx-1" href="<c:url value="/doLogout.jsp" />">Выйти</a>
        </div>
    </c:if>
</nav>
