package DTOS.UserInterfaces.Activity;

public class Project {
    
    private final int id;
    private final String name;
    private final String description;
    private final String link;
    
    public Project(int id, String name, String description, String link) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.link = link;
    }
    
    public final int getId() {
        return Integer.parseInt(id+"");
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getLink() {
        return link;
    }
}
