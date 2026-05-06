import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        new Thread(() -> new Serverl2().start()).start();
        new Thread(() -> new fromL2.Serverl3().start()).start();

    }
}
//var a=1|var b=2|var c=3|var d=4|a|b|c|+|d|3|-|/|*
//var a=0.5|var b=4|var c=1|var d=2|var n=3|a|cos|b|sqrt|c|arctg|+|d|n|^|3|-|/|*