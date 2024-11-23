package AI_Semi_Capable_Model;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    
    private static final String recoverAccount = """
            hrpppt_O2>>
            """;
    private static final String security = """
            hrpppt_O2>>
            """;
    private static final String functionality = """
            hrpppt_O2>>
            """;
    private static final String AIModel = """
            hrpppt_O2>>
            """;
    private static final String interactions = """
            hrpppt_O2>>
            """;
    private static final String rules = """
            hrpppt_O2>>
            """;
    private static final String updates = """
            hrpppt_O2>>
            """;
    private static List<String> memory = List.of(recoverAccount, security, functionality, AIModel, interactions, rules, updates);
    
    
    
    public static void main(String[] args) {
        String[] strongSubject = new String[1]; strongSubject[0] = "None";
        AtomicLong count = new AtomicLong();
        String content = """
                What are Binary Trees?
                """;
        
        memory.forEach(chunk -> {
            Map.Entry<String, Long> result = useContent(content, false, chunk);
            if (!result.equals("None") || count.get() < result.getValue()) {
                strongSubject[0] = result.getKey();
            }
        });
        System.out.println(strongSubject[0]);
        
        System.out.println("Subject: "+ Objects.requireNonNull(useContent(content, true, "")).getKey());
    }
    
    
    private static List<Map.Entry<String, Integer>> fetchContent(String content) {
        
        content = content.toLowerCase();
        HashMap<String, Integer> map = new HashMap<>();
        Arrays.stream(content.split(" "))
                .forEach(s -> {
                    int len = s.length()-1;
                    if (s.charAt(len) == 's' || s.charAt(len) == '?' || s.charAt(len) == '!' || s.charAt(len) == ',' || s.charAt(len) == ';' || s.charAt(len) == '.') {
                        s = s.substring(0, len);
                    }
                    map.put(s, map.getOrDefault(s, 0)+1);
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
                    if (dummy.contains(s)) count++;
                }
            }
            
        } catch (IOException e) {
            //do nothing
        }
        return count;
    }
    
    private static Map.Entry<String, Long> useContent(String content, boolean isWeb, String chunk) {
        
        List<Map.Entry<String, Integer>> res = fetchContent(content);
        final String[] subject = new String[1]; subject[0] = "None";
        AtomicLong constant = new AtomicLong();
        long words = Arrays.stream(content.split(" ")).count() * 5;
        
        res.forEach(entry -> {
            int i = checkContent(entry.getKey(), content, isWeb, chunk);
            if (i >= words) {
                if (subject[0] == null || i > constant.get()) {
                    subject[0] = entry.getKey();
                    constant.set(i);
                }
            }
        });
        ;
        return new AbstractMap.SimpleEntry<>(subject[0], words);
    }
}
