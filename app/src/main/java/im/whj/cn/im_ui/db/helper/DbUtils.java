/*
 * Created by William on 17-9-21 上午9:26
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 17-9-21 上午9:26
 */

package im.whj.cn.im_ui.db.helper;

import java.util.ArrayList;
import java.util.List;

import im.whj.cn.im_ui.db.MsgDbModelDao;
import im.whj.cn.im_ui.db.entity.MsgDbModel;
import im.whj.cn.im_ui.db.service.MsgService;

/**
 * @author William
 * @Github WHuaJian
 * Created at 2017/9/21 上午9:26
 */

public class DbUtils {

    private static DbUtils mHelper;
    private static MsgService mMessageService;

    public DbUtils() {
        mMessageService = MsgService.getInstance();
    }

    public static DbUtils getInstance() {
        synchronized (DbUtils.class) {
            mHelper = new DbUtils();
            return mHelper;
        }

    }

    /* 分页查询 10条 */
    public List<MsgDbModel> getHistoryMessage(int sqlOffset) {
        List<MsgDbModel> messages = mMessageService.queryBuilder()
                .offset(sqlOffset * 10)
                .orderDesc(MsgDbModelDao.Properties.Time).limit(10).list();
        return messages;
    }

    public long queryMsgCount(){

        return mMessageService.count();
    }

    public void insertMessage2Table(MsgDbModel message) {
        mMessageService.saveOrUpdate(message);
    }


    /* 查询聊天记录所有的图片 */
    public ArrayList<String> queryAllPhoto() {
        ArrayList<String> imgs = new ArrayList<>();
        List<MsgDbModel> models = mMessageService.queryBuilder()
                .where(MsgDbModelDao.Properties.Type.eq("2"))
                .orderAsc(MsgDbModelDao.Properties.Time).build().list();
        for (int i = 0; i < models.size(); i++) {
            imgs.add(models.get(i).getContent());
        }
        return imgs;
    }

    /* 查询当前图片在所有图片的位置 */
    public int getImgPosition(String msgId) {
        int position = 0;
        List<MsgDbModel> models = mMessageService.queryBuilder()
                .where(MsgDbModelDao.Properties.Type.eq("2"))
                .orderAsc(MsgDbModelDao.Properties.Time).build().list();
        for (int i = 0; i < models.size(); i++) {
            if (msgId.equals(models.get(i).getMsg_id())) {
                position = i;
                break;
            }
        }
        return position;
    }

    public void deleteAll(){
        mMessageService.deleteAll();
    }


    public void detroy(){
        mMessageService = null;
    }
}
