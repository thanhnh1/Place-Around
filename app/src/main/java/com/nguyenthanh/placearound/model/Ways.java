package com.nguyenthanh.placearound.model;

import java.util.ArrayList;

public class Ways {
	

	 //description : give a list of ways
	
	ArrayList<Way> listWay ; 
	
	public Ways ()
	{
		listWay = null ;
	}
	
	public Ways (ArrayList<Way> ways)
	{
		this.listWay = ways ;
	}
	
	public int getCountWay ()
	{
		return listWay.size() ;
	}

}
