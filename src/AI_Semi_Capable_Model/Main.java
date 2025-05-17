package AI_Semi_Capable_Model;

import DTOS.UserInterfaces.Activity.Search_Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    // Dictionary for spell checking (currently commented out for later implementation)
    private static SpellDictionary dictionary;
    
    // Variable to store generated responses
    private static String result;
    
    // Default message if no relevant subject is found
    private static final String SUBJECT_NOT_FOUND = "Subject not found!";
    
    static {
        // Initialize result (dictionary initialization is commented out)
        result = "";
    }
    
    // Placeholder strings for different categories (empty for now)
    private static final String recoverAccount = "";
    private static final String security = "";
    private static final String functionality = "";
    private static final String AIModel = "";
    private static final String interactions = "";
    private static final String rules = "";
    private static final String updates = "";
    
    // Memory map to store previously learned topics and responses
    private static final Map<String, String> memory = new HashMap<>();
    
    // Thread pool for concurrent task execution
    private static Executor exec = Executors.newCachedThreadPool();
    
    // List of new lines to learn from user interactions
    private static List<String> linesToLearn = new ArrayList<>();
    
    // Main method for processing user input and generating a response
    public static String call(String content) {
        // Load predefined data into memory
        loadData(memory);
        
        String[] strongSubject = new String[1];
        strongSubject[0] = SUBJECT_NOT_FOUND;
        AtomicLong count = new AtomicLong(1);
        
        // Search stored memory for relevant content
        memory.forEach((k, chunk) -> {
            Map.Entry<String, Long> result = useContent(content, false, chunk);
            if (!result.getKey().equals(SUBJECT_NOT_FOUND) && count.get() < result.getValue()) {
                strongSubject[0] = k;
                count.set(result.getValue());
            }
        });
        
        // If no relevant subject found, attempt to generate a new response
        if (strongSubject[0].equals(SUBJECT_NOT_FOUND)) {
            String res = useContent(content, true, "").getKey();
            
            // Generate AI response
            String responseContent = GenerativeAI.generateAnswer(content);
            
            // Store the new response in memory
            memory.put(memory.size() + "", content + "\n" + res + "\n\n" + responseContent);
            
            return res + "\n\n" + responseContent;
        } else {
            // Retrieve previous conversation if relevant data exists
            return "From previous conversation: " + memory.get(strongSubject[0]);
        }
    }
    
    // Method for extracting key content from user input
    private static List<Map.Entry<String, Integer>> fetchContent(String content) {
        content = content.toLowerCase();
        HashMap<String, Integer> map = new HashMap<>();
        
        // Process each word in the input content
        Arrays.stream(content.split(" "))
                .forEach(s -> {
                    // Convert plural words to singular
                    s = s.replaceAll("ies$", "y")
                            .replaceAll("es$", "")
                            .replaceAll("s$", "");
                    
                    // Remove punctuation marks
                    s = s.replaceAll("\\p{Punct}", "");
                    
                    // Store word frequency in map
                    map.put(s, map.getOrDefault(s, 0) + 1);
                });
        
        // Return the 10 most frequent words sorted in descending order
        return map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .toList();
    }
    
    // Method for checking relevant content against stored or web data
    private static int checkContent(String word, String content, boolean isWeb, String chunk) {
        AtomicInteger count = new AtomicInteger();
        
        try {
            BufferedReader bf = null;
            if (isWeb) {
                // Fetch Wikipedia page for the given word
                URL url = new URL("https://en.wikipedia.org/wiki/" + word);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                bf = new BufferedReader(new StringReader(chunk));
            }
            
            // Scan through the text and compare words for similarity
            bf.lines().forEach(line -> {
                AtomicBoolean isValidContent = new AtomicBoolean(false);
                Arrays.stream(content.split(" ")).forEach(s -> {
                    for (String wordToMatch : line.split(" ")) {
                        if ((line.contains(s) || Search_Feature.similarity(s, wordToMatch) >= 50) && s.length() >= 4) {
                            count.getAndIncrement();
                            isValidContent.set(true);
                        }
                    }
                });
                // Add relevant lines to learning data
                if (isValidContent.get() && isWeb) {
                    linesToLearn.add(line);
                }
            });
            
        } catch (IOException e) {
            // Exception handling (no action taken)
        }
        
        return count.get();
    }
    
    // Determine subject relevance and retrieve data accordingly
    private static Map.Entry<String, Long> useContent(String content, boolean isWeb, String chunk) {
        List<Map.Entry<String, Integer>> res = fetchContent(content);
        final String[] subject = new String[1];
        subject[0] = SUBJECT_NOT_FOUND;
        AtomicLong constant = new AtomicLong();
        long words = isWeb ? Arrays.stream(content.split(" ")).count() * 5 : 1;
        
        res.forEach(entry -> {
            int i = checkContent(entry.getKey(), content, isWeb, chunk);
            if (i >= words) {
                if (subject[0].equals(SUBJECT_NOT_FOUND) || i > constant.get()) {
                    subject[0] = entry.getKey();
                    constant.set(i);
                }
            }
        });
        
        // If checking the web, return a Wikipedia link if relevant content was found
        if (isWeb && !subject[0].equals(SUBJECT_NOT_FOUND)) {
            return new AbstractMap.SimpleEntry<>("Here's what I found on the web: https://en.wikipedia.org/wiki/" + subject[0], 0L);
        }
        
        return new AbstractMap.SimpleEntry<>(subject[0], constant.get());
    }
    
    // Load predefined categories into memory
    private static void loadData(Map<String, String> memory) {
        memory.put("recoverAccount", recoverAccount);
        memory.put("security", security);
        memory.put("functionality", functionality);
        memory.put("AiModel", AIModel);
        memory.put("interactions", interactions);
        memory.put("rules", rules);
        memory.put("updates", updates);
    }
    
    // Spell checking function (not currently in use)
    public static boolean isWordInDictionary(String word) throws IOException {
        SpellChecker spellChecker = new SpellChecker(dictionary);
        StringWordTokenizer tokenizer = new StringWordTokenizer(word);
        spellChecker.checkSpelling(tokenizer);
        return spellChecker.getSuggestions(word, 1).isEmpty();
    }
    
    // Class for generating AI responses
    public class GenerativeAI {
        private static String OPENAI_API_KEY;
        private static final String OPENAI_ENDPOINT = "https://api-inference.huggingface.co/models/openai-community/gpt2";
        private static final List<String> apis;
        
        static {
            Properties props = new Properties();
            try {
                props.load(Files.newInputStream(Path.of("storefront.properties")));
                OPENAI_API_KEY = props.getProperty("KEY");
                apis = Files.readAllLines(Path.of("API_KEYS"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        private static String generateAnswer(String prompt) {
            // Send request to AI model and retrieve response
            try {
                boolean canGenerateAnswer = false;
                for (String api : apis) {
                    if (GPT2TrainerTester.chooseFreeAi(api)) {
                        canGenerateAnswer = true;
                        OPENAI_API_KEY = api;
                        break;
                    }
                }
                
                if (!canGenerateAnswer) return "I am sorry, but all our Discussion Servers are currently unavailable, " +
                        "I suggest asking me again in about 30 seconds or so, a server might get free.";
                
                Map<String, Object> requestBody = new HashMap<>();
                requestBody.put("inputs", prompt);
                
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonRequestBody = objectMapper.writeValueAsString(requestBody);
                
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(OPENAI_ENDPOINT))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + OPENAI_API_KEY)
                        .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody))
                        .build();
                
                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                
                return response.body().substring(2, Math.min(response.body().length(), 250));
            } catch (IOException | InterruptedException e) {
                return "Couldn't generate answer! - " + e.getMessage();
            }
        }
    }

    public static void getAPIS(List<String> apis) {
        try {
            apis = Files.readAllLines(Path.of("API_KEYS"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void teachAIModelsIfNotUsed(List<String> apis) {
        if (apis == null) return;

        for (String api : apis) {
            try {
                if (!GPT2TrainerTester.chooseFreeAi(api)) return;
            } catch (IOException ex) {
                //do nothing
            }
        }

        for (String api : apis) {
            GPT2TrainerTester.learnTheAi(Main.getLinesToLearn(), Main.getExec(), api);
        }
    }
    
    public static Executor getExec() {
        return exec;
    }
    
    public static List<String> getLinesToLearn() {
        return new ArrayList<>(linesToLearn);
    }
}
