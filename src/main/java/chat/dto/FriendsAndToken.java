package chat.dto;

import java.util.ArrayList;
import java.util.List;

public class FriendsAndToken {
    List<String> friends;
    String token;

    public FriendsAndToken() {
        friends = new ArrayList<>();
    }

    public void addFriend(String login){
        this.friends.add(login);
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
