package chat.services;

import chat.dto.*;
import chat.model.Friend;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    boolean createUser(UserRegister userRegister);
    Optional<FriendsAndToken> login(UserLogin userLogin);
    Set<String> getAll();
    boolean addFriend(String friendName,String token);
    FriendsDto refresh(String token);
    void logOut(String token);
}
