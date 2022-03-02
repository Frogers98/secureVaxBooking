package app.repository.forum;

import app.model.forum.Post;
import app.model.forum.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

//     Update the reply_id column for a particular post in the posts table
//    @Modifying
//    @Query("update Post post set post.reply = ?1 where post.post_id = ?2")
//    int updateReplyId(Long reply_id, Long post_id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE posts p set reply_id =:reply_id where p.post_id = :post_id",
            nativeQuery = true)
    void updateReplyId(@Param("reply_id") Long reply_id, @Param("post_id") Long post_id);

//    @Modifying
//    @Query("update Post p set p.reply = :reply where p.post_id= :post_id")
//    void updateReplyId(@Param(value = "reply") Reply reply, @Param(value = "post_id") Long post);



}
