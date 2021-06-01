package Tags;
import java.io.IOException;

import Models.User;
import Models.UserList;
import Models.UserListHelper;
import Models.VerifyUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class AddUser extends SimpleTagSupport {
    // Поле данных для атрибута user
    private User user;
    private static String captcha;
    // Метод-сеттер для установки атрибута (вызывается контейнером)
    public void setUser(User user) {
        this.user = user;
    }
    public static void setCapcha(java.lang.String capcha) {
        captcha = capcha;
        System.out.println(capcha);
    }
    public void doTag() throws JspException, IOException {
// Изначально описание ошибки = null (т.е. ошибки нет)
        String errorMessage = null;
// Извлечь из контекста приложения общий список пользователей
        UserList userList = (UserList) getJspContext().getAttribute("users", PageContext.APPLICATION_SCOPE);
// Проверить, что логин не пустой
        if (user.getLogin()==null || user.getLogin().equals("")) {
            errorMessage = "Логин не может быть пустым!";
        } else {
// Проверить, что имя не пустое
            if (user.getName()==null || user.getName().equals("")) {
                errorMessage = "Имя пользователя не может быть пустым!";
            }
        }
        if (errorMessage==null) {
            System.out.println("gRecaptchaResponse=" + captcha);

            if (!VerifyUtils.verify(captcha)) {
                errorMessage = "Неверная капча!";
            }
        }
// Если ошибки не было - добавить пользователя
        if (errorMessage==null) {
            try {
// Непосредственное добавление пользователя делает UserList
                userList.addUser(user);
// Записать обновлѐнный список пользователей в файл
                UserListHelper.saveUserList(userList);
            } catch (UserList.UserExistsException e) {
// Ошибка - пользователь с таким логином уже существует
                errorMessage = "Пользователь с таким логином уже существует!";
            }
        }
// Сохранить описание ошибки (текст или null) в сессии
        getJspContext().setAttribute("errorMessage", errorMessage,
                PageContext.SESSION_SCOPE);
    }
}
