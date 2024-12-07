package AI_Semi_Capable_Model;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    private static SpellDictionary dictionary;
    private static String result;
    
    static {
        try {
            dictionary = new SpellDictionaryHashMap(new File("Dictionary.txt"));
            result = "";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static final String recoverAccount = """
            RecoverAccount description...
            In developing
            """;
    private static final String security = """
            Security description...
            In developing
            """;
    private static final String functionality = """
            Functionality description...
            In developing
            """;
    private static final String AIModel = """
            AI Model / are / you / bot / evil / smart / genius
            
            Don't hesitate to report any form of (cyber) bullying or any abusive behaviour!
            
            I am still in developing, so I may perform slow for long contents.
            
            In case your asking, no, I can't provide answers my self due to the lack of developing in my side i.e not smart enough yet
            """;
    private static final String interactions = """
            Interaction / Interactions / Human / Plans / Bad / Good / Evil / Sad / crazy / bully / bullying / cyber / anti / racist / swear / swears / addressed / me / I / he / she / they / us / we / was / were / had / been / Friend / Worker / cosy
            
            You can easily talk, ask and interact with other people, but don't spam them! (read the rules / ask AI Model)
            
            Anything hurtful will break the rules! (read the rules / ask AI Model)
            
            Messaging others creates new connections and maybe establishes new friendships or co-workers!
            """;
    private static final String rules = """
            Rule / Forbidden / Detail / Detailed / Sets / Not / Allowed / Strengths do / done
            Don't hesitate to report any form of (cyber) bullying or any abusive behaviour!
            
            Don't scam, spam or behave bad with other users!
            
            Respect the community & cultures.
            
            Don't abuse the account registration as out databases are small and any registration will cost us.
            
            Don't abuse the AI Model i.e ask question with no sense.
            """;
    private static final String updates = """
            Updates / Update / New / Old / Feature / Newest / Anything / App / Application
            
            Updates take time as this is a startup and a non-financed Application.
            In order for any further updates to happen, the community should get involved with suggestions and feedback, as well as support and not restrict timelines.
            
            Next Update:
            Artificial Intelligence Upgrade
            Interfaces remodeled
            Bug Fixes
            
            Current Features:
            Advertising your project
            Talk to the community (beta)
            Support
            Account Registration & Logging
            """;
    
    private static final Map<String, String> memory = new HashMap<>();
    
    public static String call(String content) {
        
        loadData(memory);
        
        String[] strongSubject = new String[1];
        strongSubject[0] = "None";
        AtomicLong count = new AtomicLong(1);
        
        memory.forEach((k, chunk) -> {
            Map.Entry<String, Long> result = (Map.Entry<String, Long>) useContent(content, false, chunk);
            if (!result.getKey().equals("None") && count.get() < result.getValue()) {
                strongSubject[0] = k;
                count.set(result.getValue());
            }
        });
        
        if (strongSubject[0].equals("None")) {
            System.out.println("Couldn't find given content in the Q&A list! Searching the web...");
            String res = useContent(content, true, "").getKey();
            System.out.println(res + "-");
            return res;
        } else {
            return memory.get(strongSubject[0]);
        }
        //memory.put(memory.size() + "", content);
    }
    
    private static List<Map.Entry<String, Integer>> fetchContent(String content) {
        content = content.toLowerCase();
        HashMap<String, Integer> map = new HashMap<>();
        Arrays.stream(content.split(" "))
                .forEach(s -> {
                    int len = s.length() - 1;
                    if (len >= 0 && (s.charAt(len) == 's' || s.charAt(len) == '?' || s.charAt(len) == '!' || s.charAt(len) == ',' || s.charAt(len) == ';' || s.charAt(len) == '.')) {
                        s = s.substring(0, len);
                    } else if (s.charAt(len) == 'g' && s.charAt(len-1) == 'n' && s.charAt(len-2) == 'i') {
                        s = s.substring(0, len-2);
                    }
                    map.put(s, map.getOrDefault(s, 0) + 1);
                });
        
        return map.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue()
                        .reversed())
                .limit(10)
                .toList();
    }
    
    private static int checkContent(String word, String content, boolean isWeb, String chunk) {
        int count = 0;
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
            
            String dummy;
            while ((dummy = bf.readLine()) != null) {
                for (String s : content.split(" ")) {
                    if (dummy.contains(s) && (isWordInDictionary(s) || s.length() >= 4)) count++;
                }
            }
        } catch (IOException e) {
            // do nothing
        }
        return count;
    }
    
    private static Map.Entry<String, Long> useContent(String content, boolean isWeb, String chunk) {
        List<Map.Entry<String, Integer>> res = fetchContent(content);
        final String[] subject = new String[1];
        subject[0] = "None";
        AtomicLong constant = new AtomicLong();
        long words = isWeb ? Arrays.stream(content.split(" ")).count() * 5 : 1;
        
        res.forEach(entry -> {
            int i = checkContent(entry.getKey(), content, isWeb, chunk);
            if (i >= words) {
                if (subject[0].equals("None") || i > constant.get()) {
                    subject[0] = entry.getKey();
                    constant.set(i);
                }
            }
        });
        
        if (isWeb && !subject[0].equals("None")) {
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
}
