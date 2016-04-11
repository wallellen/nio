import org.jibx.binding.Run;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 4/7/16.
 */
public class JiBxBindMain {
    public static void main(String[] args) {
        String[] _args = new String[]{"-b", "-l", "-v", "./src/main/java/com/wallellen/netty/section10/Address.java"};
       // Compile.main(_args);
        Run.main(new String[0]);
    }
}
