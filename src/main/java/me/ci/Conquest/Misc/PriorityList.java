package me.ci.Conquest.Misc;

import java.util.HashMap;

public class PriorityList<T>{
	private final HashMap<T,Integer> list = new HashMap<>();
	public T getMostImportant(){
		if(list.isEmpty())return null;
		T t = null;
		int p = 0;
		int m;
		for(T a : list.keySet()){
			m=list.get(a);
			if(m>p){
				p=m;
				t=a;
			}
		}
		return t;
	}
	public void add(final T t, int p){
		if(p<0)return;
		list.put(t, p);
	}
	public int getSize(){ return list.size(); }
	public void remove(final T t){ list.remove(t); }
}