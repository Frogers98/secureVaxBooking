package app.model;

import app.model.Post;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "replies")
public class Reply {

    @Id
    @GeneratedValue
    private Long reply_id;

    @OneToOne
    private Post post_id;
//    @OneToOne
//    private Admin admin_id;

    @NotBlank
    private String reply_content;

    public Reply() {
        super();
    }

    public Reply(Post post_id, String reply_content) {
        this.post_id = post_id;
        this.reply_content = reply_content;
    }

    public Long getReply_id() {
        return reply_id;
    }

    public void setReply_id(Long reply_id) {
        this.reply_id = reply_id;
    }

    public Post getPost_id() {
        return post_id;
    }

    public void setPost_id(Post post_id) {
        this.post_id = post_id;
    }

    public String getReply_content() {
        return reply_content;
    }

    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }
}
