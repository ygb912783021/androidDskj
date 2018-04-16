package dingshi.com.hibook.bean;

/**
 * @author wangqi
 * @since 2018/1/11 16:19
 */

public class Avatar {

    private String nick;
    private String avatar;


    public Avatar(){

    }

    public Avatar(String nick, String avatar) {
        this.nick = nick;
        this.avatar = avatar;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    @Override
    public String toString() {
        return "Avatar{" +
                "nick='" + nick + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }


}
