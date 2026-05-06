import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Serverl2 {
    private final Map<String, Operation> operations = Map.ofEntries(
            Map.entry("+", (BinaryOperation) BinaryArithmeticOperation::sum),
            Map.entry("-", (BinaryOperation) BinaryArithmeticOperation::sub),
            Map.entry("*", (BinaryOperation) BinaryArithmeticOperation::mul),
            Map.entry("/", (BinaryOperation) BinaryArithmeticOperation::div),
            Map.entry("%", (BinaryOperation) BinaryArithmeticOperation::mod),
            Map.entry("sin", (UnaryOperation) UnaryArithmeticOperation::sin),
            Map.entry("cos", (UnaryOperation) UnaryArithmeticOperation::cos),
            Map.entry("^", (BinaryOperation) BinaryArithmeticOperation::pow),
            Map.entry("sqrt", (UnaryOperation) UnaryArithmeticOperation::sqrt),
            Map.entry("exp", (UnaryOperation) UnaryArithmeticOperation::exp),
            Map.entry("ln", (UnaryOperation) UnaryArithmeticOperation::ln),
            Map.entry("arctg", (UnaryOperation) UnaryArithmeticOperation::arctg)
    );
    public void service(Socket socket) throws IOException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        try {
            socket.setSoTimeout(5 * 60 * 1000);
            HashMap<String, Double> variables = new HashMap<>();
            Stack<String> operands = new Stack<>();
            out.write(String.format("Ready.\r\nSupported operations: %s.\r\nCurrent operands: %s.\r\n", operations.keySet(), operands));
            out.write("IP: " + socket.getInetAddress().getHostAddress() + "\r\n");
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String lineE;
            try {
                while (!"exit".equals(lineE = in.readLine())) {
                    String[] lines = lineE.split("\\|");
                    for (String line:lines) {
                        Operation operation;
                        if (line.contains("var")) {
                            line = line.substring(4);
                            operands.push(line);
                            operation = new AssignmentOperation();
                        } else {
                            operation = operations.get(line);
                        }
                        if (operation != null) {
                            operation.perform(operands, variables);
                        } else {
                            try {
                                operands.push(line);
                            } catch (NumberFormatException e) {
                                throw new OperationException("Invalid operand " + line);
                            }
                        }
                        out.write(String.format("Current operands: %s.\r\n", operands));
                        out.write("Current variables: " + mapToString(variables) + "\r\n");
                        out.flush();
                    }
                }
            } catch (OperationException e) {
                out.write(String.format("Error. %s.\r\n", e.getMessage()));
                out.flush();
            }
        }catch (Exception e){
            out.write("Возникла ошибка на стороне сервера");
            out.flush();
        }finally {
            socket.close();
        }
    }
    public String mapToString(HashMap<String, Double> variables){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (String key: variables.keySet()){
            sb.append("{").append(key).append(":").append(variables.get(key)).append("}");
        }
        sb.append("]");
        return sb.toString();
    }
    public void start(){
        try(ServerSocket serverSocket = new ServerSocket(4568)) {
            serverSocket.setSoTimeout(15*60*1000);
            System.out.println("Calc listening on port 4568");
            while(true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    service(clientSocket);
                }catch (IOException exception){
                    exception.printStackTrace();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}