package me.ci.Conquest.Buildings.Constructors;

import me.ci.Community.Plot;
import me.ci.Community.PlotMg;
import me.ci.Conquest.KingdomManagment.Kingdom;
import me.ci.Conquest.Military.ArmyFormation;
import me.ci.Conquest.Misc.Direction;

import org.bukkit.Chunk;

public enum BuildingType{
	WILDERNESS(-1, 1, "Wilderness", new int[]{1}, 1, new int[]{0}, new int[]{0}, new int[]{0}, false, false, new int[]{1}, false, '-', false),
	LAND(0, 1, "Land", new int[]{88}, 1, new int[]{88}, new int[]{0}, new int[]{0}, false, false, new int[]{1, 1, 1, 1, 1, 1}, false, '+', false),
	CAPITOL(1, 7, "Capitol", new int[]{7644, 14602, 22638, 29204, 41405, 53214, 59682, 67424, 79380, 95060, 105105, 117600, 125489, 131026, 170520, 177968, 194922, 203889, 207270, 244020}, 20, new int[]{5733, 10951, 16978, 21903, 20702, 26607, 29841, 33712, 39690, 47530, 52552, 58800, 62744, 65513, 85260, 88984, 97461, 101944, 103635, 122010}, new int[]{1911, 3650, 5659, 7301, 20702, 26607, 29841, 33712, 39690, 38024, 42041, 47040, 50195, 52410, 42630, 44492, 48730, 50972, 51817, 0}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 9506, 10510, 11760, 12548, 13102, 42630, 44492, 48730, 50972, 51817, 122010}, false, false, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20}, true, '=', false),
	ANCIENT_DOJO(2, 3, "AncientDojo", new int[]{936, 1656, 2565, 5796, 7110, 10368, 13293}, 7, new int[]{936, 1656, 2565, 4347, 5332, 5184, 6646}, new int[]{0, 0, 0, 1449, 1777, 4147, 5316}, new int[]{0, 0, 0, 0, 0, 1036, 1329}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	ANTIMAGIC_ONAGER_WORKSHOP(3, 3, "AntiMagicOnagerWorkshop", new int[]{828, 2034, 3564, 4896, 5355, 7128, 12663}, 7, new int[]{828, 2034, 4989, 6854, 7496, 9979, 6331}, new int[]{0, 0, 356, 489, 535, 712, 5064}, new int[]{0, 0, 0, 0, 0, 0, 1266}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	ARCHERY_RANGE(4, 3, "ArcheryRange", new int[]{990, 2340, 4320, 5184, 8415, 10503, 13734}, 7, new int[]{990, 3227, 3240, 3888, 4207, 5265, 6867}, new int[]{0, 234, 1080, 1296, 4207, 4212, 3433}, new int[]{0, 0, 0, 0, 0, 1053, 3433}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	BALLISTIC_WORKSHOP(5, 3, "BallisticWorkshop", new int[]{927, 2322, 4293, 6336, 7650, 11016, 12285}, 7, new int[]{927, 3250, 3219, 3168, 3825, 5508, 6142}, new int[]{0, 232, 1073, 3168, 3825, 4406, 4913}, new int[]{0, 0, 0, 0, 0, 1101, 1228}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	BARRACK(6, 3, "Barrack", new int[]{918, 2124, 4914, 6300, 8910, 12690, 14805}, 7, new int[]{918, 2973, 2457, 3150, 4455, 6345, 7402}, new int[]{0, 212, 2457, 3150, 3564, 3172, 3701}, new int[]{0, 0, 0, 0, 891, 3172, 3701}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	BATTERING_RAM_WORKSHOP(7, 3, "BatteringRamWorkshop", new int[]{837, 2070, 4293, 6732, 7920, 11285, 12033}, 7, new int[]{837, 2898, 3219, 3366, 3960, 5643, 6016}, new int[]{0, 207, 1073, 3366, 3960, 4514, 4812}, new int[]{0, 0, 0, 0, 0, 128, 1203}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	BLACKSMITH(8, 3, "Blacksmith", new int[]{873, 2250, 4320, 7560, 9450, 10962, 13356}, 7, new int[]{873, 3150, 3240, 3780, 4725, 5481, 10017}, new int[]{0, 225, 1080, 3024, 3780, 4384, 5342}, new int[]{0, 0, 0, 756, 945, 1096, 1335}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	BRIDGEMAKER_WORKSHOP(9, 3, "BridgeMakerWorkshop", new int[]{882, 2196, 3834, 6732, 8010, 11070, 12600}, 7, new int[]{882, 3074, 2875, 3366, 4005, 5535, 6300}, new int[]{0, 219, 958, 3366, 4005, 4428, 5040}, new int[]{0, 0, 0, 0, 0, 1107, 1260}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	CATAPULT_WORKSHOP(10, 3, "CatapultWorkshop", new int[]{927, 2286, 4104, 6588, 7785, 11070, 12726}, 7, new int[]{927, 3200, 3078, 3294, 3892, 5535, 6363}, new int[]{0, 228, 1026, 3294, 3892, 4428, 5090}, new int[]{0, 0, 0, 0, 0, 1107, 1272}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	CHURCH(11, 3, "Church", new int[]{963, 2538, 3915, 5256, 7605, 10908, 15939}, 7, new int[]{963, 1903, 2936, 3942, 3802, 5454, 7969}, new int[]{0, 634, 978, 1314, 3802, 4363, 0}, new int[]{0, 0, 0, 0, 0, 1090, 79690}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	COLISEUM(12, 5, "Coliseum", new int[]{5375, 11400, 16425, 21900, 27500, 33450, 38850}, 7, new int[]{2687, 5700, 8212, 10950, 13750, 16725, 19725}, new int[]{1343, 2850, 4106, 5475, 6875, 8362, 9712}, new int[]{1343, 2850, 4106, 5475, 6875, 8362, 9712}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	COTTAGE(13, 1, "Cottage", new int[]{92, 216, 477, 600, 715, 1224, 1596}, 7, new int[]{92, 216, 357, 450, 536, 612, 798}, new int[]{0, 0, 119, 150, 178, 489, 399}, new int[]{0, 0, 0, 0, 0, 122, 343}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, true, 'C', false),
	COW_TOSSER_WORKSHOP(14, 3, "CowTosserWorkshop", new int[]{810, 2322, 4212, 6084, 7785, 10314, 12789}, 7, new int[]{810, 3250, 3159, 3042, 3892, 5157, 6394}, new int[]{0, 232, 1053, 3042, 3892, 4125, 5115}, new int[]{0, 0, 0, 0, 0, 1031, 1278}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	DRAGONS_LAIR(15, 3, "DragonsLair", new int[]{2367, 4338, 6615, 8676, 11565, 13608, 15435}, 7, new int[]{1183, 2169, 3307, 4338, 5782, 6804, 7717}, new int[]{0, 0, 0, 0, 0, 0, 0}, new int[]{1183, 2169, 3307, 4338, 5782, 6804, 7717}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	DRAGON_PIT(16, 3, "DragonPit", new int[]{927, 2376, 6048, 8532, 10710, 11664, 15750}, 7, new int[]{927, 3326, 3024, 4266, 5355, 5832, 7875}, new int[]{0, 237, 1512, 2133, 2677, 2916, 0}, new int[]{0, 0, 1512, 2133, 2677, 2916, 7875}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	DRUIDS_HALLOW(17, 3, "DruidsHallow", new int[]{1629, 3060, 4509, 6192, 8325, 9558, 11655}, 7, new int[]{814, 1530, 2254, 3096, 4162, 4779, 5827}, new int[]{814, 1530, 2254, 3096, 4162, 4779, 5827}, new int[]{0, 0, 0, 0, 0, 0, 0}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	EARTH_TEMPLE(18, 3, "EarthTemple", new int[]{828, 2682, 4347, 6948, 10440, 11664, 15687}, 7, new int[]{828, 2011, 3266, 3474, 5220, 5832, 7843}, new int[]{0, 670, 1086, 2779, 2610, 2916, 0}, new int[]{0, 0, 0, 694, 2610, 2916, 7843}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	FIRE_TEMPLE(19, 3, "FireTemple", new int[]{1944, 4266, 5913, 7740, 9855, 12204, 15309}, 7, new int[]{972, 2133, 2956, 3870, 4927, 6102, 7654}, new int[]{486, 1066, 1478, 1935, 2463, 3051, 0}, new int[]{486, 1066, 1478, 1935, 2463, 3051, 7654}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	GATEHOUSE(20, 1, "Gatehouse", new int[]{855, 2592, 7425, 7596, 10305, 13878}, 6, new int[]{855, 1944, 2362, 3798, 5152, 6939}, new int[]{0, 648, 2362, 3038, 2576, 0}, new int[]{0, 0, 0, 759, 2576, 6939}, true, true, new int[]{1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6}, false, '=', true),
	GRANARY(21, 3, "Granary", new int[]{963, 2448, 3780, 5544, 8010, 9882, 12915}, 7, new int[]{963, 3427, 2835, 4158, 4005, 4941, 5165}, new int[]{0, 244, 945, 1386, 4005, 4941, 5165}, new int[]{0, 0, 0, 0, 0, 0, 1291}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	GRAVEYARD(22, 3, "Graveyard", new int[]{873, 2358, 3807, 5652, 10440, 12798, 16506}, 7, new int[]{873, 3301, 2855, 4239, 5220, 6399, 8253}, new int[]{0, 235, 951, 1413, 2610, 3199, 0}, new int[]{0, 0, 0, 0, 2610, 3199, 8253}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	HOLY_BATTLEFIELD(23, 3, "HolyBattlefield", new int[]{981, 2484, 3510, 4572, 5175, 7074, 8442}, 7, new int[]{981, 3477, 4914, 6400, 7244, 9903, 11818}, new int[]{0, 284, 351, 457, 517, 707, 844}, new int[]{0, 0, 0, 0, 0, 0, 0}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	ILLUSIONISTS_TOWER(24, 3, "IllusionistsTower", new int[]{1305, 2772, 3942, 6444, 9315, 12474, 16191}, 7, new int[]{978, 2079, 2956, 3222, 4657, 6237, 8095}, new int[]{326, 693, 985, 3222, 3725, 3118, 0}, new int[]{0, 0, 0, 0, 0, 3118, 8095}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	MAGES_TOWER(25, 3, "MagesTower", new int[]{2106, 3960, 5832, 8244, 10260, 12420, 14301}, 7, new int[]{1053, 1980, 2916, 4122, 5130, 6210, 7150}, new int[]{526, 990, 1458, 2061, 2565, 3105, 3575}, new int[]{526, 990, 1458, 2061, 2565, 3105, 3575}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	MANGONEL_WORKSHOP(26, 3, "MangonelWorkshop", new int[]{936, 2286, 3861, 6156, 8235, 11448, 13104}, 7, new int[]{936, 3200, 2895, 3078, 4117, 5724, 6552}, new int[]{0, 228, 965, 3078, 4117, 4579, 5241}, new int[]{0, 0, 0, 0, 0, 1144, 1310}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	MARKETPLACE(27, 3, "Marketplace", new int[]{954, 1836, 3159, 5148, 9000, 12636, 15813}, 7, new int[]{954, 1836, 4422, 3861, 4500, 6318, 7906}, new int[]{0, 0, 315, 1287, 3600, 3159, 0}, new int[]{0, 0, 0, 0, 900, 3159, 7906}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	MEAD_HALL(28, 3, "MeadHall", new int[]{882, 2340, 3969, 6768, 7560, 9558, 11277}, 7, new int[]{882, 3276, 2976, 3384, 3780, 4749, 5638}, new int[]{0, 234, 992, 3384, 3780, 4779, 5638}, new int[]{0, 0, 0, 0, 0, 0, 0}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	METAL_MINING(29, 3, "MetalMining", new int[]{864, 2512, 4023, 5760, 8280, 11178, 14364}, 7, new int[]{864, 3376, 3017, 4320, 4140, 5589, 7182}, new int[]{0, 241, 1005, 1440, 4140, 4471, 3591}, new int[]{0, 0, 0, 0, 0, 1117, 3591}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	MILITARY_FORT(30, 3, "MilitaryFort", new int[]{855, 3258, 4860, 6120, 7785, 9180, 10836}, 7, new int[]{855, 1629, 2430, 3060, 3892, 4590, 5418}, new int[]{0, 1629, 2430, 3060, 3892, 4590, 5418}, new int[]{0, 0, 0, 0, 0, 0, 0}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	MOAT(31, 1, "Moat", new int[]{240}, 1, new int[]{120}, new int[]{0}, new int[]{120}, true, true, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, false, '=', false),
	NOBLE_MANOR(32, 3, "NobleManor", new int[]{1431, 2628, 4023, 6408, 9135, 11232, 13545}, 7, new int[]{1073, 1971, 3017, 3204, 4567, 5616, 6772}, new int[]{357, 657, 1005, 3204, 3653, 4492, 3386}, new int[]{0, 0, 0, 0, 913, 1123, 3386}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	PORTAL_GATE(33, 3, "PortalGate", new int[]{2097, 3438, 5238, 6984, 9585, 10314, 15561}, 7, new int[]{1048, 1719, 2619, 3492, 4792, 5157, 7780}, new int[]{524, 1375, 2095, 2793, 3833, 4125, 0}, new int[]{524, 343, 523, 698, 958, 1031, 7780}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	ROAD(34, 1, "Road", new int[]{101, 320, 537, 681, 1135, 1392, 1743}, 7, new int[]{101, 240, 268, 454, 567, 696, 871}, new int[]{0, 80, 268, 227, 283, 348, 0}, new int[]{0, 0, 0, 227, 283, 348, 871}, true, true, new int[]{1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7, 1, 2, 3, 4, 5, 6, 7}, false, '#', true),
	SANCTUARY(35, 3, "Sanctuary", new int[]{2034, 3906, 5886, 8208, 9945, 11880, 14238}, 7, new int[]{1017, 1953, 2943, 4104, 4972, 5940, 7119}, new int[]{508, 976, 1471, 2052, 2486, 2970, 3559}, new int[]{508, 976, 1471, 2052, 2486, 2970, 3559}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	SAWMILL(36, 3, "Sawmill", new int[]{828, 2322, 4266, 6156, 8910, 10800, 14742}, 7, new int[]{828, 3250, 3199, 3078, 4455, 5400, 7371}, new int[]{0, 232, 1066, 3078, 3564, 4320, 3685}, new int[]{0, 0, 0, 0, 891, 1080, 3685}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	SIEGE_TOWER_WORKSHOP(37, 3, "SiegeTowerWorkshop", new int[]{855, 2304, 4644, 6120, 8370, 11016, 12285}, 7, new int[]{855, 3225, 2322, 3060, 4185, 5508, 6142}, new int[]{0, 230, 2322, 3060, 4185, 4406, 4913}, new int[]{0, 0, 0, 0, 0, 1101, 1228}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	STABLE(38, 3, "Stable", new int[]{900, 2376, 3888, 6408, 8100, 10962, 14490}, 7, new int[]{900, 3326, 2916, 3204, 4050, 5481, 7245}, new int[]{0, 237, 972, 3204, 4050, 4384, 3622}, new int[]{0, 0, 0, 0, 0, 1096, 3622}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	STONE_QUARRY(39, 3, "StoneQuarry", new int[]{909, 2142, 3969, 6696, 8235, 11070, 14931}, 7, new int[]{909, 2998, 2976, 3348, 4117, 5535, 7465}, new int[]{0, 214, 992, 3348, 4117, 4428, 3732}, new int[]{0, 0, 0, 0, 0, 1107, 3732}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	TAVERN(40, 3, "Tavern", new int[]{828, 2142, 3834, 6588, 9000, 11340, 12033}, 7, new int[]{828, 2998, 2875, 3294, 4500, 5670, 6016}, new int[]{0, 214, 958, 3294, 3600, 4536, 4812}, new int[]{0, 0, 0, 0, 900, 1134, 1203}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	TOWNHALL(41, 7, "Townhall", new int[]{9947, 20384, 29400, 40180, 56840, 67326, 75803, 88200, 99666, 113190, 127204, 133476}, 12, new int[]{4973, 10192, 14700, 20090, 28420, 33663, 37901, 44100, 49833, 56595, 63602, 66738}, new int[]{3978, 8153, 11760, 16072, 14210, 16831, 18950, 22050, 24916, 28297, 31801, 33369}, new int[]{994, 2038, 2940, 4018, 14210, 16831, 18950, 22050, 24916, 28297, 31801, 33369}, false, false, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, true, '=', true),
	TRADE_ROUTE(42, 1, "TradeRoute", new int[]{105}, 1, new int[]{105}, new int[]{0}, new int[]{0}, false, true, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, false, '#', true),
	TREBUCHET_WORKSHOP(43, 3, "TrebuchetWorkshop", new int[]{900, 2268, 4239, 6408, 7425, 11178, 11970}, 7, new int[]{900, 3175, 3179, 3204, 3712, 5589, 5985}, new int[]{0, 226, 3179, 3204, 3712, 4471, 4788}, new int[]{0, 0, 0, 0, 0, 1117, 1197}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	UNIVERSITY(44, 5, "University", new int[]{2775, 7750, 11700, 18100, 21857, 29400, 39725}, 7, new int[]{2775, 5812, 8775, 9050, 10937, 14700, 19862}, new int[]{0, 1937, 2925, 9050, 10937, 11760, 9931}, new int[]{0, 0, 0, 0, 0, 2940, 9931}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	WALL(45, 1, "Wall", new int[]{186, 596, 1020, 1592, 2280, 2880}, 6, new int[]{186, 446, 510, 796, 1140, 1440}, new int[]{0, 148, 510, 636, 570, 0}, new int[]{0, 0, 0, 158, 570, 1440}, true, true, new int[]{1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6}, false, 'W', false),
	WATCH_TOWER(46, 1, "WatchTower", new int[]{1269, 2808, 5049, 6696, 9135, 10260, 15750}, 7, new int[]{951, 2106, 2524, 3348, 4567, 5130, 7875}, new int[]{317, 702, 2524, 3348, 3653, 4104, 0}, new int[]{0, 0, 0, 0, 913, 1026, 7875}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '@', true),
	WATER_TEMPLE(47, 3, "WaterTemple", new int[]{2097, 4032, 5859, 7740, 10080, 11934, 16254}, 7, new int[]{1048, 2016, 2929, 3870, 5040, 5967, 8127}, new int[]{524, 1008, 1464, 1935, 2520, 2983, 0}, new int[]{524, 1008, 1464, 1935, 2520, 2983, 8127}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	ARMY(48, 1, "Army", new int[]{100}, 1, new int[]{0}, new int[]{0}, new int[]{0}, false, false, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, false, '%', false),
	WIND_TEMPLE(49, 3, "WindTemple", new int[]{1566, 3060, 5319, 7848, 10395, 11934, 14553}, 7, new int[]{783, 1530, 2659, 3924, 5197, 5967, 7276}, new int[]{783, 1530, 2127, 1962, 2598, 2983, 3638}, new int[]{0, 0, 531, 1962, 2598, 2983, 3638}, true, false, new int[]{1, 2, 3, 4, 5, 6, 7}, false, '=', true),
	LAKE(50, 1, "Lake", new int[]{240}, 1, new int[]{240}, new int[]{0}, new int[]{0}, false, false, new int[]{1}, false, '0', false);
	private final int id;
	private final int length;
	private final String name;
	private final int[] hp;
	private final int lvl;
	private final int[] moneycost;
	private final int[] woodcost;
	private final int[] stonecost;
	private final boolean buildable;
	private final boolean reshapeable;
	private final int[] graphics;
	private final boolean villagerholder;
	private final char symbol;
	private final boolean road;
	private BuildingType(final int id, final int length, final String name, final int[] hp, final int lvl, final int[] moneycost, final int[] woodcost, final int[] stonecost, final boolean buildable, final boolean reshapeable, final int[] graphics, final boolean villagerholder, final char symbol, final boolean road){
		this.id=id;
		this.length=length;
		this.name=name;
		this.hp=hp;
		this.lvl=lvl;
		this.moneycost=moneycost;
		this.buildable=buildable;
		this.woodcost=woodcost;
		this.stonecost=stonecost;
		this.reshapeable=reshapeable;
		this.graphics=graphics;
		this.villagerholder=villagerholder;
		this.symbol=symbol;
		this.road=road;
	}
	public static BuildingType getById(int id){
		for(BuildingType type : BuildingType.values()){ if(type.id==id)return type; }
		return null;
	}
	public static BuildingType getByName(String name){
		for(BuildingType type : BuildingType.values()){ if(type.name.equalsIgnoreCase(name))return type; }
		return null;
	}
	public BuildBuilding hasRoomToBuildFromNwc(Kingdom kingdom, Chunk nwc){
		final int h = PlotMg.getPlotAt(nwc).getHeight();
		Plot plot;
		Kingdom overall = null;
		BuildBuilding b = new BuildBuilding();
		for(int x = nwc.getX(); x<nwc.getX()+this.length; x++){
			for(int z = nwc.getZ(); z<nwc.getZ()+this.length; z++){
				plot=PlotMg.getPlotAt(nwc.getWorld(), x, z);
				if(plot.isKingdomPlot()){
					if(overall==null)overall=plot.getKingdom();
					if(plot.getBuilding().isBuildingOrUpgrading())return b;
					if(overall!=kingdom)return b;
					if(!(plot.getKingdom().equals(overall)
							&&plot.getBuilding().getType().equals(BuildingType.LAND)))return b;
					if(plot.getHeight()!=h)return b;
				}else return b;
			}
		}
		b.canbuild=true;
		b.kingdom=(overall==null?kingdom:overall);
		return b;
	}
	public boolean hasResourcesToBuild(Kingdom kingdom, int lvl){
		try{
			int gc = moneycost[lvl-1];
			int wc = woodcost[lvl-1];
			int sc = stonecost[lvl-1];
			if(kingdom.getMoney()<gc)return false;
			if(kingdom.getResourceLevels(ResourceType.WOOD)<wc)return false;
			if(kingdom.getResourceLevels(ResourceType.STONE)<sc)return false;
			return true;
		}catch(final Exception exception){ exception.printStackTrace(); }
		return false;
	}
	public int getGraphicsLevelFor(ArmyFormation form, int lvl){
		if(this==BuildingType.GATEHOUSE){
			if(form.usesSide(Direction.EAST)
					||form.usesSide(Direction.WEST))return lvl;
			return lvl+6;
		}
		if(isReshapeable())return lvl+(this.lvl*form.getId());
		return lvl;
	}
	public int[] getCost(int lvl){ return new int[]{this.moneycost[lvl-1], this.woodcost[lvl-1], this.stonecost[lvl-1]}; }
	public boolean isReshapeable(){ return this.reshapeable; }
	public int getLevelForGraphics(int g){ return this.graphics[g]; }
	public int[] getGraphicLevels(){ return this.graphics; }
	public boolean canHoldVillagers(){ return this.villagerholder; }
	public char getTextSymbol(){ return symbol; }
	public int getId(){ return this.id; }
	public int getLength(){ return this.length; }
	public String getName(){ return this.name; }
	public int getMaxHp(int lvl){ return this.hp[lvl-1]; }
	public int getMaxLevel(){ return this.lvl; }
	public boolean canUpgrade(int lvl){ return lvl<getMaxLevel(); }
	public boolean canBeBuilt(){ return this.buildable; }
	public BuildBuilding hasRoomToBuildFromCenter(Kingdom kingdom, Chunk nwc){ return hasRoomToBuildFromNwc(kingdom, nwc.getWorld().getChunkAt(nwc.getX()-((this.length-1)/2), nwc.getZ()-((this.length-1)/2))); }
	public boolean needsRoad(){ return road; }
	public static class BuildBuilding{
		public boolean canbuild;
		public Kingdom kingdom;
	}
}