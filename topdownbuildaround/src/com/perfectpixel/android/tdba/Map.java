package com.perfectpixel.android.tdba;

import java.util.ArrayList;
import java.io.*;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Map {

	public ArrayList <int[][]> maps = new ArrayList <int[][]>();
	
	int[][] map0;

	int[][] map1;
	
	int[][] map2;
	
	int[][] map3;

	private int player_x, player_y;
	private int center_x, center_y;
	private ArrayList<int[]> damaged_tiles = new ArrayList<int[]>();
	private int[] player_tile;
	private int counter = 0;
	private int spawn_counter = 30 * 5;

	public Map() {
		
		map0 = readMap();
		
		int[][] tmp_map = new int[map0.length][map0[0].length];
		for(int x=0; x < map0.length; x++) {
			for(int y=0; y < map0[0].length; y++) {
				tmp_map[x][y] = 0;
			}
		}
		
		int[][] tmp_map2 = new int[map0.length][map0[0].length];
		for(int x=0; x < map0.length; x++) {
			for(int y=0; y < map0[0].length; y++) {
				tmp_map2[x][y] = 0;
			}
		}
		
		int[][] tmp_map3 = new int[map0.length][map0[0].length];
		for(int x=0; x < map0.length; x++) {
			for(int y=0; y < map0[0].length; y++) {
				tmp_map2[x][y] = 0;
			}
		}
		
		map1 = (int[][]) tmp_map;
		map2 = (int[][]) tmp_map2;
		map3 = (int[][]) tmp_map3;
		
		Global.grid_width = map0.length;
		Global.grid_height = map0[0].length;
		
		maps.add(map0);
		maps.add(map1);
		maps.add(map2);
		maps.add(map3);
		
		/*placeTile(3,3,1,8);
		placeTile(3,3,2,8);
		placeTile(3,3,3,8);
		placeTile(3,4,1,9);
		placeTile(4,4,1,10);
		placeTile(5,4,1,10);
		placeTile(3,4,2,9);*/
		//placeTile(6,6,1,12);
		//placeTile(7,7,1,12);
		//placeTile(6,7,1,13);
		genScenery(0);
		
		placeBuilding(5, 5, genBuilding(8,8,2), 0);
		
		updatePlayer();
		player_tile = getCenterTile();
	}
	
	
	public int[][] readMap() {
		
		int[][] tmp_map;
		
		try{
		
			InputStream is = Global.active.getResources().openRawResource(R.raw.test2);;
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
			String line;
			
			if (is!=null) {							
			
				line = br.readLine();
				int tmp_x = Integer.parseInt(line);
				line = br.readLine();
				int tmp_y = Integer.parseInt(line);
			
				tmp_map = new int[tmp_x][tmp_y];
			
				int cur_x = 0;
			
				while ((line = br.readLine()) != null)   {
				
					String[] items = line.split(" ");
				
					for (int y=0; y < items.length; y++) {
						tmp_map[cur_x][y] = Integer.parseInt(items[y]);
					}
				
					cur_x++;
				
				}
				
				return tmp_map;
	
			}
				
			is.close();
			br.close();
			
		}
		
		catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
		
		return null;
	}
	
	public void placeBuilding(int _x, int _y, int[][] _building, int _type) {
		
		if(_type == 0) {
			_type = (int) Math.ceil(Math.random()*3);
			_type += 6;
		}
		
		for(int x=0; x < _building.length; x++) {
			for(int y=0; y < _building[0].length; y++) {
				//wall
				if(_building[x][y] == 1) {
					placeTile(_x+x,_y+y,1,_type);
					placeTile(_x+x,_y+y,2,_type);
					placeTile(_x+x,_y+y,3,_type);
				}
				//floor
				if(_building[x][y] == 2) {
					placeTile(_x+x,_y+y,1,0);
					//placeTile(_x+x,_y+y,3,_type);
				}
				//door
				if(_building[x][y] == 3) {
					placeTile(_x+x,_y+y,1,0);
					placeTile(_x+x,_y+y,3,_type);
				}
			}
		}
	}
	
	public int[][] genBuilding(int _x, int _y, int _rooms) {
		
		int min_x = 4;
		int min_y = 4;
		
		int rand_x = (int) (Math.random()*(_x - min_x)) + min_x;
		int rand_y = (int) (Math.random()*(_y - min_y)) + min_y;
		
		int[][] room = genRoom(rand_x,rand_y);
		
		int rand2_x = (int) (Math.random()*(_x - min_x)) + min_x;
		int rand2_y = (int) (Math.random()*(_y - min_y)) + min_y;
		
		int[][] room2 = genRoom(rand2_x,rand2_y);
		
		int rand_place = (int) Math.ceil(Math.random()*4);
		
		int[][] tmp_building;
		
		if (rand_place == 1 || rand_place == 3) {
			if(rand_x > rand2_x) {
				tmp_building = new int[rand_x][rand_y+rand2_y-1];
			}
			else {
				tmp_building = new int[rand2_x][rand_y+rand2_y-1];
			}
		}
		else {
			if(rand_y > rand2_y) {
				tmp_building = new int[rand_x+rand2_x-1][rand_y];
			}
			else {
				tmp_building = new int[rand_x+rand2_x-1][rand2_y];
			}
		}
		
		for(int x = 0; x < tmp_building.length; x++) {
			for(int y = 0; y < tmp_building[0].length; y++) {
				tmp_building[x][y] = 0;
			}
		}
		
		//place right and down
		if(rand_place == 2 || rand_place == 3) {
			for(int x = 0; x < rand_x; x++) {
				for(int y = 0; y < rand_y; y++) {
					tmp_building[x][y] = room[x][y];
				}
			}
			
			//place right
			if(rand_place == 2) {
				for(int x = 0; x < rand2_x; x++) {
					for(int y = 0; y < rand2_y; y++) {
						if(tmp_building[x+rand_x-1][y] == 1 && y > 0 && y < rand2_y-1 && y != rand_y-1) {
							tmp_building[x+rand_x-1][y] = 2;
						}
						else {
							tmp_building[x+rand_x-1][y] = room2[x][y];
						}
					}
				}
				
				tmp_building[(int) Math.floor(rand2_x/2)][rand_y-1] = 3;
			}
			
			//place down
			if(rand_place == 3) {
				for(int x = 0; x < rand2_x; x++) {
					for(int y = 0; y < rand2_y; y++) {
						if(tmp_building[x][y+rand_y-1] == 1 && x > 0 && x < rand2_x-1 && y != rand_x-1) {
							tmp_building[x][y+rand_y-1] = 2;
						}
						else {
							tmp_building[x][y+rand_y-1] = room2[x][y];
						}
					}
				}
				
				tmp_building[(int) Math.floor(rand2_x/2)][rand_y+rand2_y-2] = 3;
			}
		}
		
		//place above and left
		if(rand_place == 1 || rand_place == 4) {
			for(int x = 0; x < rand2_x; x++) {
				for(int y = 0; y < rand2_y; y++) {
					tmp_building[x][y] = room2[x][y];
				}
			}
			
			//place left
			if(rand_place == 4) {
				for(int x = 0; x < rand_x; x++) {
					for(int y = 0; y < rand_y; y++) {
						if(tmp_building[x+rand2_x-1][y] == 1 && y > 0 && y < rand_y-1 && y != rand2_y-1) {
							tmp_building[x+rand2_x-1][y] = 2;
						}
						else {
							tmp_building[x+rand2_x-1][y] = room[x][y];
						}
					}
				}
				tmp_building[(int) Math.floor(rand2_x/2)][rand2_y-1] = 3;
			}
			
			//place up
			if(rand_place == 1) {
				for(int x = 0; x < rand_x; x++) {
					for(int y = 0; y < rand_y; y++) {
						if(tmp_building[x][y+rand2_y-1] == 1 && x > 0 && x < rand_x-1 && y != rand2_x-1) {
							tmp_building[x][y+rand2_y-1] = 2;
						}
						else {
							tmp_building[x][y+rand2_y-1] = room[x][y];
						}
					}
				}
				
				tmp_building[(int) Math.floor(rand_x/2)][rand_y+rand2_y-2] = 3;
			}
		}
		
		return tmp_building;
	}
	
	public int[][] genRoom(int _x, int _y) {
		
		int[][] tmp_room = new int[_x][_y];
		
		for(int x = 0; x < _x; x++) {
			for(int y = 0; y < _y; y++) {
				tmp_room[x][y] = 2;
			}
		}
		
		for(int x = 0; x < _x; x++) {
			tmp_room[x][0] = 1;
			tmp_room[x][_y-1] = 1;
		}
		
		for(int y = 1; y < _y-1; y++) {
			tmp_room[0][y] = 1;
			tmp_room[_x-1][y] = 1;
		}
		
		return tmp_room;
	}
	
	public void genScenery(int _level) {
		for(int x=0; x < map0.length; x++) {
			for(int y=0; y < map0[0].length; y++) {
				if(getTileNum(x, y, _level)==4) {
					double chance = Math.random()*15;
					if (chance <= .2) {
						placeTile(x, y, _level+1, 11);
					}
					else if (chance <= .6) {
						placeTile(x, y, _level+1, 16);
					}
					else if (chance <= 1) {
						placeTile(x, y, _level+1, 17);
					}
					else if (chance <= 1.5) {
						placeTile(x, y, _level+1, 15);
					}
					else if (chance <= 1.8) {
						placeTile(x, y, _level+1, 12);
					}
					else if (chance <= 2) {
						placeTile(x, y, _level+1, 13);
					}
					else if (chance <= 2.1) {
						placeTile(x, y, _level+1, 18);
					}
					else if (chance <= 2.2) {
						placeTile(x, y, _level+1, 19);
					}
					//else if (chance <= 3) {
						//placeTile(x, y, _level+1, 15);
					//}
				}
			}
		}
	}

	public boolean getExists(int _x, int _y) {
		if(0 <= _x && _x < Global.grid_width) {
			if(0 <= _y && _y < Global.grid_height) {
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}
	
	public int[] pixelToGrid(int _x, int _y, int _level) {
		_y += levelDelta(_level);
		int tmp_x, tmp_y;
		tmp_x = (int) Math.floor((_x - center_x) / (float) Global.tile_size);
		tmp_y = (int) Math.floor((_y - center_y) / (float) Global.tile_size);
		tmp_x = getCenterTile()[0] + tmp_x;
		tmp_y = getCenterTile()[1] + tmp_y;
		return new int[] { tmp_x, tmp_y };
	}

	public int[] gridToPixel(int _x, int _y, int _level) {
		int tmp_x = center_x + (_x * Global.tile_size) - player_x;
		int tmp_y = center_y + (_y * Global.tile_size) - player_y;
		return new int[] { tmp_x, tmp_y };
	}

	public int levelDelta(int _level) {
		int tmp_y = 0;
		if (_level > 1)
			tmp_y = (int) (((_level-1)/2.0)*Global.tile_size);
		return tmp_y;
	}
	
	public boolean getPlace(int _x, int _y, int _level) {
		if (maps.get(_level)[_y][_x] == 0) {
			return true;
		}
		return false;
	}

	public void placeTile(int _x, int _y, int _level, int _input) {
		maps.get(_level)[_y][_x] = _input;
	}

	public boolean getWalk(String _direction, int _x, int _y, int _level) {
		try {
			if (_direction.equals("up")) {
				return Global.tiles.get(maps.get(_level)[(_y / Global.tile_size) - 1][_x / Global.tile_size]).getWalk();
			} else if (_direction.equals("right")) {
				return Global.tiles.get(maps.get(_level)[_y / Global.tile_size][(_x / Global.tile_size) + 1]).getWalk();
			} else if (_direction.equals("down")) {
				return Global.tiles.get(maps.get(_level)[(_y / Global.tile_size) + 1][_x / Global.tile_size]).getWalk();
			} else if (_direction.equals("left")) {
				return Global.tiles.get(maps.get(_level)[_y / Global.tile_size][(_x / Global.tile_size) - 1]).getWalk();
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public int[] getCenterTile() {
		
		int tmp_x;
		int tmp_y;
		if(Global.player.getMoving()) {
			tmp_x = (int) Math.round(Global.player.getX1() / Global.tile_size);
			tmp_y = (int) Math.round(Global.player.getY1() / Global.tile_size);
		}
		else {
			tmp_x = (int) Math.round(player_x / Global.tile_size);
			tmp_y = (int) Math.round(player_y / Global.tile_size);
		}
		int[] tmp = { tmp_x, tmp_y };
		return tmp;
	}

	public Tile getTile(int _x, int _y, int _level) {
		return Global.tiles.get(maps.get(_level)[_y][_x]);
	}
	
	public int getTileNum(int _x, int _y, int _level) {
		return maps.get(_level)[_y][_x];
	}

	public boolean setDamage(String _direction) {
		
		int[] tmp = getCenterTile();
		
		//for (int i = 0; i < Global.player.getLevel()+Global.player.getReach(); i++) {
		for (int i = Global.player.getLevel()+Global.player.getReach(); i >= Global.player.getLevel()-1; i--) {
		
			if (Global.player.getLevel()+i < maps.size()) {
			
				if (_direction.equals("up")) {
					if (!getPlace(tmp[0], tmp[1] - 1, Global.player.getLevel()+i)) {
						damage(tmp[0], tmp[1] - 1, Global.player.getLevel()+i, Global.player.getDamage());
						return true;
					}
				}	
				if (_direction.equals("down")) {
					if (!getPlace(tmp[0], tmp[1] + 1, Global.player.getLevel()+i)) {
						damage(tmp[0], tmp[1] + 1, Global.player.getLevel()+i, Global.player.getDamage());
						return true;
					}
				}
				if (_direction.equals("left")) {
					if (!getPlace(tmp[0] - 1, tmp[1], Global.player.getLevel()+i)) {
						damage(tmp[0] - 1, tmp[1], Global.player.getLevel()+i, Global.player.getDamage());
						return true;
					}
				}
				if (_direction.equals("right")) {
					if (!getPlace(tmp[0] + 1, tmp[1], Global.player.getLevel()+i)) {
						damage(tmp[0] + 1, tmp[1], Global.player.getLevel()+i, Global.player.getDamage());
						return true;
					}
				}
			}
		}
		
		return false;
	}

	public void damage(int _x, int _y, int _level, int _damage) {
		int damage = _damage;
		int curr_dmg = 0;
		int index = 0;
		boolean existing = false;
		for (int i = damaged_tiles.size()-1; i > 0; i--) {
			int[] tmp = damaged_tiles.get(i);
			if (tmp[0] == _x && tmp[1] == _y && tmp[2] == _level) {
				tmp[3] = damage + tmp[3];
				curr_dmg = tmp[3];
				damaged_tiles.remove(i);
				damaged_tiles.add(tmp);
				index = i;
				existing = true;
				break;
			}
		}
		if (!existing) {
			int[] tmp = new int[] { _x, _y, _level, damage };
			damaged_tiles.add(tmp);
		}

		if (curr_dmg >= Global.tiles.get(maps.get(_level)[_y][_x]).getLife()) {
			Global.player.getInventory().addTile(maps.get(_level)[_y][_x], 1);
			placeTile(_x, _y, _level, 0);
			damaged_tiles.remove(index);
		}
				
		Global.view.addEffect(_x, _y, Color.argb(100, 255, 255, 255), 2, _level);
	}

	public void updatePlayer() {
		player_x = Global.player.getX();
		player_y = Global.player.getY();
		center_x = Global.player.getDrawX();
		center_y = Global.player.getDrawY();
	}

	public int[] getDrawRestrict() {

		int safe = 3;

		int min_x = (int) (getCenterTile()[0] - Math.floor((Global.width / 2) / Global.tile_size) - safe);
		int max_x = (int) (getCenterTile()[0] + Math.ceil((Global.width / 2) / Global.tile_size) + safe);
		int min_y = (int) (getCenterTile()[1] - Math.floor((Global.height / 2) / Global.tile_size) - safe);
		int max_y = (int) (getCenterTile()[1] + Math.ceil((Global.height / 2) / Global.tile_size) + safe);

		int[] tmp = new int[] { min_x, max_x, min_y, max_y };
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i] < 0)
				tmp[i] = 0;
			if (tmp[i] > map0[0].length && i < 2)
				tmp[i] = map0[0].length;
			if (tmp[i] > map0.length && i > 1)
				tmp[i] = map0.length;
		}
		return tmp;
	}

	public void onDraw(Canvas c) {

		counter++;
		updatePlayer();

		//placeTile(player_tile[0], player_tile[1], Global.player.getLevel(), 0);
		
		player_tile = getCenterTile();
		/*
		for(int i=0; i<Global.tiles.size(); i++) {
			if(Global.tiles.get(i).getID().equals("wall_blank")) {
				placeTile(player_tile[0], player_tile[1], Global.player.getLevel(), i);
				break;
			}
		}
		
		for (int y = 0; y < map1.length; y++) {
			for (int x = 0; x < map1[y].length; x++) {
				if (Global.tiles.get(map1[y][x]).getID().equals("spawner")) {
					if (counter % spawn_counter == 0) {
						Global.view.spawnEnemy(x, y, 0);
					}
				}
			}
		}
		*/
	}
	
	public void drawRow(Canvas c, int _level, int _row) {
		int[] restrict = getDrawRestrict();
		
		for (int x = restrict[0]; x < restrict[1]; x++) {
				
			int draw_x = Global.map.gridToPixel(x,_row,_level)[0];
			int draw_y = Global.map.gridToPixel(x,_row,_level)[1];

			draw_y -= levelDelta(_level);
				
			Tile tmp = getTile(x, _row, _level);
			tmp.onDraw(c, draw_x, draw_y, x, _row, _level);
		}
	}
		
	public void drawSceneryRow(Canvas c, int _level, int _row) {
		
		int[] restrict = getDrawRestrict();
		
		for (int x = restrict[0]; x < restrict[1]; x++) {
				
			int draw_x = Global.map.gridToPixel(x,_row,_level)[0];
			int draw_y = Global.map.gridToPixel(x,_row,_level)[1];

			draw_y -= levelDelta(_level);
				
			Tile tmp = Global.tiles.get(maps.get(_level)[_row][x]);
			if(tmp.getScenery()) {
				tmp.drawScenery(c, draw_x, draw_y, x, _row, _level);
			}
		}
	}

	public void drawGridRow(Canvas c, int _row) {
		
		int[] restrict = getDrawRestrict();
		
		for (int x = restrict[0]; x < restrict[1]; x++) {
			
			int draw_x = Global.map.gridToPixel(x,_row,0)[0];
			int draw_y = Global.map.gridToPixel(x,_row,0)[1];
			
			draw_y -= levelDelta(Global.grid_level);

			Global.paint.setColor(Color.argb(150, 255, 255, 255));
			c.drawRect(draw_x, draw_y, draw_x + Global.tile_size, draw_y + Global.tile_size, Global.paint);

			Global.paint.setStyle(Paint.Style.STROKE);
			Global.paint.setColor(Color.argb(80, 0, 0, 0));
			c.drawRect(draw_x, draw_y, draw_x + Global.tile_size, draw_y + Global.tile_size, Global.paint);

			Global.paint.setStyle(Paint.Style.FILL);
			Global.paint.setColor(Color.WHITE);
			
		}
	}
}
