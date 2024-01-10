package kr.co.crud.domain;

import lombok.Data;

@Data
public class BoardVO {

    private String no;
    private String title;
    private String content;
    private int file;
    private int hit;
    private String uid;
    private String rdate;
    private String del_yn;

    /* 추가 */
    private String name;
    private int fno;
    private String newName;
    private String oriName;

}
