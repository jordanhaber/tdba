package com.perfectpixel.android.tdba;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Player {

	private int padding, draw_x, draw_y;
	private int x, y, x1, y1;
	private int speed = Global.tile_size / 4;
	private int level = 1;
	private Inventory inventory;
	private int max_life = 100;
	private int life = max_life;
	private int damage = 2;
	private int reach = 3;
	
	private String direction = "down";
	
	private boolean moving = false;
	private int frame_1 = 0;
	private String direction_move = "";
	private int frame_2_choose = 0;
	
	private boolean attacking = false;
	private int frame_2 = 0;
	private String direction_attack = "";
	private int frame_1_choose = 0;
	
	private int num_body = 0;
	private int num_face = 0;
	private int num_hair = 0;
	private int num_arms = 0;
	private int num_armor = 0;
	private int num_weapon = 0;
	
	private Bitmap[] img_body = new Bitmap[5];
	private Bitmap[] img_face = new Bitmap[5];
	private Bitmap[] img_hair = new Bitmap[5];
	private Bitmap[] img_arms = new Bitmap[5];
	private Bitmap[] img_arms_back = new Bitmap[5];
	private Bitmap[] img_armor = new Bitmap[5];
	private Bitmap[] img_weapon = new Bitmap[5];
	
	private int anim_h = 60;
	private int anim_w = 40;
	private int anim_x = 0;
	private int anim_y = 0;
	private int anim_rate = 1;
	private Rect rect_draw;

	public Player(int _x, int _y) {
		draw_x = _x;
		draw_y = _y;
		x1 = 0;
		y1 = 0;
		x = Global.tile_size;
		y = Global.tile_size;
		padding = Global.tile_size / 8;
		inventory = new Inventory();
		setIMG();
	}

	public void setIMG() {
		
		int tmp_id = Global.view.getResources().getIdentifier("player_body", "drawable", Global.package_name);
		img_body[0] = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
		
		tmp_id = Global.view.getResources().getIdentifier("player_face", "drawable", Global.package_name);
		img_face[0] = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
		
		tmp_id = Global.view.getResources().getIdentifier("player_hair", "drawable", Global.package_name);
		img_hair[0] = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
		
		tmp_id = Global.view.getResources().getIdentifier("player_armor", "drawable", Global.package_name);
		img_armor[0] = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
		
		tmp_id = Global.view.getResources().getIdentifier("player_arms_back_bow", "drawable", Global.package_name);
		img_arms_back[0] = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
		
		tmp_id = Global.view.getResources().getIdentifier("player_arms_bow", "drawable", Global.package_name);
		img_arms[0] = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
		
		tmp_id = Global.view.getResources().getIdentifier("weapon_bow", "drawable", Global.package_name);
		img_weapon[0] = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
		
		rect_draw = new Rect(draw_x, draw_y-Global.tile_size/2, draw_x+anim_w, draw_y+anim_h-Global.tile_size/2);
		
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getX1() {
		return x;
	}

	public int getY1() {
		return y;
	}

	
	public int getDrawX() {
		return draw_x;
	}

	public int getDrawY() {
		return draw_y;
	}

	public int getLevel() {
		return level;
	}
	
	public int getReach() {
		return reach;
	}
	
	public boolean getMoving() {
		return moving;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void damage(int _input) {
		life -= _input;
	}

	public int getLife() {
		return life;
	}

	public int getMaxLife() {
		return max_life;
	}

	public int getDamage() {
		return damage;
	}
	
	public void setAttack(String _direction) {
		if (!moving && !attacking && !_direction.equals("")) {
			direction_attack = _direction;
			direction = _direction;
			frame_2 = 0;
			attacking = true;
		}
	}
	
	public void setDirection(String _direction) {
		if (!moving && !attacking && !_direction.equals("")) {
			if (level > 0 && Global.map.getWalk(_direction, getX(), getY(), level-1)) {
				if (Global.map.getWalk(_direction, getX(), getY(), level)) {
					if (level < Global.map.maps.size() && Global.map.getWalk(_direction, getX(), getY(), level + 1)) {
						direction_move = _direction;
						direction = _direction;
						moving = true;
					}
				}
			}
		}
	}

	public void movePlayer() {

		if (moving && x1 == 0 && y1 == 0) {
			if (direction_move.equals("up")) {
				y1 = y - Global.tile_size;
			} else if (direction_move.equals("right")) {
				x1 = x + Global.tile_size;
			} else if (direction_move.equals("down")) {
				y1 = y + Global.tile_size;
			} else if (direction_move.equals("left")) {
				x1 = x - Global.tile_size;
			}
		}
		
		if (moving) {
			if (direction_move.equals("up")) {
				y -= speed;
			} else if (direction_move.equals("right")) {
				x += speed;
			} else if (direction_move.equals("down")) {
				y += speed;
			} else if (direction_move.equals("left")) {
				x -= speed;
			}
		

			if (direction_move.equals("left") && x <= x1) {
				x = x1;
				x1 = 0;
				y1 = 0;
				direction_move = "";
				moving = false;
			} else if (direction_move.equals("right") && x >= x1) {
				x = x1;
				x1 = 0;
				y1 = 0;
				direction_move = "";
				moving = false;
			} else if (direction_move.equals("up") && y <= y1) {
				y = y1;
				x1 = 0;
				y1 = 0;
				direction_move = "";
				moving = false;
			} else if (direction_move.equals("down") && y >= y1) {
				y = y1;
				x1 = 0;
				y1 = 0;
				direction_move = "";
				moving = false;
			}
		}
	}
	
	public boolean checkEnd(int _group, Bitmap _img) {
		if(_group == 1) {
			if(frame_1>((_img.getWidth()/anim_w)-1)*anim_rate+(anim_rate/2)) {
				return true;
			}
			else
				return false;
		}
		if(_group == 2) {
			if(frame_2>=((_img.getWidth()/anim_w)-1)*anim_rate+(anim_rate/2)) {
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}

	public void onDraw(Canvas c) {

		if (Global.map != null)
			movePlayer();
		
		String tmp_direction = "";
		
		if(attacking)
			tmp_direction = direction_attack;
		else
			tmp_direction = direction_move;
		
		if(tmp_direction.equals("down")) {
			anim_y = 0;
		}
		else if(tmp_direction.equals("up")) {
			anim_y = anim_h;
		}
		else if(tmp_direction.equals("right")) {
			anim_y = anim_h*2;
		}
		else if(tmp_direction.equals("left")) {
			anim_y = anim_h*3;
		}
		
		if(moving || !(frame_1 == 0 || frame_1 == 4*anim_rate))
			frame_1++;
		
		if(checkEnd(1, img_body[num_body]))
			frame_1 = 0;
		
		int draw_1 = (int) Math.floor(frame_1/anim_rate);
		
		int draw_2;
		if(!attacking)
			draw_2 = frame_2_choose;
		else
			draw_2 = (int) Math.floor(frame_2/anim_rate);
		
		Rect rect_1 = new Rect(anim_w*draw_1, anim_y, anim_w*(draw_1+1), anim_y+anim_h);
		Rect rect_2 = new Rect(anim_w*draw_2, anim_y, anim_w*(draw_2+1), anim_y+anim_h);
		Rect rect_single = new Rect(0, anim_y, anim_w, anim_y+anim_h);
		
		//Draw the player
		if(direction.equals("right")) {
			c.drawBitmap(img_arms_back[num_arms], rect_2, rect_draw, null);
			c.drawBitmap(img_weapon[num_weapon], rect_2, rect_draw, null);
		}
			
		else if(direction.equals("up")) {
			c.drawBitmap(img_weapon[num_weapon], rect_2, rect_draw, null);
			c.drawBitmap(img_arms_back[num_arms], rect_2, rect_draw, null);
		}
		
		else
			c.drawBitmap(img_arms_back[num_arms], rect_2, rect_draw, null);

		c.drawBitmap(img_body[num_body], rect_1, rect_draw, null);
		c.drawBitmap(img_face[num_face], rect_single, rect_draw, null);
		c.drawBitmap(img_armor[num_armor], rect_1, rect_draw, null);
		c.drawBitmap(img_hair[num_hair], rect_single, rect_draw, null);
		c.drawBitmap(img_arms[num_arms], rect_2, rect_draw, null);
		
		if(direction.equals("left") || direction.equals("down"))
			c.drawBitmap(img_weapon[num_weapon], rect_2, rect_draw, null);
		
		if(attacking && checkEnd(2, img_arms[num_arms])) {
			frame_2 = 0;
			attacking = false;
			///assign damage/add object/effect
			Global.projectiles.add(new Projectile("arrow", direction_attack, Global.tile_size/2, damage, 10, Global.map.getCenterTile()[0], Global.map.getCenterTile()[1], level));
		}
		else if(attacking) {
			frame_2++;
		}
	}
}
