package kr.iam.global.exception.code;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    ADVERTISE_NOT_FOUND(404, "Advertisement Not Found"),
    EPISODE_NOT_FOUND(404, "Episode Not Found"),
    CHANNEL_NOT_FOUND(404, "Channel Not Found"),
    MEMBER_NOT_FOUND(404, "Member Not Found"),
    LOGOUT_MEMBER(404, "Member Already Logout"),
    UNABLE_TO_SEND_EMAIL(404, "Email Not Send"),
    INVALID_REQUEST(400, "No Cookie Info"),
    INVALID_REFRESHTOKEN(410, "Invalid RefreshToken");

    private final int status;

    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
