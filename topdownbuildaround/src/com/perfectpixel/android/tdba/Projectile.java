package com.perfectpixel.android.tdba;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

public class Projectile {
	
	static int id_set = 0;
	
	private Bitmap img = null;
	private String type = "";
	private String direction = "";
	private int speed;
	private int damage;
	private int range;
	private int distance = 0;
	private int x, y = 0;
	private int x1,y1 = 0;
	private int move_x, move_y = 0;
	private int anim_y = 0;
	private int anim_w = 40;
	private int anim_h = 60;
	private int level = 1;
	private boolean moving = false;
	private int id = 0;
	private Rect rect_single = null;
	
	public Projectile(String _type, String _direction, int _speed, int _damage, int _range, int _x, int _y, int _level) {
		type = "projectile_"+_type;
		direction = _direction;
		speed = _speed;
		damage = _damage;
		range = 10;
		x = _x;
		y = _y;
		x1 = x;
		y1 = y;
		level = _level;
		id = id_set;
		id_set++;
		setIMG();
	}
	
	public void setIMG() {
		int tmp_id = Global.view.getResources().getIdentifier(type, "drawable", Global.package_name);
		img = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
		
		if(direction.equals("down")) {
			anim_y = 0;
		}
		else if(direction.equals("up")) {
			anim_y = anim_h;
		}
		else if(direction.equals("right")) {
			anim_y = anim_h*2;
		}
		else if(direction.equals("left")) {
			anim_y = anim_h*3;
		}
		
		rect_single = new Rect(0, anim_y, anim_w, anim_y+anim_h);
	}

	public int getID() {
		return id;
	}

	public int getY() {
		if(moving) {
			return y1;
		}
		else
			return y;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void remove() {
		for (int i = 0; i < Global.projectiles.size(); i++) {
			if(Global.projectiles.get(i).getID() == id) {
				Global.projectiles.remove(i);
				break;
			}
		}
	}
	
	
	public void move() {
		
		if(!moving) {
			if (direction.equals("up")) {
				y1 = y - 1;
			} else if (direction.equals("right")) {
				x1 = x +1;
			} else if (direction.equals("down")) {
				y1 = y + 1;
			} else if (direction.equals("left")) {
				x1 = x - 1;
			}
			if(Global.map.getExists(x1, y1)) {
				if(distance < range && Global.map.getTile(x1,y1,level).getWalk())
					moving = true;
				else
					remove();
			}
			else
				remove();		
		}
		
		if (moving) {
			if (direction.equals("up")) {
				move_y -= speed;
			} else if (direction.equals("right")) {
				move_x += speed;
			} else if (direction.equals("down")) {
				move_y += speed;
			} else if (direction.equals("left")) {
				move_x -= speed;
			}
			
			if (direction.equals("left") && (x*Global.tile_size) + move_x <= x1*Global.tile_size) {
				move_x = 0;
				x = x1;
				moving = false;
				distance++;
			} else if (direction.equals("right") && (x*Global.tile_size) + move_x >= x1*Global.tile_size) {
				move_x = 0;
				x = x1;
				moving = false;
				distance++;
			} else if (direction.equals("up") && (y*Global.tile_size) + move_y <= y1*Global.tile_size) {
				move_y = 0;
				y = y1;
				moving = false;
				distance++;
			} else if (direction.equals("down") && (y*Global.tile_size) + move_y >= y1*Global.tile_size) {
				move_y = 0;
				y = y1;
				moving = false;
				distance++;
			}
		}
	}
	
	public void onDraw(Canvas c) {
		
		move();
		
		int draw_x = Global.map.gridToPixel(x,y,level)[0]+move_x;
		int draw_y = Global.map.gridToPixel(x,y,level)[1]+move_y;
				
		Rect rect_draw = new Rect(draw_x, draw_y, draw_x+anim_w, draw_y+anim_h);
		
		c.drawBitmap(img, rect_single, rect_draw, null);
	}
}
