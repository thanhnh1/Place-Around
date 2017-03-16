package com.nguyenthanh.placearound.LoginApi.object;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 3/15/2017.
 */

public class ObjectResponseData {

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @SerializedName("id")
    private String id;

    @SerializedName("content")
    private String content;

    @SerializedName("campaign")
    private String campaign;

    @SerializedName("creatorName")
    private String creatorName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getContent() {

        return content;
    }

    public String getCampaign() {
        return campaign;
    }

    public String getCreatorName() {
        return creatorName;
    }
}
