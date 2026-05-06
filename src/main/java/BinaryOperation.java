import java.util.HashMap;
import java.util.Stack;

public interface BinaryOperation extends Operation {
    @Override
    default void perform(Stack<String> operands, HashMap<String, Double> variables) throws OperationException {
        if(operands.empty()) throw new OperationException("There is no operand");
        String operand2_str = operands.pop();
        Double operand2;
        if(variables.containsKey(operand2_str)){
            operand2 = variables.get(operand2_str);
        }else{
            operand2 = Double.parseDouble(operand2_str);
        }
        if(operands.empty()) throw new OperationException("There is no operand");
        String operand1_str = operands.pop();
        Double operand1;
        if(variables.containsKey(operand1_str)){
            operand1 = variables.get(operand1_str);
        }else{
            operand1 = Double.parseDouble(operand1_str);
        }
        double result = perform(operand1, operand2);
        operands.push(String.valueOf(result));
    }

    double perform(double operand1, double operand2);
}