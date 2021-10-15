package com.materiabot;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements.Summon._Summon;
import com.materiabot.IO.JSON.UnitParser;

public class _Library {
	public static final _Library L = new _Library();

	private _Library() { //TODO Fazer no futuro scheduler método para ler todas as units que estão no $schedule para as manter em memoria 
		UNIT_CACHE = CacheBuilder.newBuilder()
				.expireAfterAccess(30, TimeUnit.MINUTES).build(new CacheLoader<String, Unit>(){
					@Override
					public Unit load(String key) throws Exception {
						if(key.toLowerCase().contains("_short"))
							return UnitParser.parseUnitQuick(key.replace("_short", ""));
						else
							return UnitParser.parseUnit(key);
					}
				});
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	private LoadingCache<String, Unit> UNIT_CACHE;
	private String name;
	public static List<_Summon> SUMMON_LIST = new LinkedList<>();

	public String getName() { return name; }
	public Unit getUnit(String u) {
		if(u == null) return null;
		Unit unit = null;
		try {
			unit = UNIT_CACHE.get(u);
			if(unit != null && !u.contains("_short"))
				UNIT_CACHE.invalidate(u + "_short");
		} catch (Exception e) {}
		return unit != null && unit.getName() != null ? unit : null;
	}
	public Unit getQuickUnit(String u) {
		if(u == null) return null;
		if(UNIT_CACHE.getIfPresent(u) != null)
			return getUnit(u);
		else
			return getUnit(u + "_short");
	}
	public void clearUnitCache() {
		_Library.L.UNIT_CACHE.invalidateAll();
	}
	
	public static _Summon getSummon(String summonName) {
		_Summon.getSummon(summonName);
		return SUMMON_LIST.stream().filter(s -> s.getNicknames().contains(summonName.toLowerCase())).findFirst().orElse(null);
	}
	public static List<_Summon> getAllSummon(String summonName) {
		_Summon.getSummon(summonName);
		return SUMMON_LIST.stream().filter(s -> s.getNicknames().contains(summonName.toLowerCase())).sorted((s1, s2) -> Integer.compare(s1.getMaxLevel(), s2.getMaxLevel())).collect(Collectors.toList());
	}
}