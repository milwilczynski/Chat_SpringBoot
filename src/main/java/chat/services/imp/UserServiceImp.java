package chat.services.imp;

import chat.dto.*;
import chat.expresions.RegularExpresions;
import chat.model.Friend;
import chat.model.Online;
import chat.model.User;
import chat.security.JwtGenerate;
import chat.security.configuration.JwtConfig;
import chat.services.UserService;
import chat.storage.FriendsRepository;
import chat.storage.OnlineRepository;
import chat.storage.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "userService")
public class UserServiceImp implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtGenerate jwtGenerate;
    private FriendsRepository friendsRepository;
    private OnlineRepository onlineRepository;
    @Autowired
    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder, @Qualifier("jwtGenerate") JwtGenerate jwtGenerate, FriendsRepository friendsRepository, OnlineRepository onlineRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerate = jwtGenerate;
        this.friendsRepository = friendsRepository;
        this.onlineRepository = onlineRepository;
    }

    @Override
    public boolean createUser(UserRegister userRegister) {
        Optional<User> userOptional = userRepository.findByLogin(userRegister.getLogin());
        if(userOptional.isEmpty() && validRegistration(userRegister)){
            userRepository.save(new User(
                    userRegister.getLogin().toLowerCase(),
                    passwordEncoder.encode(userRegister.getPassword()),
                    userRegister.getEmail()));
            return true;
        }
        return false;
    }

    @Override
    public Optional<FriendsAndToken> login(UserLogin userLogin) {
        Optional<User> user = userRepository.findByLogin(userLogin.getLogin().toLowerCase());
        if(user.isPresent()){
            if(passwordEncoder.matches(userLogin.getPassword(),user.get().getPassword())){
                Token token = new Token(jwtGenerate.generateToken(userLogin.getLogin().toLowerCase()));
                List<Friend> friends = friendsRepository.getAllFriends(userLogin.getLogin());
                FriendsAndToken friendsAndToken = new FriendsAndToken();
                friendsAndToken.setToken(token.getToken());
                for(Friend x : friends){
                    if(x.getUserA().equals(userLogin.getLogin())){
                        friendsAndToken.addFriend(x.getUserB());
                    }
                    friendsAndToken.addFriend(x.getUserA());
                }
                Online online = new Online();
                online.setLogin(userLogin.getLogin());
                onlineRepository.save(online);
                return Optional.of(friendsAndToken);
            }
        }
        return Optional.empty();
    }

    @Override
    public Set<String> getAll() {
        Set<String> all = new HashSet<>();
        for(User x : userRepository.findAll()){
            all.add(x.getLogin());
        }
        return all;
    }

    @Override
    public boolean addFriend(String friendName,String token) {
        String login = getLoginFromToken(token);
        if(friendName.equals(login)){
            return false;
        }
        Optional<User> findIfFriendExist = userRepository.findByLogin(friendName);
        if(findIfFriendExist.isPresent()){
            Optional<Friend> friend = friendsRepository.findFriendRelation(friendName,login);
            if(friend.isEmpty()){
                Friend friendToSave = new Friend();
                friendToSave.setUserA(friendName);
                friendToSave.setUserB(login);
                friendsRepository.save(friendToSave);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public FriendsDto refresh(String token) {
        String login = getLoginFromToken(token);
        List<Friend> friends = friendsRepository.getAllFriends(login);
        FriendsDto friendsDto = new FriendsDto();
        for(Friend x : friends){
            if(x.getUserA().equals(login)){
                friendsDto.addFriend(x.getUserB());
            }
            friendsDto.addFriend(x.getUserA());
        }
        return friendsDto;
    }

    @Override
    public void logOut(String token) {
        Optional<Online> online = onlineRepository.findByLogin(getLoginFromToken(token));
        if(online.isPresent()){
            onlineRepository.deleteById(online.get().getId());
        }
    }


    private boolean validRegistration(UserRegister userRegister){
        return RegularExpresions.validUserEmail(userRegister.getEmail())
                && RegularExpresions.validUserName(userRegister.getLogin())
                && RegularExpresions.validUserPassword(userRegister.getPassword());
    }
    private String getLoginFromToken(String token){
        Claims claims;
        token = token.substring(7);
        try {
            claims = Jwts.parser()
                    .setSigningKey(JwtConfig.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
            return "";
        }
        return claims.get("login",String.class);
    }
}
