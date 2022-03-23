package com.example.VivaLaTrip.Form;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserForm {
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z]).{6,20}", message = "아이디는 6~20자 영문 대 소문자, 숫자를 사용하세요.")
    private String username;

    @NotBlank(message = "비밀번호 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "이름은 특수문자를 제외한 2~10자리여야 합니다.")
    private String UserName_;
    private String liked;

//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getUserName() {
//        return UserName;
//    }
//
//    public void setUserName(String userName) {
//        UserName = userName;
//    }
//
//    public String getLiked() {
//        return liked;
//    }
//
//    public void setLiked(String liked) {
//        this.liked = liked;
//    }
}
