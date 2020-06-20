package me.ci.Conquest.Buildings.Constructors;

import java.util.HashMap;

import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.KingdomManagment.KingdomMg;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Flag
{
	private Byte[] colors;
	private byte[] three_colors;

	public Flag(Byte[] colors)
	{
		this.colors = colors;
		this.three_colors = null;
		getThreeColors();
	}

	public Flag(String code)
	{
		String[] s = code.split(",");
		colors = new Byte[s.length];
		for (int i = 0; i < s.length; i++)
		{
			colors[i] = Byte.valueOf(s[i]);
		}
		getThreeColors();
	}

	public void setColors(Byte[] colors)
	{
		this.colors = colors;
		this.three_colors = null;
		getThreeColors(true);
	}

	public Byte getPrimaryColor()
	{
		return getThreeColors()[0];
	}

	public Byte getSecondaryColor()
	{
		return getThreeColors()[1];
	}

	public Byte getTertiaryColor()
	{
		return getThreeColors()[2];
	}

	public byte[] getThreeColors()
	{
		return getThreeColors(false);
	}

	public void recalculateColors()
	{
		getThreeColors(true);
	}

	private byte[] getThreeColors(boolean recolor)
	{
		if (recolor && three_colors != null)
			return three_colors;
		HashMap<Byte, Integer> cols = new HashMap<Byte, Integer>();
		for (int i = 1; i < colors.length; i++)
		{
			if (cols.containsKey(colors[i]))
				cols.put(colors[i], cols.get(colors[i]) + 1);
			else
				cols.put(colors[i], 1);
		}
		three_colors = new byte[3];
		three_colors[0] = getHighest(cols);
		cols.remove(three_colors[0]);
		three_colors[1] = getHighest(cols);
		cols.remove(three_colors[1]);
		three_colors[2] = getHighest(cols);
		if (three_colors[1] == -1)
			three_colors[1] = three_colors[0];
		if (three_colors[2] == -1)
			three_colors[2] = three_colors[0];
		return three_colors;
	}

	public byte getHighest(HashMap<Byte, Integer> cols)
	{
		byte by = -1;
		int i = -1;
		for (byte b : cols.keySet())
		{
			int o = cols.get(b);
			if (o > i)
			{
				by = b;
				i = o;
			}
		}
		return by;
	}

	public void draw(Block b)
	{
		if (colors[0] == 0)
		{
			b	.getRelative(0, 0, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 1, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 2, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 3, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 4, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 4, 1)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 4, 2)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 4, -1)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 4, -2)
				.setType(Material.OAK_FENCE);
			b	.getRelative(1, 4, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(2, 4, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(-1, 4, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(-2, 4, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 3, 2) // TODO Reimplement wool colors
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 2, 2)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 1, 2)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 3, -2)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 2, -2)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 1, -2)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(2, 3, 0)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(2, 2, 0)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(2, 1, 0)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(-2, 3, 0)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(-2, 2, 0)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(-2, 1, 0)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 0, 2)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 0, -2)
				.setType(Material.OAK_FENCE);
			b	.getRelative(2, 0, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(-2, 0, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, -1, 2)
				.setType(Material.COBBLESTONE_WALL);
			b	.getRelative(0, -1, -2)
				.setType(Material.COBBLESTONE_WALL);
			b	.getRelative(2, -1, 0)
				.setType(Material.COBBLESTONE_WALL);
			b	.getRelative(-2, -1, 0)
				.setType(Material.COBBLESTONE_WALL);
		}
		else if (colors[0] == 1)
		{
			b.setType(Material.OAK_FENCE);
			b	.getRelative(0, 1, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 2, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 3, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 4, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 4, -1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 3, -1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 2, -1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 4, -2)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 3, -2)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 2, -2)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 4, -3)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 3, -3)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 2, -3)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 4, -4)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 3, -4)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 2, -4)
				.setType(Material.WHITE_WOOL);
		}
		else if (colors[0] == 2)
		{
			b.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 1, -1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 1, 0)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 1, 1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 2, -1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 2, 0)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 2, 1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 3, -1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 3, 0)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 3, 1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 4, -2)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 4, -1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 4, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 4, 1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 4, 2)
				.setType(Material.OAK_FENCE);
		}
		else if (colors[0] == 3)
		{
			b.setType(Material.OAK_FENCE);
			b	.getRelative(0, 1, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 2, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 3, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 4, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 4, -1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(-1, 4, -2)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 4, -3)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 4, -4)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 4, -5)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 3, -1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(-1, 3, -2)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 3, -3)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 3, -4)
				.setType(Material.WHITE_WOOL);
		}
		else
		{
			b.setType(Material.OAK_FENCE);
			b	.getRelative(0, 1, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 2, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 3, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 4, 0)
				.setType(Material.OAK_FENCE);
			b	.getRelative(0, 0, -1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 1, -1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 2, -1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 3, -1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 4, -1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 0, 1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 1, 1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 2, 1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 3, 1)
				.setType(Material.WHITE_WOOL);
			b	.getRelative(0, 4, 1)
				.setType(Material.WHITE_WOOL);
		}
	}

	public static boolean isPartOfFlag(Block main, Block clicked, byte id)
	{
		if (isPartOfWool(main, clicked, id))
			return true;
		if (id == 0)
		{
			if (main.getRelative(0, 0, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 1, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 2, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 3, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 4, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 4, 1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 4, 2)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 4, -1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 4, -2)
					.equals(clicked))
				return true;
			if (main.getRelative(1, 4, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(2, 4, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(-1, 4, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(-2, 4, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 0, 2)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 0, -2)
					.equals(clicked))
				return true;
			if (main.getRelative(2, 0, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(-2, 0, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, -1, 2)
					.equals(clicked))
				return true;
			if (main.getRelative(0, -1, -2)
					.equals(clicked))
				return true;
			if (main.getRelative(2, -1, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(-2, -1, 0)
					.equals(clicked))
				return true;
		}
		else if (id == 1)
		{
			if (main.getRelative(0, 0, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 1, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 2, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 3, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 4, 0)
					.equals(clicked))
				return true;
		}
		else if (id == 2)
		{
			if (main.getRelative(0, 4, 2)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 4, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 4, -1)
					.equals(clicked))
				return true;
		}
		else if (id == 3)
		{
			if (main.getRelative(0, 0, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 1, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 2, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 3, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 4, 0)
					.equals(clicked))
				return true;
		}
		else
		{
			if (main.getRelative(0, 0, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 1, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 2, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 3, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 4, 0)
					.equals(clicked))
				return true;
		}
		return false;
	}

	public static boolean isPartOfWool(Block main, Block clicked, byte id)
	{
		if (id == 0)
		{
			if (main.getRelative(0, 3, 2)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 2, 2)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 1, 2)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 3, -2)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 2, -2)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 1, -2)
					.equals(clicked))
				return true;
			if (main.getRelative(2, 3, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(2, 2, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(2, 1, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(-2, 3, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(-2, 2, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(-2, 1, 0)
					.equals(clicked))
				return true;
		}
		else if (id == 1)
		{
			if (main.getRelative(0, 4, -1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 3, -1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 2, -1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 4, -2)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 3, -2)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 2, -2)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 4, -3)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 3, -3)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 2, -3)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 4, -4)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 3, -4)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 2, -4)
					.equals(clicked))
				return true;
		}
		else if (id == 2)
		{
			if (main.getRelative(0, 0, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 1, -1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 1, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 1, 1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 2, -1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 2, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 2, 1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 3, -1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 3, 0)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 3, 1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 4, -1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 4, 1)
					.equals(clicked))
				return true;
		}
		else if (id == 3)
		{
			if (main.getRelative(0, 4, -1)
					.equals(clicked))
				return true;
			if (main.getRelative(-1, 4, -2)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 4, -3)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 4, -4)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 4, -5)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 3, -1)
					.equals(clicked))
				return true;
			if (main.getRelative(-1, 3, -2)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 3, -3)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 3, -4)
					.equals(clicked))
				return true;
		}
		else
		{
			if (main.getRelative(0, 0, 1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 1, 1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 2, 1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 3, 1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 4, 1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 0, -1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 1, -1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 2, -1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 3, -1)
					.equals(clicked))
				return true;
			if (main.getRelative(0, 4, -1)
					.equals(clicked))
				return true;
		}
		return false;
	}

	public static void addColor(Byte[] before, Block main, Block clicked, byte color)
	{
		before[getWoolId(main, clicked, before[0])] = color;
	}

	public static int getWoolBlocks(byte id)
	{
		if (id == 0)
			return 12;
		if (id == 1)
			return 12;
		if (id == 2)
			return 12;
		if (id == 3)
			return 9;
		return 10;
	}

	public static byte getWoolId(Block main, Block wool, byte id)
	{
		if (id == 0)
		{
			if (main.getRelative(0, 3, 2)
					.equals(wool))
				return 1;
			if (main.getRelative(0, 2, 2)
					.equals(wool))
				return 2;
			if (main.getRelative(0, 1, 2)
					.equals(wool))
				return 3;
			if (main.getRelative(0, 3, -2)
					.equals(wool))
				return 4;
			if (main.getRelative(0, 2, -2)
					.equals(wool))
				return 5;
			if (main.getRelative(0, 1, -2)
					.equals(wool))
				return 6;
			if (main.getRelative(2, 3, 0)
					.equals(wool))
				return 7;
			if (main.getRelative(2, 2, 0)
					.equals(wool))
				return 8;
			if (main.getRelative(2, 1, 0)
					.equals(wool))
				return 9;
			if (main.getRelative(-2, 3, 0)
					.equals(wool))
				return 10;
			if (main.getRelative(-2, 2, 0)
					.equals(wool))
				return 11;
			if (main.getRelative(-2, 1, 0)
					.equals(wool))
				return 12;
		}
		else if (id == 1)
		{
			if (main.getRelative(0, 4, -1)
					.equals(wool))
				return 1;
			if (main.getRelative(0, 3, -1)
					.equals(wool))
				return 2;
			if (main.getRelative(0, 2, -1)
					.equals(wool))
				return 3;
			if (main.getRelative(0, 4, -2)
					.equals(wool))
				return 4;
			if (main.getRelative(0, 3, -2)
					.equals(wool))
				return 5;
			if (main.getRelative(0, 2, -2)
					.equals(wool))
				return 6;
			if (main.getRelative(0, 4, -3)
					.equals(wool))
				return 7;
			if (main.getRelative(0, 3, -3)
					.equals(wool))
				return 8;
			if (main.getRelative(0, 2, -3)
					.equals(wool))
				return 9;
			if (main.getRelative(0, 4, -4)
					.equals(wool))
				return 10;
			if (main.getRelative(0, 3, -4)
					.equals(wool))
				return 11;
			if (main.getRelative(0, 2, -4)
					.equals(wool))
				return 12;
		}
		else if (id == 2)
		{
			if (main.getRelative(0, 0, 0)
					.equals(wool))
				return 1;
			if (main.getRelative(0, 1, -1)
					.equals(wool))
				return 2;
			if (main.getRelative(0, 1, 0)
					.equals(wool))
				return 3;
			if (main.getRelative(0, 1, 1)
					.equals(wool))
				return 4;
			if (main.getRelative(0, 2, -1)
					.equals(wool))
				return 5;
			if (main.getRelative(0, 2, 0)
					.equals(wool))
				return 6;
			if (main.getRelative(0, 2, 1)
					.equals(wool))
				return 7;
			if (main.getRelative(0, 3, -1)
					.equals(wool))
				return 8;
			if (main.getRelative(0, 3, 0)
					.equals(wool))
				return 9;
			if (main.getRelative(0, 3, 1)
					.equals(wool))
				return 10;
			if (main.getRelative(0, 4, -1)
					.equals(wool))
				return 11;
			if (main.getRelative(0, 4, 1)
					.equals(wool))
				return 12;
		}
		else if (id == 3)
		{
			if (main.getRelative(0, 4, -1)
					.equals(wool))
				return 1;
			if (main.getRelative(-1, 4, -2)
					.equals(wool))
				return 2;
			if (main.getRelative(0, 4, -3)
					.equals(wool))
				return 3;
			if (main.getRelative(0, 4, -4)
					.equals(wool))
				return 4;
			if (main.getRelative(0, 4, -5)
					.equals(wool))
				return 5;
			if (main.getRelative(0, 3, -1)
					.equals(wool))
				return 6;
			if (main.getRelative(-1, 3, -2)
					.equals(wool))
				return 7;
			if (main.getRelative(0, 3, -3)
					.equals(wool))
				return 8;
			if (main.getRelative(0, 3, -4)
					.equals(wool))
				return 9;
		}
		else
		{
			if (main.getRelative(0, 0, 1)
					.equals(wool))
				return 1;
			if (main.getRelative(0, 1, 1)
					.equals(wool))
				return 2;
			if (main.getRelative(0, 2, 1)
					.equals(wool))
				return 3;
			if (main.getRelative(0, 3, 1)
					.equals(wool))
				return 4;
			if (main.getRelative(0, 4, 1)
					.equals(wool))
				return 5;
			if (main.getRelative(0, 0, -1)
					.equals(wool))
				return 6;
			if (main.getRelative(0, 1, -1)
					.equals(wool))
				return 7;
			if (main.getRelative(0, 2, -1)
					.equals(wool))
				return 8;
			if (main.getRelative(0, 3, -1)
					.equals(wool))
				return 9;
			if (main.getRelative(0, 4, -1)
					.equals(wool))
				return 10;
		}
		return 0;
	}

	public static boolean exists(Byte[] flagcolors)
	{
		for (Kingdom k : KingdomMg.getKingdoms())
		{
			if (sameFlag(k.getFlag().colors, flagcolors))
				return true;
		}
		return false;
	}

	private static boolean sameFlag(Byte[] f1, Byte[] f2)
	{
		if (f1.length != f2.length)
			return false;
		for (int i = 0; i < f1.length; i++)
		{
			if (f1[i] != f2[i])
				return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		String s = "";
		for (byte b : colors)
		{
			s += "," + b;
		}
		s = s.substring(1);
		return s;
	}

	public static String getName(int id)
	{
		if (id == 0)
			return "Weighted Banner";
		if (id == 1)
			return "Modern";
		if (id == 2)
			return "Gonfalon";
		if (id == 3)
			return "Pennon";
		return "Banner";
	}

	public int getId()
	{
		return this.colors[0];
	}

	public static Block getBlockAt(Byte[] info, Block main, int pic)
	{
		try
		{
			if (info[0] == 0)
			{
				if (pic == 1)
					return main.getRelative(0, 3, 2);
				if (pic == 2)
					return main.getRelative(0, 2, 2);
				if (pic == 3)
					return main.getRelative(0, 1, 2);
				if (pic == 4)
					return main.getRelative(0, 3, -2);
				if (pic == 5)
					return main.getRelative(0, 2, -2);
				if (pic == 6)
					return main.getRelative(0, 1, -2);
				if (pic == 7)
					return main.getRelative(2, 3, 0);
				if (pic == 8)
					return main.getRelative(2, 2, 0);
				if (pic == 9)
					return main.getRelative(2, 1, 0);
				if (pic == 10)
					return main.getRelative(-2, 3, 0);
				if (pic == 11)
					return main.getRelative(-2, 2, 0);
				if (pic == 12)
					return main.getRelative(-2, 1, 0);
			}
			else if (info[0] == 1)
			{
				if (pic == 1)
					return main.getRelative(0, 4, -1);
				if (pic == 2)
					return main.getRelative(0, 3, -1);
				if (pic == 3)
					return main.getRelative(0, 2, -1);
				if (pic == 4)
					return main.getRelative(0, 4, -2);
				if (pic == 5)
					return main.getRelative(0, 3, -2);
				if (pic == 6)
					return main.getRelative(0, 2, -2);
				if (pic == 7)
					return main.getRelative(0, 4, -3);
				if (pic == 8)
					return main.getRelative(0, 3, -3);
				if (pic == 9)
					return main.getRelative(0, 2, -3);
				if (pic == 10)
					return main.getRelative(0, 4, -4);
				if (pic == 11)
					return main.getRelative(0, 3, -4);
				if (pic == 12)
					return main.getRelative(0, 2, -4);
			}
			else if (info[0] == 2)
			{
				if (pic == 1)
					return main.getRelative(0, 1, -1);
				if (pic == 2)
					return main.getRelative(0, 1, 0);
				if (pic == 3)
					return main.getRelative(0, 1, 1);
				if (pic == 4)
					return main.getRelative(0, 2, -1);
				if (pic == 5)
					return main.getRelative(0, 2, 0);
				if (pic == 6)
					return main.getRelative(0, 2, 1);
				if (pic == 7)
					return main.getRelative(0, 3, -1);
				if (pic == 8)
					return main.getRelative(0, 3, 0);
				if (pic == 9)
					return main.getRelative(0, 3, 1);
				if (pic == 10)
					return main.getRelative(0, 4, -1);
				if (pic == 11)
					return main.getRelative(0, 4, 1);
			}
			else if (info[0] == 3)
			{
				if (pic == 1)
					return main.getRelative(0, 4, -1);
				if (pic == 2)
					return main.getRelative(-1, 4, -2);
				if (pic == 3)
					return main.getRelative(0, 4, -3);
				if (pic == 4)
					return main.getRelative(0, 4, -4);
				if (pic == 5)
					return main.getRelative(0, 4, -5);
				if (pic == 6)
					return main.getRelative(0, 3, -1);
				if (pic == 7)
					return main.getRelative(-1, 3, -2);
				if (pic == 8)
					return main.getRelative(0, 3, -3);
				if (pic == 9)
					return main.getRelative(0, 3, -4);
			}
			else
			{
				if (pic == 1)
					return main.getRelative(0, 0, -1);
				if (pic == 2)
					return main.getRelative(0, 1, -1);
				if (pic == 3)
					return main.getRelative(0, 2, -1);
				if (pic == 4)
					return main.getRelative(0, 3, -1);
				if (pic == 5)
					return main.getRelative(0, 4, -1);
				if (pic == 6)
					return main.getRelative(0, 0, 1);
				if (pic == 7)
					return main.getRelative(0, 1, 1);
				if (pic == 8)
					return main.getRelative(0, 2, 1);
				if (pic == 9)
					return main.getRelative(0, 3, 1);
				if (pic == 10)
					return main.getRelative(0, 4, 1);
			}
		}
		catch (Exception exception)
		{}
		return main;
	}
}
