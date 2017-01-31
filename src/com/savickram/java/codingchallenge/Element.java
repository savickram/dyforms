package com.savickram.java.codingchallenge;

import java.util.ArrayList;

/**
 * 
 * This class represents an element in the form
 * It hold all the info about the element
 * 
 * @author Vickram
 *
 */
public class Element {

	public int id;

	public String name;

	public String[] values;
	
	public String type;

	public boolean hasChild;
	
	public boolean hasParent;

	public ArrayList<ChildElement> childList;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	public boolean isHasChild() {
		return hasChild;
	}

	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}

	public ArrayList<ChildElement> getChildList() {
		return childList;
	}

	public void setChildList(ArrayList<ChildElement> childList) {
		this.childList = childList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isHasParent() {
		return hasParent;
	}

	public void setHasParent(boolean hasParent) {
		this.hasParent = hasParent;
	}

}
