package fromL2;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Serverl3 {
    public void service(Socket socket) throws IOException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        HTTPController httpController = new HTTPController();
        try {
            socket.setSoTimeout(5 * 60 * 1000);
            out.write("Ready.\r\nSupported operations: get, getById, post, save, load, getByLicensePlate, getUnpaid.\r\n");
            out.write("IP: " + socket.getInetAddress().getHostAddress() + "\r\n");
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            ObjectMapper mapper = new ObjectMapper();
            while (!"exit".equals(line = in.readLine())) {
                switch (line){
                    case "get"-> {
                        out.write(httpController.request("GET /obj").replace("},{", "},\r\n{")+"\r\n");
                        out.flush();
                    }
                    case "getById" ->{
                        out.write("Введите id: ");out.flush();
                        int id = Integer.parseInt(in.readLine());
                        out.write(httpController.request("GET /obj?id="+id).replace("},{", "},\r\n{")+"\r\n");
                        out.flush();
                    }
                    case "getByLicensePlate" ->{
                        out.write("Введите номер авто: ");out.flush();
                        String licensePlate = in.readLine();
                        out.write(httpController.request("GET /obj?licensePlate="+licensePlate).replace("},{", "},\r\n{")+"\r\n");
                        out.flush();
                    }
                    case "getUnpaid" ->{
                        out.write(httpController.request("GET /obj?paymentDate=null").replace("},{", "},\r\n{")+"\r\n");
                        out.flush();
                    }
                    case "post" ->{
                        try {
                            out.write("Номер авто: ");
                            out.flush();
                            String licensePlate = in.readLine();

                            out.write("Фамилия водителя: ");
                            out.flush();
                            String lastName = in.readLine();

                            out.write("Имя водителя: ");
                            out.flush();
                            String firstName = in.readLine();

                            out.write("Отчество водителя: ");
                            out.flush();
                            String middleName = in.readLine();

                            out.write("Состав правонарушения: ");
                            out.flush();
                            String violationType = in.readLine();

                            out.write("Дата правонарушения (гггг-мм-дд): ");
                            out.flush();
                            String violationDate = in.readLine();

                            out.write("Сумма штрафа: ");
                            out.flush();
                            double fineAmount = Double.parseDouble(in.readLine());

                            out.write("Дата оплаты штрафа (гггг-мм-дд или 'не оплачено'): ");
                            out.flush();
                            String paymentDate = in.readLine();
                            if (paymentDate.equalsIgnoreCase("не оплачено")) {
                                paymentDate = null;
                            }

                            Violation violation = new Violation(licensePlate, lastName, firstName, middleName,
                                    violationType, violationDate, fineAmount, paymentDate);

                            httpController.post(mapper.writeValueAsString(violation));
                            out.write("Правонарушение добавлено успешно!\r\n");
                            out.flush();
                        } catch (NumberFormatException e) {
                            out.write("Ошибка формата ввода! Проверьте числовые поля.\r\n");
                            out.flush();
                        }
                    }
                    case "save" -> {
                        ArrayList<Violation> violations = mapper.readValue(httpController.request("GET /obj"),
                                mapper.getTypeFactory().constructCollectionType(ArrayList.class, Violation.class));
                        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("violations.ser"))){
                            oos.writeObject(violations);
                            out.write("Данные сохранены в файл violations.ser\r\n");
                            out.flush();
                        } catch (IOException e) {
                            out.write("Не удалось сохранить в файл\r\n");
                            out.flush();
                        }
                    }
                    case "load" -> {
                        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("violations.ser"))){
                            ArrayList<Violation> violations = (ArrayList<Violation>) ois.readObject();
                            for (Violation violation : violations) {
                                httpController.post(mapper.writeValueAsString(violation));
                            }
                            out.write("Загружено " + violations.size() + " правонарушений\r\n");
                            out.flush();
                        } catch (IOException | ClassNotFoundException e) {
                            out.write("Не удалось загрузить из файла\r\n");
                            out.flush();
                        }
                    }
                    case "stats" -> {
                        ArrayList<Violation> violations = mapper.readValue(httpController.request("GET /obj"),
                                mapper.getTypeFactory().constructCollectionType(ArrayList.class, Violation.class));

                        double totalFines = 0;
                        double unpaidFines = 0;
                        int unpaidCount = 0;

                        for (Violation v : violations) {
                            totalFines += v.getFineAmount();
                            if (v.getPaymentDate() == null || v.getPaymentDate().isEmpty()) {
                                unpaidFines += v.getFineAmount();
                                unpaidCount++;
                            }
                        }

                        out.write("=== СТАТИСТИКА ПРАВОНАРУШЕНИЙ ===\r\n");
                        out.write("Всего правонарушений: " + violations.size() + "\r\n");
                        out.write("Общая сумма штрафов: " + totalFines + " руб.\r\n");
                        out.write("Неоплаченных штрафов: " + unpaidCount + "\r\n");
                        out.write("Сумма неоплаченных штрафов: " + unpaidFines + " руб.\r\n");
                        out.flush();
                    }
                    default -> {
                        out.write("Такой команды нет! Доступные команды: get, getById, getByLicensePlate, getUnpaid, post, save, load, stats\r\n");
                        out.flush();
                    }
                }
                out.write("/-\\_".repeat(10)+"\r\n");
                out.flush();
            }
        } catch (Exception e) {
            out.write("Возникла ошибка на стороне сервера: " + e.getMessage() + "\r\n");
            out.flush();
        } finally {
            socket.close();
        }
    }

    public void start() {
        try(ServerSocket serverSocket = new ServerSocket(4567)) {
            serverSocket.setSoTimeout(15*60*1000);
            System.out.println("Violations server listening on port 4567");
            while(true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(() -> {
                        try {
                            service(clientSocket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}