package com.vector.ven.model;

import com.vector.ven.constant.Constants;
import com.vector.ven.entity.YouWord;
import com.vector.ven.network.TeamDownloader;
import com.vector.ven.network.TeamListener;
import com.vector.ven.network.TeamUrl;
import com.vector.ven.network.api.API;
import com.vector.ven.network.api.TranslationAPI;
import com.vector.ven.table.Word;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/12/19
 * Description:<p>{TODO: 用一句话描述}
 */
public class WordModel {

    private TranslationAPI mTranslation;
    private TeamDownloader mDownloader;

    public WordModel() {
        mTranslation = new TranslationAPI();
        mDownloader = new TeamDownloader();
    }

    public List<YouWord> query(int page) {
        List<Word> words = DataSupport.findAll(Word.class);
        List<YouWord> youWords = new ArrayList<>(words.size());
        for (Word word : words) {
            youWords.add(YouWord.createByJson(word.getJson()));
        }
        return youWords;
    }

    public void downloadAudio(String query, TeamListener listener) {
        //http://tts.baidu.com/text2audio?lan=zh&pid=101&ie=
        // UTF-8&text=%E5%A5%BD%E7%9A%84&spd=2";
        TeamUrl url = new TeamUrl(API.BAIDU_AUDIO);
        char lanC = query.charAt(0);
        String lan;
        if ((lanC >= 'a' && lanC <= 'z') ||
                (lanC >= 'A' && lanC <= 'Z')) {
            //英文
            lan = "en";
        } else {
            //中文
            lan = "zh";
        }

        url.add("lan", lan)
                .add("pid", "101")
                .add("ie", "UTF-8")
                .add("text", query)
                .add("spd", "2");
        mDownloader.download(url, Constants.SYS_PATH, query + ".mp3", listener);
    }

    /**
     * 查找一个单词的翻译，当数据库不存在这个单词的时候，到网络查找
     *
     * @param query 需要翻译的单词
     * @return 如果数据库有这个单词，返回对应的翻译，否则 return null
     */
    public YouWord query(String query, int requestCode, TeamListener listener) {

        List<Word> words = DataSupport.where("query = '" + query + "'").find(Word.class);
        if (words.isEmpty()) {
            mTranslation.translation(query, requestCode, listener);
        } else {
            Word word = words.get(0);
            return YouWord.createByJson(word.getJson());
        }
        return null;
    }

    /**
     * 保存一个单词
     *
     * @param query
     * @param json
     * @return if success return true
     */
    public boolean save(String query, String json) {
        if (isEmpty(query)) {
            Word word = new Word();
            word.setQuery(query);
            word.setJson(json);
            return word.save();
        }

        return false;
    }

    /**
     * 检查一个单词是否已经保存到了数据库
     *
     * @param query
     * @return
     */
    public boolean isEmpty(String query) {
        List<Word> words = DataSupport.where("query = '" + query + "'").find(Word.class);
        return words.isEmpty();
    }


    /**
     * 删除
     *
     * @param query 需要删除的单词
     * @return 删除的行数
     */
    public int delete(String query) {
        return DataSupport.deleteAll(Word.class, "query = '" + query + "'");
    }

}
