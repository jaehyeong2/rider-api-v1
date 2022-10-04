package jjfactory.simpleapi.business.rider.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class LoginReq {
    @NotBlank
    private String loginId;
    @NotBlank
    private String password;
    @Builder
    public LoginReq(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
