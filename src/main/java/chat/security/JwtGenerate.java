package chat.security;

public interface JwtGenerate {
    String generateToken(String userName);
}
