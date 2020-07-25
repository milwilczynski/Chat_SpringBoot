package chat.Controller;

import chat.dto.*;
import chat.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin
public class UsersController {
    private UserService userService;

    @Autowired
    public UsersController(@Qualifier("userService") UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<Void> register(@RequestBody UserRegister user) {
        return userService.createUser(user) ? ResponseEntity.ok().build():ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    public ResponseEntity<FriendsAndToken> login(@RequestBody UserLogin userLogin) {
        Optional<FriendsAndToken> friendsAndToken = userService.login(userLogin);
        return friendsAndToken.isPresent() ? new ResponseEntity<>(friendsAndToken.get(),HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(value = "/addFriend")
    public ResponseEntity<Void> addFriend(@RequestParam String friendName, @RequestHeader (name="Authorization") String token){
        boolean isAdded = userService.addFriend(friendName,token);
        return isAdded ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
    @GetMapping(value = "/refresh")
    public ResponseEntity<FriendsDto> refreshFriends(@RequestHeader (name="Authorization") String token){
        return new ResponseEntity<>(userService.refresh(token),HttpStatus.OK);
    }
    @GetMapping(value = "/logOut")
    public ResponseEntity<Void> logOut(@RequestHeader (name="Authorization") String token){
        userService.logOut(token);
        return ResponseEntity.ok().build();
    }
}
