package marketplace.service.auth;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaoyuliang
 */
public class FakeAuthenticationService implements AuthenticationService {
    private final Map<String, String> keyUserMap;

    /**
     * A fake authentication service with two users
     * User1: id "111" apikey = "aaa-bbb-ccc"
     * User2: id "222" apikey = "abc-abc-abc"
     */
    public FakeAuthenticationService() {
        keyUserMap = new HashMap<>();
        keyUserMap.put("aaa-bbb-ccc", "111");
        keyUserMap.put("abc-abc-abc", "222");
    }

    @Override
    public String authenticate(String apiKey) {
        if (keyUserMap.containsKey(apiKey)) {
            return keyUserMap.get(apiKey);
        } else {
            throw new AuthenticationException("User not exist");
        }
    }
}
