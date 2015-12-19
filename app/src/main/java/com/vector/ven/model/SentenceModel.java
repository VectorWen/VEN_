package com.vector.ven.model;

import android.text.format.Time;

import com.vector.ven.application.App;
import com.vector.ven.constant.Constants;
import com.vector.ven.entity.Sentence;
import com.vector.ven.network.Team;
import com.vector.ven.network.TeamDownloader;
import com.vector.ven.network.TeamListener;
import com.vector.ven.network.TeamUrl;
import com.vector.ven.network.api.API;
import com.vector.ven.table.Word;
import com.vector.ven.util.DataPool;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/12/19
 * Description:<p>{TODO: 用一句话描述}
 */
public class SentenceModel {
    private DataPool mDataPool;
    private Team mTeam;
    private TeamDownloader mDownloader;

    private static final String KEY_SENTENCE = "key_sentence";

    public SentenceModel(){
        mTeam = new Team();
        mDataPool = new DataPool(App.mInstance);
        mDownloader = new TeamDownloader();
    }

    public void downloadAudio(Sentence sentence, TeamListener listener) {
        TeamUrl url = new TeamUrl(sentence.getTts());
        mDownloader.download(url, Constants.SYS_PATH,sentence.getDateline()+".mp3",listener);
    }

    public boolean save(Sentence sentence){
        if(isEmpty(sentence)){
            return sentence.save();
        }
        return false;
    }

    /**
     * 删除
     *
     * @param sentence
     * @return 删除的行数
     */
    public int delete(Sentence sentence) {
        return DataSupport.deleteAll(Sentence.class, "dateline = '" + sentence.getDateline() + "'");
    }

    /**
     * 检查一个单词是否已经保存到了数据库
     *
     * @param sentence
     * @return
     */
    public boolean isEmpty(Sentence sentence) {
        List<Sentence> sentences = DataSupport.where("dateline = '" + sentence.getDateline() + "'").find(Sentence.class);
        return sentences.isEmpty();
    }


    /**
     * 获取今天的句子
     * 1，本地缓存有了，那么就拿本地的，否则网上下载
     * @return
     */
    public Sentence getTodaySentence(int requestCode,TeamListener listener){
        Time time = new Time();
        time.setToNow();
        String today = time.format3339(true);
        Sentence sentence = (Sentence) mDataPool.get(KEY_SENTENCE);
        if(sentence != null&&sentence.getDateline().equals(today)){
            return sentence;
        }

        mTeam.setUrl(API.SENTENCE);
        mTeam.get(requestCode,listener);
        return null;
    }

    public void cacheSentence(Sentence sentence){
        mDataPool.put(KEY_SENTENCE,sentence);
    }


}
