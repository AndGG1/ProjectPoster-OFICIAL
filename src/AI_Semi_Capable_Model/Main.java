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
            AIModel description...
            
            I am a Semi-capable AI Q&A Model i.e you ask question and I provide the most related section of the project, or, if not found, a resource to look up for what you asked.
            
            I am still in developing, so I may perform slow for long contents.
            """;
    private static final String interactions = """
            Interaction / Interactions / Human / Plans / Bad / Good / Evil / Sad / crazy / bully / bullying / cyber / anti / racist / swear / swears / addressed / me / I / he / she / they / us / we / was / were / had / been / Friend / Worker / cosy
            
            You can easily talk, ask and interact with other people, but don't spam them! (read the rules / ask AI Model)
            
            Anything hurtful will break the rules! (read the rules / ask AI Model)
            
            Messaging others creates new connections and maybe establishes new friendships or co-workers!
            """;
    private static final String rules = """
            Rules / Forbidden / Detail / Detailed / Sets / Not / Allowed / Strengths
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
            AI Model Upgrade (Faster & Better responses)
            Interfaces remodeled
            Buf Fixes
            
            Current Features:
            AI Model
            Advertising your project
            Talk to the community (beta)
            Support
            Account Registration & Logging
            """;
    
    private static List<String> memory = List.of(recoverAccount, security, functionality, AIModel, interactions, rules, updates);
    
    public static void main(String[] args) {
        String[] strongSubject = new String[1];
        strongSubject[0] = "None";
        AtomicLong count = new AtomicLong(1);
        String content = """
                German Culture?
                """;
        
        if (content.length() > 500) System.out.println("Please provide a shorter text! (max. 500 words)");
        
        memory.forEach(chunk -> {
            Map.Entry<String, Long> result = useContent(content, false, chunk);
            if (!result.getKey().equals("None") || count.get() < result.getValue()) {
                strongSubject[0] = chunk.split(" ")[0];
                count.set(result.getValue());
            }
        });
        
        if (strongSubject[0].equals("None")) {
            System.out.println("Couldn't find given content in the Q&A list! Searching the web...");
            useContent(content, true, "");
        } else {
            printDefinition(strongSubject[0]);
        }
    }
    
    private static List<Map.Entry<String, Integer>> fetchContent(String content) {
        content = content.toLowerCase();
        HashMap<String, Integer> map = new HashMap<>();
        Arrays.stream(content.split(" "))
                .forEach(s -> {
                    int len = s.length() - 1;
                    if (s.charAt(len) == 's' || s.charAt(len) == '?' || s.charAt(len) == '!' || s.charAt(len) == ',' || s.charAt(len) == ';' || s.charAt(len) == '.') {
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
                    if (dummy.contains(s) && (isWordInDictionary(s) || s.length() >= 3)) count++;
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
            System.out.println("I suggest looking up on: https://en.wikipedia.org/wiki/" + subject[0]);
        }
        
        return new AbstractMap.SimpleEntry<>(subject[0], words);
    }
    
    private static void printDefinition(String subject) {
        switch (subject) {
            case "RecoverAccount":
                System.out.println("Content: " + recoverAccount);
                break;
            case "Security":
                System.out.println("Content: " + security);
                break;
            case "Functionality":
                System.out.println("Content: " + functionality);
                break;
            case "AIModel":
                System.out.println("Content: " + AIModel);
                break;
            case "Interactions":
                System.out.println("Content: " + interactions);
                break;
            case "Rules":
                System.out.println("Content: " + rules);
                break;
            case "Updates":
                System.out.println("Content: " + updates);
                break;
            default:
                System.out.println("Content not found");
        }
    }
    
    public static boolean isWordInDictionary(String word) throws IOException {
        try {
            // Update the path to match where you saved your dictionary file
            SpellDictionary dictionary = new SpellDictionaryHashMap(new File("Dictionary.txt"));
            
            SpellChecker spellChecker = new SpellChecker(dictionary);
            
            // Tokenize the word and check its spelling
            StringWordTokenizer tokenizer = new StringWordTokenizer(word);
            spellChecker.checkSpelling(tokenizer);
            
            return spellChecker.getSuggestions(word, 1).isEmpty();
        } catch (IOException e) {
            //do nothing
        }
        return false;
    }
}
