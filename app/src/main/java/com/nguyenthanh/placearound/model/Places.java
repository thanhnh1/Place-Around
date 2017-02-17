package com.nguyenthanh.placearound.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.util.Key;

public class Places implements Serializable{

	@Key
    public String status;
 
    @Key
    public List<Place> results;

}
