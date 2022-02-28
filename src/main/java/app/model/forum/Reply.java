package app.model.forum;

import app.model.forum.Post;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "replies")
public class Reply {

    @Id
    @GeneratedValue
    private Long reply_id;

    //post_id column that represents the primary key in the posts column

    // This is not a column but is used for mapping for the reply_id column in the posts table
    @OneToOne(mappedBy = "reply")
    private Post post;
//    @OneToOne
//    private Admin admin_id;

    @NotBlank
    private String reply_content;

    @NotBlank
    private String reply_date;

    public Reply() {
        super();
    }

    public Reply(Post post, String reply_content) {
        this.post = post;
        this.reply_content = reply_content;
        this.reply_date = "2000-01-01";
    }

    public Long getReply_id() {
        return reply_id;
    }

    public void setReply_id(Long reply_id) {
        this.reply_id = reply_id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post_id) {
        this.post = post_id;
    }

    public String getReply_content() {
        return reply_content;
    }

    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }

    public String getReply_date() {
        return reply_date;
    }

    public void setReply_date(String reply_date) {
        this.reply_date = reply_date;
    }
}
