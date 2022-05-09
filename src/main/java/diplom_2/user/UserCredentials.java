package diplom_2.user;

import lombok.Data;

@Data
public class UserCredentials {
    private String email;
    private String password;
    private String name;

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
