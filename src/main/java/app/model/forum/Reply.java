package app.model.forum;

import app.model.forum.Post;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "replies")
public class Reply {

    @Id
    @GeneratedValue
    private Long reply_id;

    @OneToOne
    private Post post;
//    @OneToOne
//    private Admin admin_id;

    @NotBlank
    private String reply_content;

    public Reply() {
        super();
    }

    public Reply(Post post, String reply_content) {
        this.post = post;
        this.reply_content = reply_content;
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
}
