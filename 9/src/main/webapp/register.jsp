<%@page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8" %>
<%-- Импортировать JSTL-библиотеку --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Импортировать собственную библиотеку теговых файлов --%>
<%@taglib prefix="my" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <title>Регистрация нового пользователя</title>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF8'/>
</head>
<body class="d-flex flex-column h-100">
<%-- Подключить заголовок страницы --%>
<jsp:include page="/static/header.jsp"></jsp:include>
<%-- Вставить разметку 1-колоночной страницы --%>
<my:layout1Column>
    <h1>Регистрация нового пользователя</h1>
    <%-- Вставить тег отображения сообщения об ошибке --%>
    <my:errorMessage />
    <%-- Отобразить форму регистрации (с подстановкой переданных
    данных) --%>
    <form action="/doRegister.jsp" method="post">
        <table body="0" cellspacing="0" cellpadding="10">
            <tr>
                <td>Логин:</td>
                    <%-- Начальное значение поля login равно
                    свойству login сохранѐнного в сессии JavaBean'а --%>
                <td><input class="form-control" type="text" name="login" value="${sessionScope.userData.login}"></td>
            </tr>
            <tr>
                <td>Пароль:</td>
                <td><input class="form-control" type="password" name="password" value=""></td>
            </tr>
            <tr>
                <td>Имя:</td>
                    <%-- Начальное значение поля name равно
                    свойству name сохранѐнного в сессии JavaBean'а --%>
                <td><input class="form-control" type="text" name="name" value="${sessionScope.userData.name}"></td>
            </tr>
            <tr>
                <td>Email:</td>
                    <%-- Начальное значение поля email равно
                    свойству email сохранѐнного в сессии JavaBean'а --%>
                <td><input class="form-control" type="text" name="email" value="${sessionScope.userData.email}"></td>
            </tr>
            <tr>
                <!-- reCAPTCHA -->
                <td colspan="2" align="center">
                    <div class="g-recaptcha" name="g-recaptcha-response" data-sitekey="6LcNzsMaAAAAAGRL6sVUe8tVukwurSYOe3paJt5O"></div>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input class="btn btn-outline-primary mx-1" type="submit" value="Зарегистрироваться">
                        <%-- При нажатии на кнопку
                        "Отменить" возвращаемся на страницу кабинета --%>
                    <input class="btn btn-outline-danger mx-1" type="button" value="Отменить" onclick="window.location='<c:url value="/index.jsp"/>';">
                </td>
            </tr>
        </table>
    </form>
</my:layout1Column>
<%-- Вставить нижний заголовок страницы --%>
<%@ include file="/static/footer.jsp" %>
</body>
</html>
