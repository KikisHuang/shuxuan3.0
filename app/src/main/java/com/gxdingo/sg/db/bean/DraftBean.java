package com.gxdingo.sg.db.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class DraftBean {

    @Id(autoincrement = true)
    public Long id;

    @Unique
    public String uuid;

    @Property
    public String draft;

    @Property
    public String sendIdentifier;


    @Generated(hash = 650146334)
    public DraftBean(Long id, String uuid, String draft, String sendIdentifier) {
        this.id = id;
        this.uuid = uuid;
        this.draft = draft;
        this.sendIdentifier = sendIdentifier;
    }

    @Generated(hash = 2001552740)
    public DraftBean() {
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDraft() {
        return this.draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
    }

    public String getSendIdentifier() {
        return this.sendIdentifier;
    }

    public void setSendIdentifier(String sendIdentifier) {
        this.sendIdentifier = sendIdentifier;
    }

}
