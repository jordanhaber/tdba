package com.perfectpixel.android.tdba;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

public class Tile {

	private boolean walk, wall, texture, scenery, ground;
	private int color, life;
	private String id;
	private Bitmap img1 = null;
	private Bitmap img2 = null;
	private Bitmap img1_50 = null;
	private Bitmap img2_50 = null;
	private Bitmap img1_75 = null;
	private Bitmap img2_75 = null;

	public Tile(String _id, int _life, boolean _walk, String _type, int _color) {
		id = _id;
		walk = _walk;
		
		wall = false;
		texture = false;
		scenery = false;
		
		if(_type == "wall") {
			wall = true;
		}
		if(_type == "texture") {
			texture = true;
		}
		if(_type == "scenery") {
			scenery = true;
		}
			
		color = _color;
		life = _life;
		ground = false;
		setIMGS();
	}

	public void setIMG1(Bitmap _img) {
		img1 = _img;
	}
	
	public void setIMG2(Bitmap _img) {
		img2 = _img;
	}
	
	public void setIMGS() {
		if(wall) {
			int tmp_id = Global.view.getResources().getIdentifier(id, "drawable", Global.package_name);
			img1 = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
		
			tmp_id = Global.view.getResources().getIdentifier(id+"2", "drawable", Global.package_name);
			img2 = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
			
			tmp_id = Global.view.getResources().getIdentifier(id+"_50", "drawable", Global.package_name);
			img1_50 = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
		
			tmp_id = Global.view.getResources().getIdentifier(id+"2_50", "drawable", Global.package_name);
			img2_50 = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
			
			tmp_id = Global.view.getResources().getIdentifier(id+"_75", "drawable", Global.package_name);
			img1_75 = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
		
			tmp_id = Global.view.getResources().getIdentifier(id+"2_75", "drawable", Global.package_name);
			img2_75 = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
		}
		
		if(texture) {
			int tmp_id = Global.view.getResources().getIdentifier(id, "drawable", Global.package_name);
			img1 = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
			
			Bitmap[] imgs = new Bitmap[4];
			
			tmp_id = Global.view.getResources().getIdentifier(id+"_left", "drawable", Global.package_name);
			imgs[0] = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
			
			tmp_id = Global.view.getResources().getIdentifier(id+"_right", "drawable", Global.package_name);
			imgs[1] = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
			
			tmp_id = Global.view.getResources().getIdentifier(id+"_up", "drawable", Global.package_name);
			imgs[2] = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
			
			tmp_id = Global.view.getResources().getIdentifier(id+"_down", "drawable", Global.package_name);
			imgs[3] = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
			
			Global.fades.put(id, imgs);
		}
	}

	public void setWall(boolean _input) {
		wall = _input;
	}

	public void setGround(boolean _input) {
		ground = _input;
	}
	
	public void setColor(int _color) {
		color = _color;
	}

	public void setID(String _id) {
		id = _id;
	}

	public boolean getWall() {
		return wall;
	}

	public boolean getWalk() {
		return walk;
	}

	public String getID() {
		return id;
	}

	public int getColor() {
		return color;
	}
	
	public Bitmap getIMG1() {
		return img1;
	}
	
	public Bitmap getIMG2() {
		return img2;
	}

	public int getLife() {
		return life;
	}
	
	public boolean getTexture() {
		return texture;
	}
	
	public boolean getScenery() {
		return scenery;
	}

