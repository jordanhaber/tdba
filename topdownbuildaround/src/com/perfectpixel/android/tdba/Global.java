package com.perfectpixel.android.tdba;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import android.graphics.Bitmap;
import android.graphics.Paint;

public class Global {
	
	public static String package_name;
	
	public static int tile_size = 40;
	
	public static int width;
	public static int height;
	public static int grid_width;
	public static int grid_height;
	
	public static RPG active;
	public static RPGView view;
	
	public static Brain brain;
	
	public static Player player;
	public static Map map;
	public static Bag bag;
	public static Health health;
	
	public static int bag_x;
	public static int health_x;
	
	public static ArrayList<Tile> tiles = new ArrayList<Tile>();
	public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	public static ArrayList<Effect> effects = new ArrayList<Effect>();
	public static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	public static Paint paint = new Paint();
	
	public static int enemy_max  = 20;
	
	public static int grid_level = 1;
	public static boolean grid = false;
	
	public static Hashtable<String, Bitmap[]> fades = new Hashtable<String, Bitmap[]>();
	
	public static boolean transparent = false;
	
	public static int mode = 0;
	
}
