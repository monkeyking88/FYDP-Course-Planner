package org.language.codegenerator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class SimpleBlock {
	private SimpleBlock parent;
	private Collection<SimpleBlock> children = new HashSet<SimpleBlock>();
	// the corresponding sig in alloy
	private String name;

	public SimpleBlock(String name, SimpleBlock parent){
		this.name = name;
		if(parent == null){
			this.parent = null;
		} else {
			parent.addChild(this);
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SimpleBlock getParent() {
		return parent;
	}
	private void setParent(SimpleBlock parent) {
		this.parent = parent;
	}
	public Collection<SimpleBlock> getChildren() {
		return children;
	}
	
	public boolean addChild(SimpleBlock child){
		if(child.getParent() != this){
			child.setParent(this);
		}
		return children.add(child);
	}
	public boolean removeChild(SimpleBlock child){
		if(child.getParent() != null){
			child.setParent(null);
		}
		return children.remove(child);
	}
	public Collection<SimpleBlock> getSiblings(){
		if(parent == null){
			return null;
		}
		LinkedList<SimpleBlock> siblings = new LinkedList<SimpleBlock>();
		Iterator<SimpleBlock> it = parent.getChildren().iterator();
		while(it.hasNext()){
			SimpleBlock simpleBlock = it.next();
			if(simpleBlock != this){
				siblings.add(simpleBlock);
			}
		}
		return siblings;
	}
}