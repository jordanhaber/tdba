package com.perfectpixel.android.tdba;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class RPGView extends SurfaceView {

	private Joystick j1;
	private Joystick j2;
	private RPGLoop loop;
	private SurfaceHolder holder;
	
	private TileGrid grid_tile;

	public RPGView(Context context) {
		super(context);
		
		loop = new RPGLoop(this);
		holder = getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {

			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}

			public void surfaceCreated(SurfaceHolder holder) {
				setGame();
				loop.setRun(true);
				loop.start();
			}

			public void surfaceDestroyed(SurfaceHolder holder) {
				boolean retry = true;
				loop.setRun(false);
				while (retry) {
					try {
						loop.join();
						retry = false;
					}catch (InterruptedException e) {
					}
				}
			}
		});
	}

	public void setGame() {
		
		Global.view = this;
		
		setSize();
		
		Global.player = new Player((Global.width / 2) - (Global.tile_size / 2), (Global.height / 2) - (Global.tile_size / 2));
		Global.map = new Map();
		Global.brain = new Brain();
		
		setUI();
		setTiles();
		
		Global.enemies.add(new Enemy(7,7));
	}

	public void setSize() {
		Global.width = this.getWidth();
		Global.height = this.getHeight();
	}
	
	/*public void newSize(int _size) {
		TILE_SIZE = _size;
		setTiles();
		player.setSize(_size);
		map.setSize(_size);
		map.setTiles(tiles);
		for (int i = 0; i < effects.size(); i++) {
			effects.get(i).setSize(_size);
		}
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).setSize(_size);
		}
	}*/

	public void setUI() {
		
		//Joysticks
		int j_size = 150;
		int j_padding = 30;
		j1 = new Joystick(j_padding, Global.height - j_size - j_padding, j_size);
		j2 = new Joystick(Global.width - j_size - j_padding, Global.height - j_size - j_padding, j_size);
		
		//Inventory display
		Global.bag_x = Global.width - (j_size / 2) - j_padding;
		Global.bag = new Bag();
		
		//Health display
		Global.health_x = (j_size / 2) + j_padding;
		Global.health = new Health();
		
	}

	public void setTiles() {
		
		//Add tiles to arraylist
		Global.tiles.clear();

		Global.tiles.add(new Tile("blank", 999, true, "none", Color.argb(0, 0, 0, 0)));
		
		//textures
		Global.tiles.add(new Tile("water", 999, false, "texture", Color.rgb(0, 0, 200)));
		Global.tiles.add(new Tile("puddle", 999, true, "texture", Color.rgb(135, 206, 250)));
		Global.tiles.add(new Tile("sand", 999, true, "texture", Color.rgb(250, 250, 210)));
		Global.tiles.add(new Tile("grass", 999, true,"texture", Color.rgb(50, 205, 50)));
		Global.tiles.add(new Tile("dirt", 999, true, "texture", Color.rgb(160, 82, 45)));
		Global.tiles.add(new Tile("snow", 999, true, "texture", Color.rgb(255, 255, 255)));
		
		//blocks
		Global.tiles.add(new Tile("block_brick", 30, false, "wall", Color.rgb(200, 0, 0)));
		Global.tiles.add(new Tile("block_stone", 30, false, "wall", Color.GRAY));
		Global.tiles.add(new Tile("block_wood", 15, false, "wall", Color.rgb(189, 183, 107)));
		
		Global.tiles.add(new Tile("spawner", 100, false, "wall", Color.argb(255, 50, 50, 50)));
		
		//scenery
		Global.tiles.add(new Tile("scene_tree", 30, false, "scenery", Color.rgb(200, 0, 0)));
		Global.tiles.add(new Tile("scene_rock", 30, false, "scenery", Color.rgb(200, 0, 0)));
		Global.tiles.add(new Tile("scene_rock2", 30, false, "scenery", Color.rgb(200, 0, 0)));
		Global.tiles.add(new Tile("scene_grass", 30, true, "scenery", Color.argb(0, 0, 0, 0)));
		Global.tiles.add(new Tile("scene_pebbles", 30, true, "scenery", Color.argb(0, 0, 0, 0)));
		Global.tiles.add(new Tile("scene_tree2", 30, false, "scenery", Color.argb(0, 0, 0, 0)));
		Global.tiles.add(new Tile("scene_tree3", 30, false, "scenery", Color.argb(0, 0, 0, 0)));
		Global.tiles.add(new Tile("scene_flower", 30, true, "scenery", Color.argb(0, 0, 0, 0)));
		Global.tiles.add(new Tile("scene_flower2", 30, true, "scenery", Color.argb(0, 0, 0, 0)));
		
		Global.tiles.get(15).setGround(true);
		Global.tiles.get(18).setGround(true);
		Global.tiles.get(19).setGround(true);
				
		Global.tiles.get(11).setIMG1(BitmapFactory.decodeResource(Global.view.getResources(), R.drawable.scene_tree));
		Global.tiles.get(11).setIMG2(BitmapFactory.decodeResource(Global.view.getResources(), R.drawable.scene_tree3));
		Global.tiles.get(12).setIMG1(BitmapFactory.decodeResource(Global.view.getResources(), R.drawable.scene_rock));
		Global.tiles.get(13).setIMG1(BitmapFactory.decodeResource(Global.view.getResources(), R.drawable.scene_rock2));
		Global.tiles.get(14).setIMG2(BitmapFactory.decodeResource(Global.view.getResources(), R.drawable.scene_grass));
		Global.tiles.get(15).setIMG1(BitmapFactory.decodeResource(Global.view.getResources(), R.drawable.scene_pebbles));
		Global.tiles.get(16).setIMG1(BitmapFactory.decodeResource(Global.view.getResources(), R.drawable.scene_tree));
		Global.tiles.get(16).setIMG2(BitmapFactory.decodeResource(Global.view.getResources(), R.drawable.scene_tree2));
		Global.tiles.get(17).setIMG1(BitmapFactory.decodeResource(Global.view.getResources(), R.drawable.scene_tree));
		Global.tiles.get(17).setIMG2(BitmapFactory.decodeResource(Global.view.getResources(), R.drawable.scene_tree4));
		Global.tiles.get(18).setIMG1(BitmapFactory.decodeResource(Global.view.getResources(), R.drawable.scene_flower));
		Global.tiles.get(19).setIMG1(BitmapFactory.decodeResource(Global.view.getResources(), R.drawable.scene_flower2));
		
		grid_tile = new TileGrid( "grid_tile", false, false,	Color.argb(0, 0, 0, 0));
	}

	public void spawnEnemy(int _x, int _y, int _level) {
		
		if (Global.enemies.size() < Global.enemy_max) {
			int tmp_x = _x * Global.tile_size;
			int tmp_y = _y * Global.tile_size;
			if (Global.map.getWalk("right", tmp_x, _y, _level)) {
				tmp_x = _x + 1;
			} else if (Global.map.getWalk("left", _x, _y, _level)) {
				tmp_x = _x - 1;
			} else if (Global.map.getWalk("up", _x, _y, _level)) {
				tmp_y = _y - 1;
			} else if (Global.map.getWalk("down", _x, _y, _level)) {
				tmp_y = _y + 1;
			}

			//Enemy tmp = new Enemy(this, tmp_x, tmp_y);
			//Global.enemies.add(tmp);
		}
	}

	public void removeEnemy(int _x, int _y) {
		for (int i = 0; i < Global.enemies.size(); i++) {
			if (Global.enemies.get(i).getX() == _x) {
				if (Global.enemies.get(i).getY() == _y) {
					Global.enemies.remove(i);
				}
			}
		}
	}

	public void addEffect(int _x, int _y, int _color, int _life, int _level) {
		Global.effects.add(new Effect(_x * Global.tile_size, _y * Global.tile_size - Global.map.levelDelta(_level), _color, _life));
	}

	public void removeEffect(int _x, int _y, int _color) {
		for (int i = 0; i < Global.effects.size(); i++) {
			if (Global.effects.get(i).getX() == _x) {
				if (Global.effects.get(i).getY() == _y) {
					if (Global.effects.get(i).getColor() == _color) {
						Global.effects.remove(i);
					}
				}
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {

		//Store current joystick positions
		String direction = "";
		String direction_secondary = "";
		
		//If not placing object, check joysticks
		if (!Global.grid) {
			direction = j1.getDirection();
			direction_secondary = j2.getDirection();
		}

		//Pass through results of joysticks
		Global.player.setDirection(direction);
		if(Global.mode != 2) {
			boolean secondary_action = Global.map.setDamage(direction_secondary);
			if(!secondary_action)
				Global.player.setAttack(direction_secondary);
		}
		else {
			Global.player.setAttack(direction_secondary);
		}
		

		//Draw black canvas
		Global.paint.setStyle(Paint.Style.FILL);
		canvas.drawColor(Color.BLACK);
		
		//Draw map
		int[] restrict = Global.map.getDrawRestrict();
		
		//Draw each row of current level
		for(int l = 0; l < Global.map.maps.size(); l++) {
		
			//Check if under brick at level
			if(Global.map.getTile(Global.map.getCenterTile()[0],Global.map.getCenterTile()[1], l).getWall()) {
				Global.transparent = true;
			}
			else {
				Global.transparent = false;
			}
			
			for(int r = restrict[2]; r < restrict[3];  r++) {	
				
				//Draw grid if at current level, if active
				if (Global.grid && Global.grid_level == l) {
					Global.map.drawGridRow(canvas, r);
				}
				
				//Draw bricks
				Global.map.drawRow(canvas, l, r);
				
				//Draw scenery
				if(l == 3 && r <= Global.map.getCenterTile()[1]) {
					Global.map.drawSceneryRow(canvas, 1, r);
				}
				
				//Draw enemy in row
				for (int i = 0; i < Global.enemies.size(); i++) {
					if(Global.enemies.get(i).getY() == r && Global.enemies.get(i).getLevel() == l) {
						Global.enemies.get(i).onDraw(canvas);
					}
				}
				
				//Draw player
				if(Global.map.getCenterTile()[1] == r && Global.player.getLevel() == l) {
					Global.player.onDraw(canvas);
				}
				
				//Draw scenery
				if(l == 3 && r > Global.map.getCenterTile()[1]) {
					Global.map.drawSceneryRow(canvas, 1, r);
				}
			}
			
			//Draw projectile in row
			for (int i = 0; i < Global.projectiles.size(); i++) {
				if(Global.projectiles.get(i).getLevel() == l) {
					//if(Global.projectiles.get(i).getY() == r) {
						Global.projectiles.get(i).onDraw(canvas);
					//}
				}
			}
		}
		
		//Draw effects
		for (int i = 0; i < Global.effects.size(); i++) {
				Global.effects.get(i).onDraw(canvas);
		}
		
		//Draw global map components
		Global.map.onDraw(canvas);

		//Draw UI components
		Global.bag.onDraw(canvas);
		Global.health.onDraw(canvas);
		j1.onDraw(canvas, Global.paint);
		j2.onDraw(canvas, Global.paint);
		
		//Draw grid if active
		if (Global.grid)
			grid_tile.onDraw(canvas, Global.grid_level);
	
	}

	public void tileClick(int _type) {
		
		if (Global.bag.getPlacing()) {
			
			if (_type == 1) {
				Global.bag.setPlacing(false);
				Global.grid = false;
				grid_tile.setColor(Color.argb(0, 0, 0, 0));
			}
			
			if (_type == 2) {

				if (Global.map.getPlace(grid_tile.getGridX(), grid_tile.getGridY(), Global.grid_level)) {
					Global.map.placeTile(grid_tile.getGridX(), grid_tile.getGridY(), Global.grid_level, Integer.parseInt(grid_tile.getID()));
					Global.bag.setPlacing(false);
					Global.grid = false;
					grid_tile.setColor(Color.argb(0, 0, 0, 0));
					Global.player.getInventory().removeTile(Integer.parseInt(grid_tile.getID()), 1);
					Global.grid_level = 1;
				}
			}
			
			if (_type == 3) {
				Global.grid_level += 1;
			}
			
			if (_type == 4) {
				Global.grid_level -= 1;
			}
			
			if (Global.grid_level > Global.map.maps.size()-1) {
				Global.grid_level = Global.map.maps.size()-1;
			}
			
			if (Global.grid_level < 1) {
				Global.grid_level = 1;
			}
			
		} else {
			Global.bag.setPlacing(true);
			Global.grid = true;
			if(Global.tiles.get(_type).getIMG1() != null) {
				grid_tile.setIMG(Global.tiles.get(_type).getIMG1());
			}
			else {
				grid_tile.setIMG(null);
			}
			int color = Global.tiles.get(_type).getColor();
			grid_tile.setColor(color);
			grid_tile.setID(Integer.toString(_type));
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		int id = 999;
		int tile = Global.bag.resolveTouch(event);
		
		if (tile != 999)
			tileClick(tile);
		
		if (!Global.bag.getPlacing()) {
			
			int action = event.getAction();
			switch (action & MotionEvent.ACTION_MASK) {
			
				case MotionEvent.ACTION_DOWN:
					//id = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
					id = event.getPointerId(0);
					j1.pointerDown(id, event.getX(id), event.getY(id));
					j2.pointerDown(id, event.getX(id), event.getY(id));
					break;
					
				case MotionEvent.ACTION_POINTER_DOWN:
					id = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
					j1.pointerDown(id, event.getX(id), event.getY(id));
					j2.pointerDown(id, event.getX(id), event.getY(id));
					break;

				case MotionEvent.ACTION_UP:
					id = 999;
					//id = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
					id = event.getPointerId(0);
					j1.pointerUp(id, event.getX(id), event.getY(id));
					j2.pointerUp(id, event.getX(id), event.getY(id));
					break;
					
				case MotionEvent.ACTION_POINTER_UP:
					id = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
					j1.pointerUp(id, event.getX(id), event.getY(id));
					j2.pointerUp(id, event.getX(id), event.getY(id));
					break;
					
				case MotionEvent.ACTION_MOVE:
					for (int i = 0; i < event.getPointerCount(); i++) {
						j1.pointerMove(event.getPointerId(i), event.getX(i), event.getY(i));
						j2.pointerMove(event.getPointerId(i), event.getX(i), event.getY(i));
					}		
					break;
				
			}
		}

		else if (event.getAction() == MotionEvent.ACTION_UP && tile == 999) {
			
			int[] tmp = Global.map.pixelToGrid((int) event.getX(), (int) event.getY(), Global.grid_level);
			
			if (tmp[0] > 0 && tmp[1] >= 0) {
				if (Global.bag_x > Global.width / 2 && event.getX() < Global.bag_x) {
					grid_tile.setGridX(tmp[0]);
					grid_tile.setGridY(tmp[1]);
				}
				if (Global.bag_x < Global.width / 2 && event.getX() > Global.bag_x + Global.bag.getSize()) {
					grid_tile.setGridX(tmp[0]);
					grid_tile.setGridY(tmp[1]);
				}
			}
		}

		return true;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}
}
