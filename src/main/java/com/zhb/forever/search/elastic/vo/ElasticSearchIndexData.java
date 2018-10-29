package com.zhb.forever.search.elastic.vo;

import java.util.Calendar;
import java.util.Map;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年10月29日下午1:41:03
*/

public class ElasticSearchIndexData {

    private String id;
    private String name;
    private String sex;
    private int age;
    private Calendar birthday;
    private String phone;
    private String email;
    
    public void setData(Map<String, Object> map){
        if (null != map) {
            this.setId(null==map.get("id")?"":map.get("id").toString());
            this.setName(null==map.get("name")?"":map.get("name").toString());
            this.setAge(null==map.get("age")?0:Integer.parseInt(map.get("age").toString()));
            this.setSex(null==map.get("sex")?"":map.get("sex").toString());
            this.setBirthday(null==map.get("birthday")?null:(Calendar)map.get("birthday"));
            this.setPhone(null==map.get("phone")?"":map.get("phone").toString());
            this.setEmail(null==map.get("email")?"":map.get("email").toString());
        }
    }
    
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    
    public Calendar getBirthday() {
        return birthday;
    }
    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

}


