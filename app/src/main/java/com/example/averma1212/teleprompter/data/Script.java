package com.example.averma1212.teleprompter.data;

/**
 * Created by HP on 09-01-2018.
 */

public class Script {
    public String title;
    public String desc;
    public Long id;

    public Script() {

    }
    public Script(String title, String desc,Long id) {
        this.title = title;
        this.desc = desc;
        this.id = id;
    }
}
