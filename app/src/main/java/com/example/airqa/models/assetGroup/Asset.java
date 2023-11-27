package com.example.airqa.models.assetGroup;

import java.util.List;

public class Asset {
    private long id;
    private Integer version;
    private long createdOn;
    private  String name;
    private  boolean accessPublicRead;
    private  String realm;
    private  String type;
    private List<String> path;

    private Attributes attributes;



}
