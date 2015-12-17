package com.vector.ven.network.api;

import com.vector.ven.network.Team;
import com.vector.ven.network.TeamListener;
import com.vector.ven.network.TeamUrl;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/12/17
 * Description:<p>{TODO: 用一句话描述}
 */
public class TranslationManager {

    private Team mTeam;

    public TranslationManager() {
        mTeam = new Team();
    }

    public void translation(String work, int requestCode, TeamListener listener) {
        TeamUrl url = new TeamUrl(API.TRANSLATION);
        url.add("keyfrom", "VectorHuang")
                .add("key", "2066326006")
                .add("type", "data")
                .add("doctype", "json")
                .add("version", "1.1")
                .add("q", work);
        mTeam.setUrl(url);
        mTeam.get(requestCode,listener);
    }

}
