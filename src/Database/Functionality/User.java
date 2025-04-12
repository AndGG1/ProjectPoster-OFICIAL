package Database.Functionality;

import jakarta.persistence.*;

import java.util.StringJoiner;

@Entity
@Table(name="user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private int id;
    
    @Column(name ="user_name")
    private String username;
    
    @Column(name="user_pass")
    private String pass;
    
    @Column(name="user_link")
    private String link;
    
    @Column(name="user_img")
    private String img;
    
    @Column(name="user_description")
    private String description;
    
    public User() {
    }
    
    public User(String username, String pass, String link) {
        this.username = username;
        this.pass = pass;
        this.link = link;
    }
    
    public User(String username, String pass, String link, String img, String description) {
        this.username = username;
        this.pass = pass;
        this.link = link;
        this.img = img;
        this.description = description;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPass() {
        return pass;
    }
    
    public String getLink() {
        return link;
    }
    
    public String getImg() {
        return img;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setImg(String img) {
        this.img = img;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPass(String pass) {
        this.pass = pass;
    }
    
    public void setLink(String link) {
        this.link = link;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    
    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("username='" + username + "'")
                .add("pass='" + pass + "'")
                .add("link='" + link + "'")
                .add("img='" + img + "'")
                .add("description='" + description + "'")
                .toString();
    }
}