	public void onDraw(Canvas c, int _x, int _y, int _grid_x,	int _grid_y, int _level) {
		
		Global.paint.setColor(color);

		int distance = 999;
		
		if(Global.transparent && wall) {
			int distance_x = Math.abs(_grid_x - Global.map.getCenterTile()[0]);
			int distance_y = Math.abs(_grid_y - Global.map.getCenterTile()[1] - 1);
			distance = distance_x+distance_y;
		}
		
		//if wall and no wall below
		if (wall && !Global.map.getTile(_grid_x, _grid_y + 1, _level).getWall()) {
			//draw if image exists
			if (img2 != null) {
				if(distance == 1 || distance == 0) {
					c.drawBitmap(img2_50, _x, _y, null);
				}
				else if(distance == 2) {
					c.drawBitmap(img2_75, _x, _y, null);
				}
				else {
					c.drawBitmap(img2, _x, _y, null);
				}
			} 
		
			//draw if no image
			else {
				Global.paint.setColor(Color.BLACK);
				c.drawRect(_x, _y, _x + Global.tile_size, _y + Global.tile_size, Global.paint);
				Global.paint.setColor(color);
				c.drawRect(_x, _y, _x + Global.tile_size, _y + (Global.tile_size / 2), Global.paint);
				Global.paint.setAlpha(Global.paint.getAlpha() - 80);
				c.drawRect(_x, _y + (Global.tile_size / 2), _x + Global.tile_size, _y + Global.tile_size, Global.paint);
			}
			
			if(_level == 1) {
				drawFooter(_grid_x, _grid_y, _x, _y, c);
			}
		} 
		
		//otherwise, draw as normal tile
		else {
			if (img1 != null) {
				
				if(wall) {
					if(distance == 1 || distance == 0) {
						c.drawBitmap(img1_50, _x, _y, null);
					}
					else if(distance == 2) {
						c.drawBitmap(img1_75, _x, _y, null);
					}
					else {
						c.drawBitmap(img1, _x, _y, null);
					}
				}
				else {
					c.drawBitmap(img1, _x, _y, null);
				}
			} 
			else {
				c.drawRect(_x, _y, _x + Global.tile_size, _y + Global.tile_size, Global.paint);
			}
		}
		
		if(texture) {
			drawTexture(c, _x, _y, _grid_x, _grid_y, _level);	
		}
		
		if(scenery) {
			if(!ground) {
				drawFooter(_grid_x, _grid_y, _x, _y, c);
			}
		}
	}
	
	public void drawFooter(int _grid_x, int _grid_y, int _x, int _y, Canvas c) {
		if(_grid_y<Global.grid_height-1) {
			int num1 = Global.map.getTileNum(_grid_x, _grid_y, 0);
			int num2 = Global.map.getTileNum(_grid_x, _grid_y+1, 0);
			Bitmap[] _img;
			if(num1 > num2) {
				_img = Global.fades.get(Global.map.getTile(_grid_x, _grid_y+1, 0).getID());
			}
			else {
				_img = Global.fades.get(Global.map.getTile(_grid_x, _grid_y, 0).getID());
			}
			c.drawBitmap(_img[3], _x, _y, null);
		}
		else {
			Bitmap[] _img = Global.fades.get(Global.map.getTile(_grid_x, _grid_y, 0).getID());
			c.drawBitmap(_img[3], _x, _y, null);
		}
	}
	
	public void drawTexture(Canvas c, int _x, int _y, int _grid_x, int _grid_y, int _level) {
		
		try {
			Bitmap[] _img;
			int num = Global.map.getTileNum(_grid_x, _grid_y, _level);
			
			if(_grid_x>0) {
				if(Global.map.getTileNum(_grid_x-1, _grid_y, _level) < num) {
					_img = Global.fades.get(Global.map.getTile(_grid_x-1, _grid_y, _level).getID());
					c.drawBitmap(_img[0], _x, _y, null);
				}
			}
			if(_grid_x<Global.grid_width-1) {
				if(Global.map.getTileNum(_grid_x+1, _grid_y, _level) < num) {
					_img = Global.fades.get(Global.map.getTile(_grid_x+1, _grid_y, _level).getID());
					c.drawBitmap(_img[1], _x, _y, null);
				}
			}
			if(_grid_y>0) {
				if(Global.map.getTileNum(_grid_x, _grid_y-1, _level) < num) {
					_img = Global.fades.get(Global.map.getTile(_grid_x, _grid_y-1, _level).getID());
					c.drawBitmap(_img[2], _x, _y, null);
				}
			}
			if(_grid_y<Global.grid_height-1) {
				if(Global.map.getTileNum(_grid_x, _grid_y+1, _level) < num) {
					_img = Global.fades.get(Global.map.getTile(_grid_x, _grid_y+1, _level).getID());
					c.drawBitmap(_img[3], _x, _y, null);
				}
			}
		}
		catch (Exception e) {
		}
		
	}
	
	public void drawScenery(Canvas c, int _x, int _y, int _grid_x, int _grid_y, int _level) {
		if(img2 != null) {
			int tmp_x =_x-(img2.getWidth()/2)+(Global.tile_size/2);
			int tmp_y =_y-(img2.getHeight()/2)+(Global.tile_size/2);
			c.drawBitmap(img2, tmp_x, tmp_y-Global.tile_size, null);
		}
	}

	public void drawInventory(Canvas c, int _x, int _y, int _size, int _bag_width) {

		int center_x = (_size - img1.getWidth())/2; 
		
		if (img1 != null) {
			if(center_x > 0) {
				c.drawBitmap(img1, _x+center_x, _y+center_x, null);
			}
			else {
				c.drawBitmap(img1, _x, _y, null);
			}
		}
		else {
			Global.paint.setColor(color);
			c.drawRect(_x, _y, _x + _size, _y + _size, Global.paint);
		}
	}
}
