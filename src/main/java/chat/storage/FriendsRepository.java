package chat.storage;

import chat.model.Friend;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Optional;

public interface FriendsRepository extends CrudRepository<Friend,Long> {
    @Query(value = "SELECT * FROM chat.friends WHERE (user_a = :a AND user_b = :b) OR (user_a = :b AND user_b = :a)",nativeQuery = true)
    Optional<Friend> findFriendRelation(@Param("a") String userA, @Param("b") String userB);

    @Query(value = "SELECT * FROM chat.friends WHERE user_a = :us OR user_b = :us",nativeQuery = true)
    ArrayList<Friend> getAllFriends(@Param("us")String user);
}
