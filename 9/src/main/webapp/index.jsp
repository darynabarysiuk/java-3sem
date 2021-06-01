<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8" %>
<%-- Импортировать JSTL-библиотеку --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Импортировать собственную библиотеку теговых файлов --%>
<%@taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%-- Импортировать собственную библиотеку тегов --%>
<%@taglib prefix="ad" uri="http://Tags/ad" %>
<html>
<head>
    <title>Главная страница</title>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF8'/>
</head>
<body class="d-flex flex-column h-100">
    <%-- Подключить заголовок страницы --%>
    <jsp:include page="/static/header.jsp"></jsp:include>
    <h1 class="mx-3">Главная страница</h1>
    <%-- Вставить разметку 2-колоночной страницы --%>
    <my:layout2Columns leftColumnWidth="68%" rightColumnWidth="28%">
        <jsp:attribute name="leftColumnBody">
            <%-- Содержание левой колонки передаѐтся как атрибут leftColumnBody --%>
            <%-- Извлечь список всех объявлений --%>
            <ad:getAds range="all" var="adListing" sort="${sessionScope.sort}" dir="${sessionScope.dir}" />
            <%-- Показать объявления без возможности редактирования --%>
            <my:adListing adListing="${adListing}" editMode="false" />
        </jsp:attribute>
        <jsp:attribute name="rightColumnBody">
            <%-- Содержание правой колонки передаѐтся как атрибут
            rightColumnBody --%>
            <%-- Вставить тег отображения сообщения об ошибке --%>
            <my:errorMessage />
            <%-- Вставить форму входа --%>
            <my:loginForm processor="/doLogin.jsp">
            </my:loginForm>

        </jsp:attribute>
    </my:layout2Columns>
    <%-- Вставить нижний заголовок страницы --%>
    <%@ include file="/static/footer.jsp" %>
</body>
</html>