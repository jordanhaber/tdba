package com.perfectpixel.android.tdba;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class Bag {

	final private int BAG_WIDTH = 60;
	private int padding, x;
	private Inventory inventory;
	private HashMap<Integer, Integer> bag_tiles = new HashMap<Integer, Integer>();
	private ArrayList<int[]> tile_coords = new ArrayList<int[]>();
	private boolean placing = false;
	private ArrayList<int[]> place_coords = new ArrayList<int[]>();
	private int[] ui_y = new int[3];
	private Bitmap[] ui_img = new Bitmap[6];
	private int ui_height = 0;
	
	public Bag() {
		inventory = Global.player.getInventory();
		padding = BAG_WIDTH / 10;
		x = Global.bag_x - (BAG_WIDTH / 2);
		setUI();
	}
	
	public void setUI() {
				
		int tmp_id = Global.view.getResources().getIdentifier("ui_backpack", "drawable", Global.package_name);
		ui_img[0] = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
		tmp_id = Global.view.getResources().getIdentifier("ui_backpack"+2, "drawable", Global.package_name);
		ui_img[1] = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
		
		tmp_id = Global.view.getResources().getIdentifier("ui_equipment", "drawable", Global.package_name);
		ui_img[2] = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
		tmp_id = Global.view.getResources().getIdentifier("ui_equipment"+2, "drawable", Global.package_name);
		ui_img[3] = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
		
		tmp_id = Global.view.getResources().getIdentifier("ui_attack", "drawable", Global.package_name);
		ui_img[4] = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
		tmp_id = Global.view.getResources().getIdentifier("ui_attack"+2, "drawable", Global.package_name);
		ui_img[5] = BitmapFactory.decodeResource(Global.view.getResources(), tmp_id);
		
		ui_height = ui_img[0].getHeight();
		ui_y[0] = padding*2;
		ui_y[1] = padding*4+ui_height;
		ui_y[2] = padding*6+ui_height*2;

	}

	public boolean getPlacing() {
		return placing;
	}

	public void updateBag() {
		int[] tiles = inventory.getTiles();

		bag_tiles.clear();
		tile_coords.clear();

		for (int i = 0; i < tiles.length; i++) {
			if (tiles[i] != 0) {
				bag_tiles.put(i, tiles[i]);
			}
		}
	}
	
	public int getSize() {
		return BAG_WIDTH;
	}
	
	public void setPlacing(boolean _placing) {
		placing = _placing;
	}

	public boolean resolveBounds(float _x, float _y) {
		if (_x > x && _x < x + BAG_WIDTH) {
			return true;
		}
		return false;
	}

	public int resolveTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if(event.getX() > x + BAG_WIDTH) {
				if(event.getY() > ui_y[0]) {
					if(event.getY() < ui_y[0] + ui_height) {
						Global.mode = 0;
					}
					else if(event.getY() < ui_y[1] + ui_height) {
						Global.mode = 1;
					}
					else if(event.getY() < ui_y[2] + ui_height) {
						Global.mode = 2;
					}
				}
			}
		}
		if (event.getAction() == MotionEvent.ACTION_UP && resolveBounds(event.getX(), event.getY())) {
			if (placing) {
				for (int i = 0; i < place_coords.size(); i++) {
					if (place_coords.get(i)[1] < event.getY()
							&& place_coords.get(i)[1] + BAG_WIDTH - padding > event.getY()) {
						return place_coords.get(i)[0];
					}
				}
			} 
			else if(Global.mode == 0) {
				for (int i = 0; i < tile_coords.size(); i++) {
					if (tile_coords.get(i)[1] < event.getY()
							&& tile_coords.get(i)[1] + BAG_WIDTH - padding > event.getY()) {
						return tile_coords.get(i)[0];
					}
				}
			}
		}
		
		return 999;
	}

	public void onDraw(Canvas c) {

		Global.paint.setColor(Color.argb(80, 0, 0, 0));
		c.drawRect(x, 0, x + BAG_WIDTH, Global.height, Global.paint);
		
		for(int i = 0; i < ui_y.length; i++) {
			if (Global.mode == i) {
				c.drawBitmap(ui_img[i*2+1], x + BAG_WIDTH, ui_y[i], null);
			}
			else {
				c.drawBitmap(ui_img[i*2], x + BAG_WIDTH, ui_y[i], null);
			}
		}

		if (placing) {

			place_coords.clear();

			int tmp_y = (int) (padding);
			Global.paint.setColor(Color.RED);
			c.drawRect(x + padding, tmp_y, x + BAG_WIDTH - padding, tmp_y + BAG_WIDTH - (padding * 2), Global.paint);
			int[] tmp_a = new int[2];
			tmp_a[0] = 1;
			tmp_a[1] = tmp_y;
			place_coords.add(tmp_a);

			tmp_y = (int) (BAG_WIDTH+padding)+padding;
			Global.paint.setColor(Color.GREEN);
			c.drawRect(x + padding, tmp_y, x + BAG_WIDTH - padding, tmp_y + BAG_WIDTH - (padding * 2), Global.paint);
			int[] tmp_b = new int[2];
			tmp_b[0] = 2;
			tmp_b[1] = tmp_y;
			place_coords.add(tmp_b);
			
			tmp_y = (int) (BAG_WIDTH+padding)*2+padding;
			Global.paint.setColor(Color.YELLOW);
			c.drawRect(x + padding, tmp_y, x + BAG_WIDTH - padding, tmp_y + BAG_WIDTH - (padding * 2), Global.paint);
			int[] tmp_c = new int[2];
			tmp_c[0] = 3;
			tmp_c[1] = tmp_y;
			place_coords.add(tmp_c);
			
			tmp_y = (int) (BAG_WIDTH+padding)*3+padding;
			Global.paint.setColor(Color.YELLOW);
			c.drawRect(x + padding, tmp_y, x + BAG_WIDTH - padding, tmp_y + BAG_WIDTH - (padding * 2), Global.paint);
			int[] tmp_d = new int[2];
			tmp_d[0] = 4;
			tmp_d[1] = tmp_y;
			place_coords.add(tmp_d);
		}

		else if (Global.mode == 0) {

			updateBag();

			Set set = bag_tiles.keySet();
			Iterator itr = set.iterator();

			int count = 0;

			while (itr.hasNext()) {
				int id = Integer.parseInt(itr.next().toString());

				Global.tiles.get(id).drawInventory(c, x + padding, padding + ((BAG_WIDTH + padding) * count), BAG_WIDTH - (padding * 2), BAG_WIDTH);

				Global.paint.setColor(Color.WHITE);
				Global.paint.setTextAlign(Paint.Align.CENTER);
				c.drawText(bag_tiles.get(id).toString(), x + (BAG_WIDTH / 2), padding + ((BAG_WIDTH + padding) * count) + (BAG_WIDTH / 2), Global.paint);

				int[] tmp = new int[3];
				tmp[0] = id;
				tmp[1] = padding + ((BAG_WIDTH + padding) * count);
				tile_coords.add(tmp);
				count++;
			}
			Global.paint.setColor(Color.argb(255, 0, 0, 0));
		}
	}
}
