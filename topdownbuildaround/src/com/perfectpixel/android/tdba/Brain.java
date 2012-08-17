package com.perfectpixel.android.tdba;

import java.util.ArrayList;

public class Brain {

	public Brain() {
		
	}
	
	public String moveCoordsSimple(int _x, int _y, int _goal_x, int _goal_y) {

		String direction = "";
		
		//Distances from goal
		int distance_x = Math.abs(_x-_goal_x);
		int distance_y = Math.abs(_y-_goal_y);
		
		//Check if at goal
		if(distance_x+distance_y <= 1)
			return direction;
		
		//Move by x if greater distance
		if (distance_x > distance_y) {
			if(_x-_goal_x > 0)
				direction = "left";
			else
				direction = "right";
		}
		//Otherwise move by y
		else  {
			if(_y-_goal_y > 0)
				direction = "up";
			else
			direction = "down";
		}
		return direction;
	}
	
	public String moveCoords(int _x, int _y, int _goal_x, int _goal_y) {

		String direction = "";
		
		//Distances from goal
		int distance_x = Math.abs(_x-_goal_x);
		int distance_y = Math.abs(_y-_goal_y);
		int distance_goal = distance_x+distance_y;
		
		//Check if at goal
		if(distance_x+distance_y <= 1)
			return direction;
		
		//Beginning node
		SearchNode node_start = new SearchNode();
		node_start.x = _x;
		node_start.y = _y;
		node_start.cost = 0;
		node_start.estimate = distance_goal;
		node_start.parent = null;
		
		//End node
		SearchNode node_goal = new SearchNode();
		node_goal.x = _goal_x;
		node_goal.y = _goal_y;
		
		//Searched paths
		ArrayList<SearchNode> node_open = new ArrayList<SearchNode>();
		ArrayList<SearchNode> node_closed = new ArrayList<SearchNode>();
		
		//Node to search and expand
		node_open.add(node_start);
		SearchNode node = node_start;
		int node_index = 0;
		
		//While not goal node
		while((node.x != node_goal.x || node.y != node_goal.y) && node.cost < 5) {
			
			//Expand current node
			ArrayList<SearchNode> node_expand = expandNodes(node); 
			
			//for each expanded node
			for (int i = 0; i < node_expand.size(); i++) {
				//check if walkable
				if(Global.map.getTile(node_expand.get(i).x, node_expand.get(i).y, 0).getWalk()) 
					node_expand.get(i).cost += 100;
					//set estimate
					int tmp_x = Math.abs(node_expand.get(i).x - node_goal.x);
					int tmp_y = Math.abs(node_expand.get(i).y - node_goal.y);
					node_expand.get(i).estimate = tmp_x+tmp_y+node_expand.get(i).cost;
					//add to list
					node_open.add(node_expand.get(i));
				
			}
			
			node_closed.add(node_open.get(node_index));
			node_open.remove(node_index);
			node = node_open.get(0);
			
			for  (int i = 0; i < node_open.size(); i++) {
				if(node_open.get(i).estimate <= node.estimate) {
					if(node_open.get(i).cost > node.cost) {
					node = node_open.get(i);
					node_index = i;
					}
					}
			}
		}
		
		direction = "";
		while (node.parent != null) {
			direction = node.direction;
			node = node.parent;
		}
		
		return direction;
	}
	
	
	public ArrayList<SearchNode> expandNodes(SearchNode _node) {
		
		ArrayList<SearchNode> nodes = new ArrayList<SearchNode>();
		
		nodes.add(new SearchNode());
		nodes.get(0).x = _node.x;
		nodes.get(0).y = _node.y-1;
		nodes.get(0).direction = "up";
		nodes.get(0).cost = _node.cost + 1;
		nodes.get(0).parent = _node;
		
		nodes.add(new SearchNode());
		nodes.get(1).x = _node.x+1;
		nodes.get(1).y = _node.y;
		nodes.get(1).direction = "right";
		nodes.get(1).cost = _node.cost + 1;
		nodes.get(1).parent = _node;
		
		nodes.add(new SearchNode());
		nodes.get(2).x = _node.x;
		nodes.get(2).y = _node.y+1;
		nodes.get(2).direction = "down";
		nodes.get(2).cost = _node.cost + 1;
		nodes.get(2).parent = _node;
		
		nodes.add(new SearchNode());
		nodes.get(3).x = _node.x-1;
		nodes.get(3).y = _node.y;
		nodes.get(3).direction = "left";
		nodes.get(3).cost = _node.cost + 1;
		nodes.get(3).parent = _node;
		
		return nodes;	
		
	}
}



