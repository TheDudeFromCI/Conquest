package me.ci.Conquest.Misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class PathFinder{
	private Node s;
	private Node e;
	private PathBuilder b;
	private Path p;
	private final LinkedList<Node> open = new LinkedList<Node>();
	private final ArrayList<Node> closed = new ArrayList<Node>();
	private final ArrayList<Node> created = new ArrayList<Node>();
	public void setStart(final Node start)throws PathFindingException{
		if(s!=null)throw new PathFindingException("Start location already set!");
		s=start;
		open.add(s);
	}
	public void setEnd(final Node end)throws PathFindingException{
		if(e!=null)throw new PathFindingException("End location already set!");
		e=end;
	}
	public Node getNodeAt(final int x, final int y){
		Node n = null;
		for(Node n1 : created){
			if(n1.getX()==x
					&&n1.getY()==y){
				n=n1;
				break;
			}
		}
		if(n!=null)return n;
		n=new Node(x, y, 0);
		created.add(n);
		return n;
	}
	public synchronized void findPath(final DistanceForumula df, final int range)throws PathFindingException{
		if(s==null)throw new PathFindingException("No start location set!");
		if(e==null)throw new PathFindingException("No end location set!");
		if(b==null)throw new PathFindingException("No PathBuilder is set!");
		if(p!=null)throw new PathFindingException("Path already made!");
		Node last;
		while(true){
			if(open.isEmpty())throw new PathFindingException("No path found!");
			Node n = getCheapestNode(df);
			if(range>0&&!(s.x-range<=n.x&&n.x<=s.x+range))throw new PathFindingException("Out of range!");
			if(range>0&&!(s.y-range<=n.y&&n.y<=s.y+range))throw new PathFindingException("Out of range!");
			open.remove(n);
			closed.add(n);
			if(n.getX()==e.getX()&&n.getY()==e.getY()){
				last=n;
				break;
			}
			for(Node no : b.getOpenPoints(n)){
				if(range>0&&!(s.x-range<=no.x&&no.x<=s.x+range))continue;
				if(range>0&&!(s.y-range<=no.y&&no.y<=s.y+range))continue;
				if(closed.contains(no))continue;
				if(no.getMoveCost()<0)continue;
				if(open.contains(no)){
					if(n.getG()+no.getMoveCost()<no.getG()){
						no.setG(n.getG()+no.getMoveCost());
						no.setParent(n);
					}
				}else{
					open.add(no);
					no.setParent(n);
					no.setG(n.getG()+no.getMoveCost());
				}
			}
		}
		Node parent = last;
		final ArrayList<Node> path = new ArrayList<Node>();
		if(parent!=null){
			while(true){
				path.add(parent);
				parent.setPartOfPath(true);
				parent=parent.getParent();
				if(parent==null)break;
			}
		}
		p=new Path(path);
	}
	public Node getStart(){
		return s;
	}
	public Node getEnd(){
		return e;
	}
	public Path getPath(){
		return p;
	}
	public void setPathBuilder(final PathBuilder builder)throws PathFindingException{
		if(b!=null)throw new PathFindingException("PathBuilder already set!");
		b=builder;
	}
	private Node getCheapestNode(final DistanceForumula df){
		Node node = open.get(0);
		for(Node n : open){
			if(n.getF(df)<node.getF(df))node=n;
		}
		return node;
	}
	public static enum DistanceForumula{ MANHATTEN, DIAGONAL_SHORTCUT, DISTANCE; }
	public static interface PathBuilder{ public ArrayList<Node> getOpenPoints(final Node origin); }
	public class Node{
		private final int x;
		private final int y;
		private HashMap<DistanceForumula,Integer> h = new HashMap<DistanceForumula,Integer>();
		private int g;
		private boolean path;
		private int movecost;
		private Node parent;
		public Node(final int xcords, final int ycords, final int cost){
			x=xcords;
			y=ycords;
			movecost=cost;
		}
		public int getH(final DistanceForumula df){
			if(h.containsKey(df))return h.get(df);
			int out = 0;
			switch(df){
			case DIAGONAL_SHORTCUT:
				int xd = Math.abs(x-e.x);
				int yd = Math.abs(y-e.y);
				if(xd>yd)out=xd;
				else out=yd;
				break;
			case DISTANCE:
				out=(int)Math.sqrt(Math.pow(x-e.x, 2)+Math.pow(y-e.y, 2));
				break;
			case MANHATTEN:
				out=Math.abs(x-e.x)+Math.abs(y-e.y);
				break;
			default:
				break;
			}
			h.put(df, out);
			return out;
		}
		public void setG(final int newg){ g=newg; }
		public int getX(){ return x; }
		public int getY(){ return y; }
		public int getG(){ return g; }
		public int getF(final DistanceForumula df){ return getH(df)+g; }
		public boolean isPartOfPAth(){ return path; }
		public void setPartOfPath(final boolean ispath){ path=ispath; }
		public int getMoveCost(){ return movecost; }
		public Node getParent(){ return parent; }
		public void setParent(final Node node){ parent=node; }
		public void setMoveCost(final int cost){ movecost=cost; }
	}
	public static class Path{
		private final ArrayList<Node> p;
		private int pos = -1;
		private Path(final ArrayList<Node> path){
			p=path;
			pos=p.size();
		}
		public Node getNext(){
			pos--;
			return p.get(pos);
		}
		public boolean hasNext(final boolean negative){
			if(negative)return pos>-1;
			return pos>0;
		}
		public void resetIterator(){ pos=p.size(); }
		public int getSize(){ return p.size(); }
	}
	@SuppressWarnings("serial")
	public static class PathFindingException extends Exception{
		public PathFindingException(final String message){
			super(message);
		}
	}
}