package app.repository.forum;

import app.model.forum.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

//    @Query("SELECT r FROM Reply r WHERE r.post = ?1")
//    Optional<Reply> findByPostId(long post_id);


    @Query("SELECT r FROM Reply r INNER JOIN Post p ON p.reply.reply_id = r.reply_id WHERE p.post_id = ?1")
    Optional<Reply> findByPostId(long post_id);
}
