package app.model.forum;

import app.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue
    private Long post_id;

    // Each post has a user, which is represented as a foreign key for the user table using the primary key "user_id"
    // Posts can belong to many different users, but only one user can be the author of a post
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    // This is not a column but is used for mapping for the post_id column in the replies table
    @OneToOne(mappedBy = "post")
    private Reply reply;

    @NotBlank
    private String post_title;

    @NotBlank
    private String post_content;

    @NotBlank
    private String post_date;

    public Post() {
        super();
    }

    public Post(User user, String post_title, String post_content) {
        this.user = user;
        this.post_title = post_title;
        this.post_content = post_content;
        this.post_date = "2000-01-01";
    }

    public Long getPost_id() {
        return post_id;
    }

    public void setPost_id(Long post_id) {
        this.post_id = post_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user_id) {
        this.user = user_id;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }
}
