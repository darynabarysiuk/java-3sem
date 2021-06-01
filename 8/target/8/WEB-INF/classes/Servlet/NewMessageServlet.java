package Servlet;

import java.io.IOException;
import java.util.Calendar;

import Models.ChatMessage;
import Models.ChatUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Timer;
import java.util.TimerTask;

public class NewMessageServlet extends ChatServlet {
    private static final long serialVersionUID = 1L;
    private long timerSeconds = 30L;
    private JokesGetting jokesGetting;
    private Timer timer = new Timer("Timer");

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            timerSeconds = Integer.parseInt(getInitParameter("timerSeconds"));
        }
        catch(NumberFormatException ex)
        {
            timerSeconds = 30L;
        }

        ChatUser author = activeUsers.get((String)request.getSession().getAttribute("name"));
        jokesGetting = new JokesGetting(author);
        timer.schedule(jokesGetting, timerSeconds * 100);
    }

    private void sendMessage(ChatUser author, String message){
        synchronized (messages) {
// Добавить в список сообщений новое
            messages.add(new ChatMessage(message, author,
                    Calendar.getInstance().getTimeInMillis()));
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            if(timer != null) {
                timer.cancel();
                timer.schedule(jokesGetting, timerSeconds * 100);
            }
        }catch (Exception exception){timer = null;}
// По умолчанию используется кодировка ISO-8859. Так как мы
// передаѐм данные в кодировке UTF-8
// то необходимо установить соответствующую кодировку HTTP-запроса
        request.setCharacterEncoding("UTF-8");
// Извлечь из HTTP-запроса параметр 'message'
        String message = (String)request.getParameter("message");
// Если сообщение не пустое, то
        if (message!=null && !"".equals(message)) {
// По имени из сессии получить ссылку на объект ChatUser
            ChatUser author = activeUsers.get((String)request.getSession().getAttribute("name"));
            sendMessage(author, message);
        }
// Перенаправить пользователя на страницу с формой сообщения
        response.sendRedirect("/compose_message.htm");
    }

    public class JokesGetting extends TimerTask {

        private ChatUser author;

        public JokesGetting(ChatUser author){
            this.author = author;
        }

        @Override
        public void run() {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://icanhazdadjoke.com/"))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = null;
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException e) {
                return;
            } catch (InterruptedException e) {
                return;
            }
            //замена парсинга {"id":"dvddj","joke":"some joke",...}
            var joke = response.body().split(",")[1].split("\"")[3];
            System.out.println(joke);
            sendMessage(author, joke);
        }
    }
}