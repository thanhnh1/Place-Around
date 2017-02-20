package com.nguyenthanh.placearound.model;

import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.List;

public class Places implements Serializable {

    @Key
    public String status;

    @Key
    public List<Place> results;

}
