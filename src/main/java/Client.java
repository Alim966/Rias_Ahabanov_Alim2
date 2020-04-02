// Port 4488
// IP 127.0.0.1
import org.apache.log4j.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    public static int PORT;
    public static String HOST;

    public static Socket socket;
    public static BufferedReader r;
    public static PrintWriter w;
    public static BufferedReader con;

    //Логгер
    private static Logger log = Logger.getLogger(Client.class);

    private static String logLevelGlobal;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String command = "";
        command = sc.nextLine();

        setConnection(command);
        try {
            socket = new Socket(HOST, PORT);
            r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            w = new PrintWriter(socket.getOutputStream(), true);
            con = new BufferedReader(new InputStreamReader(System.in));
            String line;
            do {
                line = r.readLine();
                if (line != null)
                    if(line.contains("connect")){
                        socket = new Socket(HOST, PORT);
                        log.info("Подключение к серверу : <" + HOST + "> :: <" + PORT + ">");
                        setConnection(line);
                    }
                if(line.contains("send")) {
                    log.info(line);
                    sendMessage(line);

                } else
                if(line.equals("help")){
                    Help();
                } else
                if(line.equals("disconnect")){
                    log.info("Отключение :: command > disconnect");
                    //setDisconnect();
                    socket.close();
                }
                if(line.equals("quit")){
                    log.info ("Выход :: command > quit");
                    System.exit(0);
                    break;
                }
                if(line.equals("logLevel")){
                    setLogLevel();
                }
                line = con.readLine();
                w.println(line);
            }
            while (true);
        }
        catch (Exception err) {
            System.err.println(err);
        }
    }

    public static void defineLog(String logLevelGlobal){
        if (logLevelGlobal.equals("fatal")) {
            log.fatal("Log Level :: fatal");
        }
        else
        if (logLevelGlobal.equals("trace")){
                log.trace("Log Level :: trace");
        }
        else
        if (logLevelGlobal.equals("error")) {
            log.error("Log Level :: error");
        }
        else
        if (logLevelGlobal.equals("info")) {
            log.info("Log Level :: info");
        }
        else
        if (logLevelGlobal.equals("debug")) {
            log.debug("Log Level :: debug");
        }
        else
        if (logLevelGlobal.equals("warn")) {
            log.warn("Log Level :: warn");
        }
    }

    public static void setLogLevel(){
        Scanner sc = new Scanner(System.in);
        System.out.println("What level of logging do you want to choose ? Enter...");
        String logLevel = sc.nextLine();
       if (logLevel.equals("off")){
                log.setLevel(Level.OFF);
                logLevelGlobal = "off";
       }
       else
           if (logLevel.equals("error")) {
           log.setLevel(Level.ERROR);
           logLevelGlobal = "error";
       }
       else
           if (logLevel.equals("info")) {
           log.setLevel(Level.INFO);
           logLevelGlobal = "info";
       }
       else
           if (logLevel.equals("debug")) {
           log.setLevel(Level.DEBUG);
           logLevelGlobal = "debug";
       }
       else
           if (logLevel.equals("all")) {
           log.setLevel(Level.ALL);
           logLevelGlobal = "all";
       }
       else
           if (logLevel.equals("fatal")) {
           log.setLevel(Level.FATAL);
           logLevelGlobal = "fatal";
       }
       else
           if (logLevel.equals("trace")) {
           log.setLevel(Level.TRACE);
           logLevelGlobal = "trace";
       }
       else
           if (logLevel.equals("warn")) {
               log.setLevel(Level.WARN);
               logLevelGlobal = "warn";
           }

        defineLog(logLevelGlobal);
    }
    //
    public static void Help(){
        System.out.println("================================================");
        System.out.println("*   connect    ::> connect <IP Адрес > <Порт>  *");
        System.out.println("*   help       ::> help                        *");
        System.out.println("*   send       ::> send <Ваще сообщение>       *");
        System.out.println("*   disconnect ::> disconnect                  *");
        System.out.println("*   quit       ::> quit                        *");
        System.out.println("================================================");
    }

    // Функция для отправки сообщений
    public static void sendMessage(String str) {
        List<String> allMessage = new ArrayList<String>();
        for (String s : str.split(" ")) {
            allMessage.add(s);
        }

        String s = "";
        for (String s1 : allMessage.subList(1, allMessage.size())) {// не берем слово send, и отпр. след.слова
            s += s1 + " ";
        }

        if (!socket.isClosed()) {
            System.out.println(s);// выводим сообщение
            log.info("Сообщение от сервера :: > " + s);
        } else {
            System.out.println("Вы не можите подключится к Серверу!");
            Help();
        }
    }

    // устанавливает соединение с сервером
    public static void setConnection(String str) {
        List<String> dataConnection = new ArrayList<String>();
        for (String retval : str.split(" ")) {
            if (str.contains("connect")) {
                dataConnection.add(retval);
            } else {
                System.out.println("Oops...");
                Help();
            }
        }
        int i = 0;
        for (String s : dataConnection) {
            if (i == 1) {
                HOST = dataConnection.get(1);
            }
            if (i == 2) {
                PORT = Integer.valueOf(dataConnection.get(2));
            }
            i++;
        }
    }

    public static void setDisconnect(){

        System.out.println("Отключение клиента от сервера!\n");

        System.out.println("If you want restart server please press to : 'Y' or 'N'");
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        if(s.equals("Y")){
            main(new String[]{});
        }
        else System.exit(1);
    }
}


