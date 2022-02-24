//package app.config;
//
//import app.controller.ForumController;
//import app.controller.UserController;
//import app.model.User;
//import app.model.forum.Post;
//import app.model.forum.Reply;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.validation.Valid;
//
//@Configuration
//public class ForumConfig {
//    @Bean
//    CommandLineRunner commandLineRunnerForum(ForumController controller) {
//        return args -> {
//            User user = new User(
//                    "03-07-1990",
//                    "John",
//                    "Jamal",
//                    "040404040",
//                    "Sample Address",
//                    "08711111",
//                    "John.jamal@gmail.com",
//                    "password"
//            );
//
////            User user = new User(
////                    "03-07-1990",
////                    "Mariam",
////                    "Jamal",
////                    "040404040",
////                    "Sample Address",
////                    "08711111",
////                    "mariam.jamal@gmail.com",
////                    "password"
////            );
//
//
////            user.setUser_id(1L);
//
//            Post newPost = new Post(
//                    user,
//                    "First post",
//                    "Post Content1"
//            );
//
//            newPost.setPost_id(2L);
//
////            Post newPost2 = new Post(
////                    user,
////                    "Second post",
////                    "Post 2 Content"
////            );
//
////            Post newPost3 = new Post(
////                    user,
////                    "Third post",
////                    "Post 3 Content"
////            );
////
////            controller.newPost(newPost3);
//
//            Reply testReply = new Reply(newPost, "reply content");
//            controller.newReply(testReply);
//        };
//
//
//    }
//}
