package me.ci.Conquest.Military;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.ci.Community.Plot;
import me.ci.Conquest.Military.Henchmen.BountyHunter;
import me.ci.Conquest.Military.Henchmen.Commander;
import me.ci.Conquest.Military.Henchmen.Explorer;
import me.ci.Conquest.Military.Henchmen.Spy;
import me.ci.Conquest.Military.Henchmen.TinkerGnome;
import me.ci.Conquest.Military.Henchmen.Wizard;

public enum HenchmenType
{
	BOUNTY_HUNTER(4, "BountyHunter", 4000, 6000, new BountyHunter(), Material.IRON_BARS),
	TINKER_GNOME(5, "TinkerGnome", 5000, 7000, new TinkerGnome(), Material.ANVIL),
	COMMANDER(9, "Commander", 7000, 9000, new Commander(), Material.GOLDEN_HELMET),
	WIZARD(10, "Wizard", 1000, 2500, new Wizard(), Material.NETHER_STAR),
	SPY(11, "Spy", 500, 1000, new Spy(), Material.FEATHER),
	EXPLORER(12, "Explorer", 4000, 6000, new Explorer(), Material.LEATHER_BOOTS);

	private final int id;
	private final String name;
	private final int mincost;
	private final int maxcost;
	private final Henchman henchman;
	private final Material material;

	private HenchmenType(final int a, final String b, final int c, final int f, final Henchman d, final Material e)
	{
		id = a;
		name = b;
		mincost = c;
		henchman = d;
		material = e;
		maxcost = f;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public int getMinCost()
	{
		return mincost;
	}

	public int getMaxCost()
	{
		return maxcost;
	}

	public boolean use(final Player p, final Plot plot)
	{
		return henchman.use(p, plot);
	}

	public void explain(final Player p)
	{
		henchman.explain(p);
	}

	public Material getMaterial()
	{
		return material;
	}

	public static HenchmenType getById(final int id)
	{
		for (HenchmenType men : values())
		{
			if (men.getId() == id)
				return men;
		}
		return null;
	}

	public static HenchmenType getByName(final String name)
	{
		for (HenchmenType men : values())
		{
			if (men	.getName()
					.equalsIgnoreCase(name))
				return men;
		}
		return null;
	}
}
