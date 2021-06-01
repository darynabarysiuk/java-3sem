package Servlet;

import java.io.IOException;
import java.io.PrintWriter;

import Models.ChatMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MessageListServlet extends ChatServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {
// Установить кодировку HTTP-ответа UTF-8
        response.setCharacterEncoding("utf8");
// Получить доступ к потоку вывода HTTP-ответа
        PrintWriter pw = response.getWriter();
// Записть в поток HTML-разметку страницы

        pw.println("<html><head>\n" +
                "<link rel=\"stylesheet\" href=\"bootstrap.min.css\"><meta http-equiv='Content-Type' content='text/html; charset=utf-8'/><meta http-equiv='refresh' content='10'></head>");
        pw.println("<body><div class='container'>");
// В обратном порядке записать в поток HTML-разметку для каждого сообщения
        for (int i=messages.size()-1; i>=0; i--) {
            ChatMessage aMessage = messages.get(i);
            pw.println("<div class='card my-1'>"+
                           "<div class='card-body'>"+
                               "<h5 class='card-title'>"+aMessage.getAuthor().getName()+"</h5>"+
                               "<p class='card-text'>"+aMessage.getMessage()+"</p>"+
                           "</div>"+
                       "</div>");
        }
        pw.println("</div></body>\n" +
                "<script src=\"bootstrap.min.js\"></script></html>");
    }
}
