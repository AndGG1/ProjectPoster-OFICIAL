package DTOS.UserInterfaces.Activity;

import jakarta.persistence.*;

@Entity
@Table(name = "projects")
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private int id;
    
    @Column(name = "project_name")
    private String name;
    
    @Column(name = "project_description")
    private String description;
    
    @Column(name = "project_link")
    private String link;
    
    @Column(name = "project_owner")
    private String owner;
    
    public Project(int id, String name, String description, String link, String owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.link = link;
        this.owner = owner;
    }
    
    public Project() {
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
    
    public String getOwner() {
        return owner;
    }
}
