package im.whj.cn.im_ui.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author William
 * @Github WHuaJian
 * Created at 2017/9/25 下午2:48
 */

@Entity
public class MsgDbModel {

    @Id
    private String msg_id;  //消息的id，唯一性

    @NotNull
    private String user_name; //名字

    @NotNull
    private boolean isFrom; //是否是发送方或者接收方

    @NotNull
    private String content;

    @NotNull
    private String time; //时间

    @NotNull
    private int type; // 1:文字 2:图片

    @Generated(hash = 395444967)
    public MsgDbModel(String msg_id, @NotNull String user_name, boolean isFrom,
            @NotNull String content, @NotNull String time, int type) {
        this.msg_id = msg_id;
        this.user_name = user_name;
        this.isFrom = isFrom;
        this.content = content;
        this.time = time;
        this.type = type;
    }

    @Generated(hash = 1325187652)
    public MsgDbModel() {
    }

    public String getMsg_id() {
        return this.msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public boolean getIsFrom() {
        return this.isFrom;
    }

    public void setIsFrom(boolean isFrom) {
        this.isFrom = isFrom;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
