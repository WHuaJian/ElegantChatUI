package im.whj.cn.im_ui.db.service;

import im.whj.cn.im_ui.db.entity.MsgDbModel;
import im.whj.cn.im_ui.db.helper.DbCore;

/**
 * @author William
 * @Github WHuaJian
 * Created at 2017/9/25 下午3:18
 */

public class MsgService extends BaseService<MsgDbModel, String> {

    public static MsgService messageDbService;

    public MsgService() {
        super(DbCore.getDaoSession().getMsgDbModelDao());
    }

    public static MsgService getInstance() {
        synchronized (MsgService.class) {
            if (messageDbService == null) {
                return SingleLoader.INSTANCE;
            }
        }
        return messageDbService;
    }

    private static class SingleLoader {
        final static MsgService INSTANCE = new MsgService();
    }

}
