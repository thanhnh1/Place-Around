package com.nguyenthanh.placearound.model;

public class Way {

	
	private final static String ZERO = null ;
	
	private String strVia ; 
	private float fDistance ;
	private float fTime ; 
	
	public Way ()
	{
		strVia = ZERO ;
		fDistance = 0 ; 
		fTime = 0 ;
	}
	
	public Way(String strVia , float distance , float time)
	{
		this.strVia = strVia ;
		this.fDistance = distance ;
		this.fTime = time ;
	}
	
	public String getVia ()
	{
		return this.strVia ;
	}
	
	public float getDistance()
	{
		return this.fDistance ;
	}
	public float getTime ()
	{
		return this.fTime ; 
	}

}
