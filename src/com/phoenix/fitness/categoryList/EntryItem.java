package com.phoenix.fitness.categoryList;


public class EntryItem implements Item{

	public final String title;
	public final String subtitle;
	public final String category;

	public EntryItem(String title, String subtitle,String category) {
		this.title = title;
		this.subtitle = subtitle;
		this.category=category;
	}
	
	public boolean isSection() {
		return false;
	}

}
