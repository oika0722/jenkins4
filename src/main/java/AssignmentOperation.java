import java.util.HashMap;
import java.util.Stack;

public class AssignmentOperation implements Operation{
    @Override
    public void perform(Stack<String> operands, HashMap<String, Double> variables) throws OperationException {
        if(operands.empty()) throw new OperationException("There is no operand");
        String operand_str = operands.pop();
        String var = operand_str.split("=")[0];
        String d_str = operand_str.split("=")[1];
        Double d;
        if(variables.containsKey(d_str)){
            d = variables.get(operand_str);
        }else{
            d = Double.parseDouble(d_str);
        }
        variables.put(var, d);
    }
}
