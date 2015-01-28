package com.bainiaohe.dodo.main.fragments.info.model;

import java.util.ArrayList;

/**
 * Created by zhugongpu on 15/1/22.
 */
public class InfoItem {
    public String name;//用户名
    public boolean isMarked;//是否已经赞过
    public String avatarImage;//头像
    public String text_content = "";//文本内容
    public ArrayList<String> imageUrls;
}
