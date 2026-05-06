import java.util.HashMap;
import java.util.Stack;

public interface Operation {
    void perform(Stack<String> operands, HashMap<String, Double> variables) throws OperationException;
}