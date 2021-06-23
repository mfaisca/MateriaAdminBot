package com.materiabot;

public class Effect_Analyzer {
	public static void main(String[] args) throws Exception {
		PluginManager.loadCommands();
		PluginManager.loadUnits();
		PluginManager.loadEffects();
		
//		UnitParser.UNITS.stream()
//				.peek(e -> System.out.println(e.getName()))
////				.map(u -> _Library.L.getUnit(u.getName()))
////				.peek(e -> System.out.println(e.getName()))
//				.filter(u -> u.getPassives().values().stream()
//						.flatMap(p -> p.getEffects().stream())
//						.anyMatch(e -> e.getEffect().getId() == 72))
//				.peek(e -> System.out.println(e.getName()))
//				.forEach(u -> System.out.println(u.getName()));

		_Library.L.getUnit("Queen").getPassives().values().stream().sorted().forEach(p -> {
			String text = System.lineSeparator() + String.format("%s (%d)%nLevel: %d | CP: %d%n%n%s", p.getName().getBest(), p.getId(), p.getLevel(), p.getCp(), p.generateDescription());
			System.out.println(text);
		});
	}
	
//	private static void unitEval(String unit) throws Exception {
//		Unit u = _Library.L.getUnit(unit);
//		int id = 11250;
//		System.out.println(u.getSpecificAbility(id).generateDescription() + "\n\n");
//		for(Ailment a : u.getSpecificAbility(id).getAilments())
//			System.out.println(a.generateDescription() + System.lineSeparator());
//		Constants.sleep(1000);
//	}
}