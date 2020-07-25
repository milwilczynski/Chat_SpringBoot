package chat.storage;

import chat.model.Online;
import chat.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OnlineRepository extends CrudRepository<Online,Long> {
    Optional<Online> findByLogin(String login);
}
