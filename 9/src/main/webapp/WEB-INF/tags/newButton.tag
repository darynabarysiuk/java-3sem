<%@ tag pageEncoding="UTF-8" %>
<%-- Импортировать JSTL-библиотеку --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Кнопка нового объявления показывается только если пользователь
аутентифицирован (authUser!=null) --%>
<c:if test="${sessionScope.authUser!=null}">
    <div>
        <a class="btn btn-outline-primary mx-3 my-1" href="<c:url value="/updateAd.jsp"/>">Создать</a>
    </div>
</c:if>