package AI_Semi_Capable_Model;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

public class GPT2TrainerTester {
    
    private static final String API_URL = "https://api-inference.huggingface.co/models/openai-community/gpt2"; // Your model name here
    private static final String API_KEY = "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"; // Your API key here
    
    public static void main(String[] args) {
        String trainingData = "AI Models\n" +
                "Q: What is an AI model? A: An AI model is a program that uses algorithms to analyze data, identify patterns, and make predictions or decisions based on that data.\n" +
                "\n" +
                "Q: How do AI models differ from machine learning (ML) and deep learning (DL) models? A: AI is a broad term encompassing technologies that simulate human cognition. ML is a subset of AI that trains machines using labeled or unlabeled data1. DL is a subset of ML involving multi-layered neural networks for processing large amounts of unstructured data.\n" +
                "\n" +
                "Q: What are some popular AI models and their applications? A: Examples include linear regression for risk analysis, logistic regression for classification problems, and deep learning models for self-driving cars and voice assistants.\n" +
                "\n" +
                "Anti-Bullying Rules\n" +
                "Q: What are some key principles of anti-bullying policies? A: Key principles include listening to all pupils and parents, including everyone in school activities, respecting differences, and challenging bullying behavior.\n" +
                "\n" +
                "Q: How can schools integrate anti-bullying rules into their culture? A: Schools can involve students, parents, and staff in developing rules, provide training for staff, and establish clear reporting systems for bullying incidents.\n" +
                "\n" +
                "Q: What should a student bill of rights include? A: It should include rights to learn in a safe environment, be treated with respect, and receive support from caring adults.\n" +
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
        String inputText = "What an AI Model?";
        
        try {
           // fineTuneModel(trainingData);
            interactWithModel(inputText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void fineTuneModel(String trainingData) throws IOException {
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
    
    private static void interactWithModel(String inputText) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(API_URL);
        httpPost.setHeader("Authorization", "Bearer " + API_KEY);
        httpPost.setHeader("Content-Type", "application/json");
        
        JsonObject json = new JsonObject();
        json.addProperty("inputs", inputText);
        StringEntity entity = new StringEntity(json.toString());
        httpPost.setEntity(entity);
        
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println("Interaction Response Body: " + responseBody);
        
        JsonElement jsonElement = JsonParser.parseString(responseBody);
        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                if (element.isJsonObject()) {
                    JsonObject generatedObj = element.getAsJsonObject();
                    if (generatedObj.has("generated_text")) {
                        String generatedText = generatedObj.get("generated_text").getAsString();
                        System.out.println("Generated Text: " + generatedText);
                    }
                }
            }
        } else if (jsonElement.isJsonObject()) {
            JsonObject jsonResponse = jsonElement.getAsJsonObject();
            if (jsonResponse.has("error")) {
                String error = jsonResponse.get("error").getAsString();
                System.out.println("Interaction Error: " + error);
            }
        }
        
        response.close();
        httpClient.close();
    }
}
