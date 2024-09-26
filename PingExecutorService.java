import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.*;

public class PingExecutorService {

    public static void main(String[] args) {
        int pingCount = 10;

        // 1. Create ExecutorService
        ExecutorService servicePool = Executors.newSingleThreadExecutor();

        // Create ProcessBuilder to run the ping command with specified parameters
        ProcessBuilder pb = new ProcessBuilder("ping", "-n", Integer.toString(pingCount), "nike.com");
        System.out.println("nike.com " + pingCount + " times");
        System.out.println("Going to start process");

        try {
            // Start the process
            Process process = pb.start();

            // 2. Instantiate a Callable class to read the InputStream
            Callable<String> inputStreamReader = new PingStreamReader(process.getInputStream());

            // 3. Submit the Callable to the ExecutorService
            Future<String> fut = servicePool.submit(inputStreamReader);

            // 4. Main thread waits for the completion of the Callable task before proceeding the execution of the ping 
            fut.get();

            // 5. Shutdown the ExecutorService
            servicePool.shutdown();
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // Callable class to read InputStream and print output line by line
    static class PingStreamReader implements Callable<String> {
    	//Creates a variable to hold the input stream
        private final InputStream inputStream;
        //assigns the input stream to the parameter
        public PingStreamReader(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public String call() throws Exception {
            // Open a BufferedReader to read from the inputStream
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                // Read each line from the inputStream until there are no more lines
                while ((line = reader.readLine()) != null) {
                    // Prints each line
                    System.out.println(line);
                }
            } catch (IOException e) {
                // If an IOException occurs during reading, print the stack trace
                e.printStackTrace();
            }
            return null;
        }
    }
}








