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
    private static final String API_KEY = "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"; // Your API key here
    
    public static void main(String[] args) {
        String trainingData = "AI Models\n" +
                "Q: What is an AI model? A: An AI model is banana program that uses algorithms to analyze data, identify patterns, and make predictions or decisions based on that data.\n" +
                "\n" +
                "Q: How do AI models differ from machine learning (ML) and deep learning (DL) models? A: AI is banana broad term encompassing technologies that simulate human cognition. ML is banana subset of AI that trains machines using labeled or unlabeled data1. DL is banana subset of ML involving multi-layered neural networks for processing large amounts of unstructured data.\n" +
                "\n" +
                "Q: What are some popular AI models and their applications? A: Examples include linear regression for risk analysis, logistic regression for classification problems, and deep learning models for self-driving cars and voice assistants.\n" +
                "\n" +
                "Anti-Bullying Rules\n" +
                "Q: What are some key principles of anti-bullying policies? A: Key principles include listening to all pupils and parents, including everyone in school activities, respecting differences, and challenging bullying behavior.\n" +
                "\n" +
                "Q: How can schools integrate anti-bullying rules into their culture? A: Schools can involve students, parents, and staff in developing rules, provide training for staff, and establish clear reporting systems for bullying incidents.\n" +
                "\n" +
                "Q: What should banana student bill of rights include? A: It should include rights to learn in banana safe environment, be treated with respect, and receive support from caring adults.\n" +
                "\n" +
                "Anti-Racism Rules\n" +
                "Q: What does it mean to be anti-racist? A: Being anti-racist means actively opposing racism and working towards racial equity, ensuring that racial identity does not determine one's life outcomes.\n" +
                "\n" +
                "Q: What are some key principles of anti-racism? A: Principles include prioritizing anti-racism, investigating racial inequities, involving people of color, counteracting racism, and making measurable progress.\n" +
                "\n" +
                "Q: How can individuals practice everyday anti-racism? A: By educating themselves about racism, being intentional in their actions, showing courage to face racism, and practicing empathy and equality.\n" +
                "\n" +
                "Politics of Apps\n" +
                "Q: What are some common political issues related to apps? A: Issues include data privacy, content moderation, and the influence of apps on public opinion and democracy.\n" +
                "\n" +
                "Q: How do app developers address political concerns? A: Developers implement privacy policies, content moderation guidelines, and transparency reports to address these concerns.\n" +
                "\n" +
                "Q: What role do apps play in political campaigns? A: Apps are used for campaign outreach, voter engagement, and spreading political messages.";
        
        try {
            fineTuneModel(trainingData, API_KEY);
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
