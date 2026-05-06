package fromL2;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HTTPController {
    private static List<Violation> violations = new CopyOnWriteArrayList<>();
    private static int nextId = 1;
    private ObjectMapper mapper = new ObjectMapper();

    public String request(String filters) {
        System.out.println("For filter: " + filters);

        try {
            if (filters.startsWith("GET /obj")) {
                if (filters.contains("id=")) {
                    int id = Integer.parseInt(filters.split("id=")[1].split(" ")[0]);
                    for (Violation v : violations) {
                        if (v.getId() == id) {
                            return "[" + mapper.writeValueAsString(v) + "]";
                        }
                    }
                    return "[]";
                } else if (filters.contains("licensePlate=")) {
                    String licensePlate = filters.split("licensePlate=")[1].split(" ")[0];
                    List<Violation> result = new ArrayList<>();
                    for (Violation v : violations) {
                        if (v.getLicensePlate().equals(licensePlate)) {
                            result.add(v);
                        }
                    }
                    return mapper.writeValueAsString(result);
                } else if (filters.contains("paymentDate=null")) {
                    List<Violation> result = new ArrayList<>();
                    for (Violation v : violations) {
                        if (v.getPaymentDate() == null || v.getPaymentDate().isEmpty()) {
                            result.add(v);
                        }
                    }
                    return mapper.writeValueAsString(result);
                } else {
                    return mapper.writeValueAsString(violations);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }

        return "[]";
    }

    public String post(String jsonData) {
        System.out.println("posting: " + jsonData);
        try {
            Violation violation = mapper.readValue(jsonData, Violation.class);
            violation.setId(nextId++);
            violations.add(violation);
            return "{\"status\":\"success\",\"id\":" + violation.getId() + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"status\":\"error\"}";
        }
    }
}