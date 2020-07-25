package chat.dto;

public class UserRegister extends UserLogin{
    private String email;

    public UserRegister(String login, String password, String email) {
        super(login, password);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
