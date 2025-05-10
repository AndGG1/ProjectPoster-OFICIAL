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
    private static SpellDictionary dictionary;
    private static String result;
    private static final String SUBJECT_NOT_FOUND = "Subject not found!";
    
    static {
        //dictionary = new SpellDictionaryHashMap(new File("Dictionary.txt"));
        result = "";
    }
    
    private static final String recoverAccount = """
            """;
    private static final String security = """
            """;
    private static final String functionality = """
            """;
    private static final String AIModel = """
            """;
    private static final String interactions = """
            """;
    private static final String rules = """
            """;
    private static final String updates = """
            """;
    
    private static final Map<String, String> memory = new HashMap<>();
    private static Executor exec = Executors.newCachedThreadPool();
    private static List<String> linesToLearn = new ArrayList<>();
    
    public static String call(String content) {
        
        loadData(memory);
        String[] strongSubject = new String[1];
        strongSubject[0] = SUBJECT_NOT_FOUND;
        AtomicLong count = new AtomicLong(1);
        
        memory.forEach((k, chunk) -> {
            Map.Entry<String, Long> result = useContent(content, false, chunk);
            if (!result.getKey().equals(SUBJECT_NOT_FOUND) && count.get() < result.getValue()) {
                strongSubject[0] = k;
                count.set(result.getValue());
            }
        });
        
        if (strongSubject[0].equals(SUBJECT_NOT_FOUND)) {
            String res = useContent(content, true, "").getKey();
            
            //applies what he learned
            String responseContent = GenerativeAI.generateAnswer(content);
            memory.put(memory.size() + "", content + "\n" + res + "\n\n" + responseContent);
            
            return res + "\n\n" + responseContent;
        } else {
                return "From previous conversation: " +  memory.get(strongSubject[0]);
            }
    }
    
    private static List<Map.Entry<String, Integer>> fetchContent(String content) {
        content = content.toLowerCase();
        HashMap<String, Integer> map = new HashMap<>();
        Arrays.stream(content.split(" "))
                .forEach(s -> {
                    
                    //plurals to singulars
                    s = s.replaceAll("ies$", "y");
                    s = s.replaceAll("es$", "");
                    s = s.replaceAll("s$", "");
                    
                    //Marks
                    s = s.replaceAll("\\p{Punct}", "");
                    map.put(s, map.getOrDefault(s, 0) + 1);
                });

        
        return map.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue()
                        .reversed())
                .limit(10)
                .toList();
    }
    
//    final static ForkJoinPool commonPool = ForkJoinPool.commonPool();
    private static int checkContent(String word, String content, boolean isWeb, String chunk) {
        AtomicInteger count = new AtomicInteger();
            try {
                BufferedReader bf = null;
                if (isWeb) {
                    URL url = new URL("https://en.wikipedia.org/wiki/" + word);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                } else {
                    bf = new BufferedReader(new StringReader(chunk));
                }
                
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
                    if (isValidContent.get() && isWeb) {
                        linesToLearn.add(line);
                    }
                });

            } catch (IOException e) {
                // do nothing
            }
        return count.get();
    }
    
//    final static ForkJoinPool commonPool2 = ForkJoinPool.commonPool();
    private static Map.Entry<String, Long> useContent(String content, boolean isWeb, String chunk) {
        List<Map.Entry<String, Integer>> res = fetchContent(content);
        final String[] subject = new String[1];
        subject[0] = SUBJECT_NOT_FOUND;
        AtomicLong constant = new AtomicLong();
        long words = isWeb ? Arrays.stream(content.split(" ")).count() * 5 : 1;
        
//        commonPool2.execute(() -> {
            res.forEach(entry -> {
                int i = checkContent(entry.getKey(), content, isWeb, chunk);
                if (i >= words) {
                    if (subject[0].equals(SUBJECT_NOT_FOUND) || i > constant.get()) {
                        subject[0] = entry.getKey();
                        constant.set(i);
                    }
                }
        });
        
        if (isWeb && !subject[0].equals(SUBJECT_NOT_FOUND)) {
            return new AbstractMap.SimpleEntry<>("Here's what I found on the web: https://en.wikipedia.org/wiki/" + subject[0], 0L);
        }
        
        return new AbstractMap.SimpleEntry<>(subject[0], constant.get());
    }
    
    
    
    private static void loadData(Map<String, String> memory) {
        memory.put("recoverAccount", recoverAccount);
        memory.put("security", security);
        memory.put("functionality", functionality);
        memory.put("AiModel", AIModel);
        memory.put("interactions", interactions);
        memory.put("rules", rules);
        memory.put("updates", updates);
    }
    
    public static boolean isWordInDictionary(String word) throws IOException {
        // Update the path to match where you saved your dictionary file
        SpellChecker spellChecker = new SpellChecker(dictionary);
        
        // Tokenize the word and check its spelling
        StringWordTokenizer tokenizer = new StringWordTokenizer(word);
        spellChecker.checkSpelling(tokenizer);
        
        return spellChecker.getSuggestions(word, 1).isEmpty();
    }
    
    
    
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
    
    //Aren't immutable / defensive programming not applied
    public static Executor getExec() {
        return exec;
    }
    
    public static List<String> getLinesToLearn() {
        return new ArrayList<>(linesToLearn);
    }
}
