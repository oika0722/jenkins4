import java.util.HashMap;
import java.util.Stack;

public interface UnaryOperation extends Operation{
    @Override
    default void perform(Stack<String> operands, HashMap<String, Double> variables) throws OperationException {
        if(operands.empty()) throw new OperationException("There is no operand");
        String operand_str = operands.pop();
        Double operand;
        if(variables.containsKey(operand_str)){
            operand = variables.get(operand_str);
        }else{
            operand = Double.parseDouble(operand_str);
        }
        double result = perform(operand);
        if(Double.isNaN(result)){
            throw new OperationException("Функция вернула NaN");
        }
        operands.push(String.valueOf(result));
    }

    double perform(double operand1);
}
