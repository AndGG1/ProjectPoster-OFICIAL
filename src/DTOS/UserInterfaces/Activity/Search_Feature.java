package DTOS.UserInterfaces.Activity;

import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.*;

public class Search_Feature {
    public static void main(String[] args) {
        System.out.println(searchRaw1(List.of("Skinnty", "Makitty"), "Kitty"));
        System.out.println(searchRaw2(List.of("Makitty", "Skinnty"), "Kitty"));
        System.out.println(searchRaw3(List.of("Skinnty", "Makitty"), "Kitty"));
    }
    
    //60/100 (Usually Inaccurate Percentage by 10-20% - Faster - Less Memory)
    protected static List<String> searchRaw1(List<String> projectNames, String label) {
        List<String> res = new ArrayList<>();
        label = label.trim();
        label = label.replace(" ", "");
        if (label.trim().isEmpty()) return res;
        
        for (String name : projectNames) {
            name = name.trim();
            name = name.replace(" ", "");
            double percentage = 0;
            
            String left = name.length() > label.length() ? name : label;
            String right = left.equals(name) ? label : name;
            for (int i = 0; i < Math.min(name.length(), label.length()); i++) {
                if (name.charAt(i) == label.charAt(i) || left.contains(right.charAt(i)+"")) {
                    percentage++;
                }
            }
            
            percentage = percentage * 100 / Math.max(label.length(), name.length());
            if (percentage >= 50) {
                res.add(name + " | " + percentage + "%");
            }
        }
        return res;
    }
    
    //75/100 (More Accurate Percentage - Slower - More Memory)
    protected static List<String> searchRaw2(List<String> projectNames, String label) {
        Map<Character, Integer> map = new HashMap<>();
        Map<Character, Integer> dummy = new HashMap<>();
        List<String> res = new ArrayList<>();
        if (label == null || label.trim().isEmpty()) return res;
        
        for (char ch : label.toCharArray())  {
            map.put(ch, map.getOrDefault(ch, 0)+1);
            dummy.put(ch, map.getOrDefault(ch, 0)+1);
        }
        
        for (String projectName : projectNames) {
            double percentage = 0;
            for (char ch : projectName.toCharArray()) {
                if (map.containsKey(ch)) {
                    map.put(ch, map.get(ch)-1);
                    if (map.get(ch) == 0) map.remove(ch);
                }
            }
            for (int num : map.values()) {
                percentage += num;
            }
            percentage = 100 - percentage * 100 / Math.max(projectName.length(), label.length());
            map.putAll(dummy);
            
            if (percentage >= 50) res.add(projectName  + " | " + percentage + "%");
        }
        return res;
    }
    
    //90/100 (Most Efficient - Mid_Fast - Low_Mid Memory Consuming)
    protected static List<String> searchRaw3(List<String> projectNames, String label) {
        LevenshteinDistance distance = new LevenshteinDistance();
        List<String> res = new ArrayList<>();
        
        if (label == null || label.trim().isEmpty()) return res;
        
        for (String name : projectNames) {
            String cleanName = name.trim().replace(" ", "");
            int dis = distance.apply(cleanName, label);
            double percentage = (1 - (double) dis / Math.max(name.length(), label.length())) * 100;
            
            if (percentage >= 50) {
                res.add(name + " | " + percentage + "%");
            }
        }
        return res;
    }
    
    public static double similarity(String word1, String word2) {
        LevenshteinDistance distance = new LevenshteinDistance();
        
        return distance.apply(word1, word2);
    }
}
