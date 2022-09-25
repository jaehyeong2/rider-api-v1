package jjfactory.simpleapi.business.rider.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginReq {
    private String loginId;
    private String password;


    public LoginReq(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
