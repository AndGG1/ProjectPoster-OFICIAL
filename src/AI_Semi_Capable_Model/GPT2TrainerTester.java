package AI_Semi_Capable_Model;

import com.google.gson.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;


public class GPT2TrainerTester {
    
    private static final String API_URL = "https://api-inference.huggingface.co/models/openai-community/gpt2"; // Your model name here
    
    public static void main(String[] args) {
        String trainingData = "";
        
        try {
            fineTuneModel(trainingData, "");
        } catch (IOException e) {
            System.out.println(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }
    }
    
    public static void fineTuneModel(String trainingData, String API_KEY) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(API_URL);
        httpPost.setHeader("Authorization", "Bearer " + API_KEY);
        httpPost.setHeader("Content-Type", "application/json");
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("inputs", trainingData);
        StringEntity stringEntity = new StringEntity(jsonObject.toString());
        httpPost.setEntity(stringEntity);
        
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        String response = EntityUtils.toString(httpResponse.getEntity());
        System.out.println("Training Response Body: " + response);
        
        
        JsonObject jsonObject1 = JsonParser.parseString(response).getAsJsonObject();
        if (jsonObject1.has("error")) {
            String error = jsonObject1.get("error").getAsString();
            System.out.println("Training Error: " + error);
        }
        httpResponse.close();
        httpClient.close();
    }
    
    public static String contentData(String word) throws IOException {
        BufferedReader bf = null;
        URL url = new URL("https://en.wikipedia.org/wiki/" + word);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        if (connection.getResponseCode() != 200) return "Failed!";
        
        connection.connect();
        bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        
        return bf.lines().collect(Collectors.joining());
    }
    
    public static void learnTheAi(List<String> linesToLearn, Executor exec, String api) {
        //learns from the given fetched data
        linesToLearn.forEach(line -> {
            exec.execute(() -> {
                try {
                    GPT2TrainerTester.fineTuneModel(line, api);
                } catch (IOException e) {
                    //do nothing
                }
            });
        });
    }
    
    public static boolean chooseFreeAi(String apiKey) throws IOException {
        URL url = new URL("https://api-inference.huggingface.co/models/openai-community/gpt2");
        
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + apiKey);
        
        int respCode = httpURLConnection.getResponseCode();
        if (respCode == 200) return true;
        return false;
    }
}
