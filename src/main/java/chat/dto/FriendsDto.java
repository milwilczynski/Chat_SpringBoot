package chat.dto;

import java.util.ArrayList;
import java.util.List;

public class FriendsDto {
    List<String> friends;

    public FriendsDto() {
        this.friends = new ArrayList<>();
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
}
