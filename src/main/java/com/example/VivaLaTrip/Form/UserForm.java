package com.example.VivaLaTrip.Form;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class UserForm {
    private String ID;
    private String PW;
    private String UserName;
    private String liked;

    public String getID() {
        return ID;
    }

    public String getPW() {
        return PW;
    }

    public String getUserName() {
        return UserName;
    }

    public String getLiked() {
        return liked;
    }
}
