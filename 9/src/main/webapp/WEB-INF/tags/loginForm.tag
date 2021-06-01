<%@tag pageEncoding="UTF-8" %>
<%-- Импортировать собственную библиотеку теговых файлов --%>
<%@taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%-- Импортировать JSTL-библиотеку --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Входным атрибутом тега является processor - адрес страницы-обработчика
запроса на аутентификацию --%>
<%@ attribute name="processor" required="false" rtexprvalue="true" %>
<%-- Форма входа показывается только если в настоящий момент пользователь не
аутентифицирован --%>
<c:if test="${sessionScope.authUser==null}">
    <form action="${processor}" method="post">
        <table border="0" cellspacing="0" cellpadding="5">
            <tr>
                <td>Логин:</td>
                <td><input class="form-control" type="text" name="login" value=""></td>
            </tr>
            <tr>
                <td>Пароль:</td>
                <td><input class="form-control" type="password" name="password" value=""></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td><input class="btn btn-outline-primary mx-1" type="submit" value="Войти">
                        <%-- Вставить ссылку регистрации --%>
                    <my:registerButton>
                        <jsp:attribute name="processor">
                        <%-- Адрес страницы с формой регистрации
                        передаѐтся как и
                        для страницы-обработчика формы регистрации
                        --%>
                            <c:url value="/register.jsp" />
                        </jsp:attribute>
                    </my:registerButton>
                </td>
            </tr>
        </table>
    </form>
</c:if>