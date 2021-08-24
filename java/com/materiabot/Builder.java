package com.materiabot;
import com.materiabot.GameElements.Unit;
import com.materiabot.GameElements.Enumerators.Ability.AttackName;
import com.materiabot.Utils.Constants;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Builder {
	public static void main(String[] args) throws Exception {
		PluginManager.loadUnits();
		File output = new File(".\\resources\\builder");
		if(!output.exists())
			output.mkdir();
		String defaultText = Files.readAllLines(Paths.get(".\\resources\\builder\\_example.txt"), StandardCharsets.UTF_8).stream().reduce((s1,  s2) -> s1 + System.lineSeparator() + s2).get();
		List<Unit> units = Constants.UNITS.stream()
									//.flatMap(c -> c.getAllUnits().stream())
									.map(c -> _Library.L.getUnit(c.getName()))
									.sorted((u1, u2) -> u1.getSeries() - u2.getSeries()).collect(Collectors.toList());
		File out2 = new File(".\\resources\\builder\\_java.java");
		@SuppressWarnings("resource")
		FileWriter fw2 = new FileWriter(out2);
		for(Unit uu : units) {
			Unit u = _Library.L.getUnit(uu.getName());
			if(u == null) {
				System.out.println("ERROR: " + uu.getName());
				return;
			}
			String cleanName = u.getName().replace(" ", "_").replace("'", "").replace("&", "");
			String nicks = u.getNicknames().stream().filter(n -> !n.equalsIgnoreCase(u.getName())).map(s -> "\"" + s + "\"").reduce((s1, s2) -> s1 + ", " + s2).orElse("");
			if(nicks.length() > 0)
				nicks = ", " + nicks;
			fw2.write("new " + cleanName + "()," + System.lineSeparator());
			String unitText = defaultText.replace("{cleanName}", cleanName)
					.replace("{name}", u.getName())
					.replace("{nicks}", nicks)
					.replace("{brv}", u.getAbility(AttackName.BRV).stream().map(s -> ""+s.getId()).reduce((s1, s2) -> s1 + ", " + s2).orElse(""))
					.replace("{hp}", u.getAbility(AttackName.HP).stream().map(s -> ""+s.getId()).reduce((s1, s2) -> s1 + ", " + s2).orElse(""))
					.replace("{s1}", u.getAbility(AttackName.S1).stream().map(s -> ""+s.getId()).reduce((s1, s2) -> s1 + ", " + s2).orElse(""))
					.replace("{s2}", u.getAbility(AttackName.S2).stream().map(s -> ""+s.getId()).reduce((s1, s2) -> s1 + ", " + s2).orElse(""))
					.replace("{ex}", u.getAbility(AttackName.EX).stream().map(s -> ""+s.getId()).reduce((s1, s2) -> s1 + ", " + s2).orElse(""))
					.replace("{aa}", u.getAbility(AttackName.AA).stream().map(s -> ""+s.getId()).reduce((s1, s2) -> s1 + ", " + s2).orElse(""))
					.replace("{ld}", u.getAbility(AttackName.LD).stream().map(s -> ""+s.getId()).reduce((s1, s2) -> s1 + ", " + s2).orElse(""))
					.replace("{bt}", u.getAbility(AttackName.BT).stream().map(s -> ""+s.getId()).reduce((s1, s2) -> s1 + ", " + s2).orElse(""))
					.replace("{ca}", u.getAbility(AttackName.CA).stream().map(s -> ""+s.getId()).reduce((s1, s2) -> s1 + ", " + s2).orElse(""));
			new File(".\\resources\\builder\\" + u.getSeries()).mkdir();
			File out = new File(".\\resources\\builder\\" + u.getSeries() + "\\" + cleanName + ".java");
			FileWriter fw = new FileWriter(out);
			fw.write(unitText);
			fw.close();
		}
		fw2.close();
	}
}
