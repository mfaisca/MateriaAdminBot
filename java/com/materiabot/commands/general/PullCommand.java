package com.materiabot.commands.general;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.plugface.core.annotations.Plugin;
import com.materiabot._Library;
import com.materiabot.GameElements.Equipment;
import com.materiabot.GameElements.Equipment.Rarity;
import com.materiabot.GameElements.Unit;
import com.materiabot.IO.SQL.SQLAccess;
import com.materiabot.IO.SQL.SQLAccess.UserCosts;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.CooldownManager;
import com.materiabot.Utils.ImageUtils;
import com.materiabot.Utils.MessageUtils;
import com.materiabot.Utils.MessageUtils.Embed;
import com.materiabot.commands._BaseCommand;
import Shared.BotException;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Plugin(name = "Command.Pull")
public class PullCommand extends _BaseCommand{
	//https://old.reddit.com/r/DissidiaFFOO/comments/gfj0nh/draw_probabilities_in_the_btld_era/
	//https://old.reddit.com/r/DissidiaFFOO/comments/qiux6z/new_drop_tables_in_jp/
	
	private static String field1 = "unitld1";
	private static String field2 = "unitld2";
	private static String field3 = "unitex";
	
	public PullCommand() { super("pull", "PULLS!!!"); }
	private static class PullRate{
		public static class Rate{
			public static Rate ZERO = new Rate(0f);
			public static Rate BT = new Rate(0.1f, 3f);
			public static Rate FR = new Rate(1f);
			public static Rate LD = new Rate(1f);
			public static Rate LD_JP = new Rate(1.5f);
			public static Rate EX = new Rate(2.25f);
			public static Rate W35 = new Rate(3f);
			public static Rate W15 = new Rate(4.25f, 34.5f);
			public static Rate W4S = new Rate(30f, 0f);
			public static Rate W3S = new Rate(59.4f, 0f);
			public static Rate W3S_JP = new Rate(58.4f, 0f);
			
			public float normal, plus1;
			
			public Rate(float n) { this(n, n*10); }
			public Rate(float n, float p1) { normal = n; plus1 = p1; }
		}
		//GL
		public static PullRate RATE1 = new PullRate(1, 0, 2, 3, 4, Rate.BT, Rate.ZERO, Rate.LD, Rate.EX, Rate.W35, Rate.W15, Rate.W4S, Rate.W3S); //LC/Events
		public static PullRate RATE2 = new PullRate(1, 0, 2, 2, 3, Rate.BT, Rate.ZERO, Rate.LD, Rate.EX, Rate.W35, Rate.W15, Rate.W4S, Rate.W3S); //Special Banners
		public static PullRate RATE3 = new PullRate(1, 0, 2, 3, 3, Rate.BT, Rate.ZERO, Rate.LD, Rate.EX, Rate.W35, Rate.W15, Rate.W4S, Rate.W3S); //BT Releases
		
		//JP (FR Weapons Release)
		public static PullRate RATE4 = new PullRate(1, 1, 3, 3, 4, Rate.BT, Rate.ZERO, Rate.LD_JP, Rate.EX, Rate.W35, Rate.W15, Rate.W4S, Rate.W3S_JP); //LC/Events
		public static PullRate RATE5 = new PullRate(1, 1, 3, 3, 3, Rate.BT, Rate.FR, Rate.LD_JP, Rate.EX, Rate.W35, Rate.W15, Rate.W4S, Rate.W3S_JP); //BT Releases
		public static PullRate RATE6 = new PullRate(1, 1, 3, 3, 4, Rate.BT, Rate.FR, Rate.LD_JP, Rate.EX, Rate.W35, Rate.W15, Rate.W4S, Rate.W3S_JP); //BT Side Releases
		
		public static List<PullRate> RATES = Arrays.asList(RATE1, RATE2, RATE3, RATE4, RATE5, RATE6);
		
		public int btCount, frCount, ldCount, exCount, unitCount;
		public Rate pBT, pFR, pLD, pEX, p35, p15, p4s, p3s;
		
		public PullRate(int bC, int fC, int lC, int eC, int uC, Rate pB, Rate pF, Rate pL, Rate pE, Rate p3, Rate p1, Rate p4s, Rate p3s) {
			btCount = bC; frCount = fC; ldCount = lC; exCount = eC; unitCount = uC;
			pBT = pB; pFR = pF; pLD = pL; 
			pEX = pE; p35 = p3; p15 = p1;
			this.p4s = p4s; this.p3s = p3s;
		}
	}
	public static class Banner{
		public PullRate rate;
		public UserCosts userCosts = new UserCosts();
		public boolean gold = false, instant = false;
		
		public List<Unit> allUnits = new LinkedList<>();
		public List<Unit> btUnits = new LinkedList<>();
		public List<Unit> frUnits = new LinkedList<>();
		public List<Unit> ldUnits = new LinkedList<>();
		public List<Unit> exUnits = new LinkedList<>();

		public Banner() { }
		public void addUnits(SlashCommandEvent event) throws BotException {
			String unit1 = event.getOption(field1).getAsString().toLowerCase();
			String unit2 = event.getOption(field2).getAsString().toLowerCase() + " ld ex";
			String unit3 = event.getOption(field3).getAsString().toLowerCase();
			String btUnit = event.getOption("unitbt").getAsString().toLowerCase();
			String frUnit = event.getOption("unitfr") != null ? event.getOption("unitfr").getAsString().toLowerCase() : null;
			if(btUnit.equals(unit1)) {
				unit1 = null;
				if(btUnit.equals(frUnit))
					addUnit(btUnit + " bt fr ld ex");
				else {
					addUnit(btUnit + " bt ld ex");
					if(frUnit != null)
						addUnit(frUnit + " fr");
				}
				addUnit(unit2);
//				if(!unit3.equals("none")) {
				if(frUnit != null)
					addUnit(unit3 + " ld ex");
				else
					addUnit(unit3 + " ex");
//				}
			}
			else {
				if(unit1.equals(frUnit))
					addUnit(unit1 + " fr ld ex");
				else
					addUnit(unit1 + " ld ex");
				addUnit(unit2 + " ld ex");
//				if(!unit3.equals("none")) {
				if(frUnit != null)
					addUnit(unit3 + " ld ex");
				else
					addUnit(unit3 + " ex");
//				}
				addUnit(btUnit + " bt");
			}
		}
		public void addUnit(String s) throws BotException {
			boolean bt = false, fr = false, ld = false, ex = false;
			if(s.endsWith(" bt") || s.contains(" bt ") || s.endsWith(" burst") || s.contains(" burst ")){
				s = s.replace(" bt", "").trim();
				s = s.replace(" burst", "").trim();
				bt = true;
			}
			if(s.endsWith(" fr") || s.contains(" fr ")){
				s = s.replace(" fr", "").trim();
				fr = true;
			}
			if(s.endsWith(" ld") || s.contains(" ld ")){
				s = s.replace(" ld", "").trim();
				ld = true;
			}
			if(s.endsWith(" ex") || s.contains(" ex ")){
				s = s.replace(" ex", "").trim();
				ex = true;
			}
			Unit u = _Library.L.getUnit(s);
			if(u == null)
				throw new BotException("The unit '" + s + "' doesn't exist!");
			if(allUnits.contains(u))
				throw new BotException("You can't use the same unit twice!");
			if(bt) {
				if(u.getJP() != null && u.getJP().getEquipment(Rarity.W_BT) != null)
					btUnits.add(u.getJP());
				else if(u.getGL() != null && u.getGL().getEquipment(Rarity.W_BT) != null)
					btUnits.add(u.getGL());
				else
					throw new BotException("The unit '" + s + "' doesn't have a BT weapon");
			}
			if(fr) {
				if(u.getJP() != null && u.getJP().getEquipment(Rarity.W_FR) != null)
					frUnits.add(u.getJP());
				else if(u.getGL() != null && u.getGL().getEquipment(Rarity.W_FR) != null)
					frUnits.add(u.getGL());
				else
					throw new BotException("The unit '" + s + "' doesn't have a FR weapon");
			}
			if(ld) {
				if(u.getJP() != null && u.getJP().getEquipment(Rarity.W_LD) != null)
					ldUnits.add(u.getJP());
				else if(u.getGL() != null && u.getGL().getEquipment(Rarity.W_LD) != null)
					ldUnits.add(u.getGL());
				else
					throw new BotException("The unit '" + s + "' doesn't have a LD weapon");
			}
			if(ex) {
				if(u.getJP() != null && u.getJP().getEquipment(Rarity.W_EX) != null)
					exUnits.add(u.getJP());
				else if(u.getGL() != null && u.getGL().getEquipment(Rarity.W_EX) != null)
					exUnits.add(u.getGL());
				else
					throw new BotException("The unit '" + s + "' doesn't have an EX weapon");
			}
			allUnits.add(u);
			userCosts.bannerHash = bannerHash();
		}
		private float limitBT(boolean plus) {
			return (plus ? rate.pBT.plus1 : rate.pBT.normal);
		}
		private float limitFR(boolean plus) {
			return limitBT(plus) + (plus ? rate.pFR.plus1 : rate.pFR.normal);
		}
		private float limitLD(boolean plus) {
			return limitFR(plus) + (plus ? rate.pLD.plus1 : rate.pLD.normal);
		}
		private float limitEX(boolean plus) {
			return limitLD(plus) + (plus ? rate.pEX.plus1 : rate.pEX.normal);
		}
		private float limit35(boolean plus) {
			return limitEX(plus) + (plus ? rate.p35.plus1 : rate.p35.normal);
		}
		private float limit15(boolean plus) {
			return limit35(plus) + (plus ? rate.p15.plus1 : rate.p15.normal);
		}
		private float limit4S(boolean plus) {
			return limit15(plus) + (plus ? rate.p4s.plus1 : rate.p4s.normal);
		}
		private float limit3S(boolean plus) {
			return limit4S(plus) + (plus ? rate.p3s.plus1 : rate.p3s.normal);
		}
		public Equipment getTicketPull() throws BotException{ return getPull(false); }
		public Equipment getPlusPull() throws BotException { return getPull(true); }
		private Equipment getPull(boolean plus) throws BotException {
			float RNG = Shared.Methods.RNG.nextFloat() * 100f;
			List<Unit> validList = new LinkedList<>();
			Equipment.Rarity rarity2 = null;
			Equipment.Type type = null;
			if(RNG < limitBT(plus)){
				validList = btUnits;
				rarity2 = Equipment.Rarity.W_BT;
			}
			else if(RNG < limitFR(plus)){
				validList = frUnits;
				rarity2 = Equipment.Rarity.W_FR;
			}
			else if(RNG < limitLD(plus)){
				validList = ldUnits;
				rarity2 = Equipment.Rarity.W_LD;
			}
			else if(RNG < limitEX(plus)){
				validList = exUnits;
				rarity2 = Equipment.Rarity.W_EX;
			}
			else if(RNG < limit35(plus)){
				validList = exUnits;
				rarity2 = Equipment.Rarity.W_35;
			}
			else if(RNG < limit15(plus)){
				validList = exUnits;
				rarity2 = Equipment.Rarity.W_15;
			}
			else if(RNG < limit4S(plus)){
				type = Equipment.Type.random4Star();
				rarity2 = Equipment.Rarity.W_4S;
			}
			else if(RNG < limit3S(plus)){
				type = Equipment.Type.random3Star();
				rarity2 = Equipment.Rarity.W_3S;
			}
			else {
				throw new BotException("Unexpected error calculating percentages");
			}
			Equipment.Rarity rarity = rarity2;
			if(type != null){
				Equipment e = new Equipment();
				e.setType(type);
				e.setRarity(rarity2);
				return e;
			}
			Equipment ret = validList.get(Shared.Methods.RNG.nextInt(validList.size()))
								.getEquipment().stream()
									.filter(e -> rarity == null || e.getRarity().equals(rarity))
									.findFirst().orElse(null);
			if(ret == null)
				throw new BotException("Unexpected error calculating equipment");
			return ret;
		}
		public PullRate getRates() {
			return PullRate.RATES.stream()
						.filter(pr -> pr.btCount == this.btUnits.size())
						.filter(pr -> pr.frCount == this.frUnits.size())
						.filter(pr -> pr.ldCount == this.ldUnits.size())
						.filter(pr -> pr.exCount == this.exUnits.size())
						.filter(pr -> pr.unitCount == this.allUnits.size()).findFirst().orElse(null);
		}
		public List<Equipment> getPulls() throws BotException{
			LinkedList<Equipment> ll = new LinkedList<>();
			for(int i = 0; i < 10; i++)
				ll.add(getTicketPull());
			ll.add(getPlusPull());
			ll.sort((e1, e2) -> e1.getRarity().getRarity() - e2.getRarity().getRarity());
			return ll;
		}
		public String getBannerUnitsEmotes() {
			return allUnits.stream().map(u -> ImageUtils.getEmoteText(u.getName()) + " "+ (btUnits.contains(u.getGL()) || btUnits.contains(u.getJP()) ? ImageUtils.getEmoteText(Equipment.Rarity.W_BT.getEmoteName()) : "")
																						+ (frUnits.contains(u.getGL()) || frUnits.contains(u.getJP()) ? ImageUtils.getEmoteText(Equipment.Rarity.W_FR.getEmoteName()) : "")
					 																	+ (ldUnits.contains(u.getGL()) || ldUnits.contains(u.getJP()) ? ImageUtils.getEmoteText(Equipment.Rarity.W_LD.getEmoteName()) : "")
					 																	+ (exUnits.contains(u.getGL()) || exUnits.contains(u.getJP()) ? ImageUtils.getEmoteText(Equipment.Rarity.W_EX.getEmoteName()) : "")
			).reduce((s1, s2) -> s1 + System.lineSeparator() + s2).orElse("---");
		}
		public int bannerHash() {
			return Math.abs(allUnits.stream().sorted((u1, u2) -> u1.getName().compareTo(u2.getName()))
									.map(u -> ImageUtils.getEmoteText(u.getName()) + ","+ (btUnits.contains(u.getGL()) || btUnits.contains(u.getJP()) ? Equipment.Rarity.W_BT.name() : "")
																						+ (frUnits.contains(u.getGL()) || frUnits.contains(u.getJP()) ? Equipment.Rarity.W_FR.name() : "")
																						+ (ldUnits.contains(u.getGL()) || ldUnits.contains(u.getJP()) ? Equipment.Rarity.W_LD.name() : "")
																						+ (exUnits.contains(u.getGL()) || exUnits.contains(u.getJP()) ? Equipment.Rarity.W_EX.name() : "")
			).reduce((s1, s2) -> s1 + "|" + s2).orElse("---").hashCode());
		}
		
		public void updateResources() {
			userCosts = SQLAccess.UserCosts.getUserCosts(userCosts);
		}
		public void saveResources(int ticket, int gems) {
			userCosts.gemCount += gems;
			userCosts.totalGems += gems;
			userCosts.ticketCount += ticket;
			userCosts.totalTickets += ticket;
			SQLAccess.UserCosts.saveUserCosts(userCosts);
		}
	}
	private static interface Layout{
//		public static class Test implements Layout{
//			private int numTests;
//			public Test(int test) { numTests = test; }
//
//			private static String test(Banner banner, long testCount) throws BotException {
//				long number5 = 0, number4 = 0, number3 = 0;
//				long numberBT = 0, numberLD = 0, numberEX = 0, number35 = 0, number15 = 0;
//				long numberOffB = 0;
//				int[] numberOf5sTotal = new int[12];
//				long time = System.currentTimeMillis();
//				
//				for(long i = 0; i < testCount; i++) {
//					//List<Equipment> l = Arrays.asList(banner.getPlusPull());
//					List<Equipment> l = banner.getPulls();
//					int count5 = (int) l.stream().filter(e -> e.getRarity().getRarity() == 5).count();
//					number5 += count5;
//					number4 += l.stream().filter(e -> e.getRarity().getRarity() == 4).count();
//					number3 += l.stream().filter(e -> e.getRarity().getRarity() == 3).count();
//					numberBT += l.stream().filter(e -> e.getRarity().equals(Equipment.Rarity.W_BT)).count();
//					numberLD += l.stream().filter(e -> e.getRarity().equals(Equipment.Rarity.W_LD)).count();
//					numberEX += l.stream().filter(e -> e.getRarity().equals(Equipment.Rarity.W_EX)).count();
//					number35 += l.stream().filter(e -> e.getRarity().equals(Equipment.Rarity.W_35)).count();
//					number15 += l.stream().filter(e -> e.getRarity().equals(Equipment.Rarity.W_15)).count();
//					numberOffB += l.stream().filter(e -> e.getRarity().equals(Equipment.Rarity.W_15)).filter(e -> e.getName() == null).count();
//					numberOf5sTotal[count5]++;
//				}
//				time = (System.currentTimeMillis() - time);
//				DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//				DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//				symbols.setGroupingSeparator('.');
//				formatter.setDecimalFormatSymbols(symbols);
//				formatter.applyPattern("###.####");
//				
//				String ret = ("Total Number of 5*: " + formatter.format(number5) + " (" + formatter.format((number5/(float)testCount)*10) + "%)");
//				ret += System.lineSeparator() + ("Total Number of 4*: " + formatter.format(number4) + " (" + formatter.format((number4/(float)testCount)*10) + "%)");
//				ret += System.lineSeparator() + ("Total Number of 3*: " + formatter.format(number3) + " (" + formatter.format((number3/(float)testCount)*10) + "%)");
//				ret += System.lineSeparator() + ("Total Number of BT: " + formatter.format(numberBT) + " (" + formatter.format((numberBT/(float)testCount)*10) + "%)");
//				ret += System.lineSeparator() + ("Total Number of LD: " + formatter.format(numberLD) + " (" + formatter.format((numberLD/(float)testCount)*10) + "%)");
//				ret += System.lineSeparator() + ("Total Number of EX: " + formatter.format(numberEX) + " (" + formatter.format((numberEX/(float)testCount)*10) + "%)");
//				ret += System.lineSeparator() + ("Total Number of 35: " + formatter.format(number35) + " (" + formatter.format((number35/(float)testCount)*10) + "%)");
//				ret += System.lineSeparator() + ("Total Number of 15: " + formatter.format(number15) + " (" + formatter.format((number15/(float)testCount)*10) + "%)");
//				ret += System.lineSeparator() + ("Total Number of 5* OffBanner: " + formatter.format(numberOffB) + " (" + formatter.format((numberOffB/(float)testCount)*10) + "%)");
//				for(int ii = 0; ii < numberOf5sTotal.length; ii++) {
//					if(numberOf5sTotal[ii] == 0) continue;
//					ret += System.lineSeparator() + ("Total Number of " + ii + "/11: " + formatter.format(numberOf5sTotal[ii]) + " (" + formatter.format((numberOf5sTotal[ii]/(float)testCount)*100) + "%)");
//				}
//				ret += System.lineSeparator() + ("Took " + (time/1000) + " seconds to calculate " + testCount + " pulls.");
//				return ret;
//			}
//			
//			@Override
//			public void sendResult(SlashCommandEvent event, Banner banner, List<Equipment> pulls) {
//				try {
//					MessageUtils.sendMessage(event.getHook(), "```" + test(banner, numTests) + "```");
//				} catch (BotException e) {
//					MessageUtils.sendStatusMessageError(event.getHook(), "Error calculating");
//				}
//			}
//		}
		public static class Ticket implements Layout{
			public void sendResult(SlashCommandEvent event, CompletableFuture<Message> m, Banner banner, List<Equipment> pulls) {
				banner.saveResources(pulls.size(), 0);
				DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
				DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
				symbols.setGroupingSeparator('.');
				formatter.setDecimalFormatSymbols(symbols);
				formatter.applyPattern("###.####");
				for(int i = banner.instant ? pulls.size() : 0; i <= pulls.size(); i++) {
					Embed builder = new Embed();
					builder.setAuthor(event.getMember().getEffectiveName(), null, event.getUser().getAvatarUrl());
					builder.addField("**Banner Units**", banner.getBannerUnitsEmotes(), true);
					builder.addField("**Banner Resources**", 	formatter.format(banner.userCosts.ticketCount) + " " + ImageUtils.getEmoteText("ticket") + System.lineSeparator() + 
							formatter.format(banner.userCosts.gemCount) + " " + ImageUtils.getEmoteText("gem"), true);
					builder.addField("**Total Resources:**", 	formatter.format(banner.userCosts.totalTickets) + " " + ImageUtils.getEmoteText("ticket") + System.lineSeparator() + 
							formatter.format(banner.userCosts.totalGems) + " " + ImageUtils.getEmoteText("gem"), true);
					List<String> ret = Stream.concat(	pulls.stream().limit(i).map(e -> ImageUtils.getEmoteText(e.getRarity().getEmoteName()) + 
										" " + (e.getUnit() != null ? ImageUtils.getEmoteText(e.getUnit().getName()) : "") + 
										" " + (e.getName() != null ? e.getName().getBest().substring(0, e.getName().getBest().contains("(") ? e.getName().getBest().indexOf("(") : e.getName().getBest().length()).trim() : (e.getRarity().equals(Equipment.Rarity.W_15) ? "5* OffBanner" : (e.getRarity().getRarity() + "* Trash")))), 
														pulls.stream().skip(i).map(e -> ImageUtils.getEmoteText(ImageUtils.Emotes.UNKNOWN_EMOTE.get()) + " ????????"))
										.flatMap(new Function<String, Stream<String>>() {
									        String column = "";
									        int count = 0;
									        public Stream<String> apply(String str) {
									            column += System.lineSeparator() + str;
									            count++;
									            if(count % 10 != 0 && count < (pulls.size()))
									            	return Stream.empty();
								            	String f = column;
								            	column = "";
								            	return Stream.of(f.trim());
									        };
									    }).collect(Collectors.toList());
					builder.addField(MessageUtils.S, ret.get(0), true);
					if(pulls.size() > 10)
						builder.addField(MessageUtils.S, ret.get(1), true);
					if(pulls.size() > 20)
						builder.addField(MessageUtils.S, ret.get(2), true);
					if(m == null)
						m = MessageUtils.sendEmbed(event.getHook(), builder);
					else
						m = MessageUtils.editMessage(m, builder);
					Constants.sleep(800);
				}
			}
		}
		public static class Multi implements Layout{
			@Override
			public void sendResult(SlashCommandEvent event, CompletableFuture<Message> m, Banner banner, List<Equipment> pulls) {
				if(banner.gold)
					pulls.sort((e1, e2) -> e1.getRarity().compareTo(e2.getRarity()));
				banner.saveResources(0, 5000 * (banner.gold ? 10 : 1));
				pulls.sort((e1, e2) -> e1.getRarity().getRarity() - e2.getRarity().getRarity());
				DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
				DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
				symbols.setGroupingSeparator('.');
				formatter.setDecimalFormatSymbols(symbols);
				formatter.applyPattern("###.####");
				for(int i = 0; i < 12; i++) {
					Embed builder = new Embed();
					if(i == 0) {
						builder.setFooter("");
						if(m == null)
							m = MessageUtils.sendEmbed(event.getHook(), builder);
						else
							m = MessageUtils.editMessage(m, builder);
						if(banner.instant) {
							i = 10; continue;
						}
						builder.setImage(null);
						builder.setFooter(null);
					}
					builder.setAuthor(event.getMember().getEffectiveName(), null, event.getUser().getAvatarUrl());
					builder.addField("**Banner Units**", banner.getBannerUnitsEmotes(), true);
					builder.addField("**Banner Resources**", 	formatter.format(banner.userCosts.ticketCount) + " " + ImageUtils.getEmoteText("ticket") + System.lineSeparator() + 
							formatter.format(banner.userCosts.gemCount) + " " + ImageUtils.getEmoteText("gem"), true);
					builder.addField("**Total Resources:**", 	formatter.format(banner.userCosts.totalTickets) + " " + ImageUtils.getEmoteText("ticket") + System.lineSeparator() + 
							formatter.format(banner.userCosts.totalGems) + " " + ImageUtils.getEmoteText("gem"), true);
					List<String> ret = Stream.concat(	
							pulls.stream().limit(i).map(e -> ImageUtils.getEmoteText(e.getRarity().getEmoteName()) + 
										" " + (e.getUnit() != null ? ImageUtils.getEmoteText(e.getUnit().getName()) : "") + 
										" " + (e.getName() != null ? e.getName().getBest().substring(0, e.getName().getBest().contains("(") ? e.getName().getBest().indexOf("(") : e.getName().getBest().length()).trim() : (e.getRarity().equals(Equipment.Rarity.W_15) ? "5* OffBanner" : (e.getRarity().getRarity() + "* Trash")))), 
														pulls.stream().skip(i).map(e -> ImageUtils.getEmoteText(ImageUtils.Emotes.UNKNOWN_EMOTE.get()) + " ????????"))
										.flatMap(new Function<String, Stream<String>>() {
									        String column = "";
									        int count = 0;
									        public Stream<String> apply(String str) {
									            column += System.lineSeparator() + str;
									            count++;
									            if(count % 5 != 0 && count < 10)
									            	return Stream.empty();
								            	String f = column;
								            	column = "";
								            	return Stream.of(f);
									        };
									    }).collect(Collectors.toList());
					builder.addField(MessageUtils.S, ret.get(0), true);
					builder.addField(MessageUtils.S, ret.get(1), true);
					builder.addField(MessageUtils.S, ret.get(2), true);
					MessageUtils.editMessage(m, builder);
					Constants.sleep(1600);
				}
			}
		}
//		public static class Simple implements Layout{
//			@Override
//			public void sendResult(SlashCommandEvent event, Banner banner, List<Equipment> pulls) {
//				String result = "**Pull by: **" + event.getMember().getEffectiveName() + "#" + event.getUser().getDiscriminator();
//				result += System.lineSeparator() + "**Banner Units: **" + banner.getBannerUnitsEmotes() + System.lineSeparator();
//				for(Equipment e : pulls) {
//					String item = ImageUtils.getEmoteText(e.getRarity().getEmoteName());
//					item += " " + (e.getUnit() != null ? ImageUtils.getEmoteText(e.getUnit().getName()) : ImageUtils.getEmoteText(e.getType().getEmote()));
//					item += " " + (e.getName() != null ? e.getName() : (e.getRarity().getRarity() + "* Trash"));
//					result += System.lineSeparator() + item;
//				}
//				MessageUtils.sendMessage(event.getHook(), result);
//			}
//		}		
		public void sendResult(SlashCommandEvent event, CompletableFuture<Message> m, Banner banner, List<Equipment> pulls);
	}

	private static HashMap<Long, Lock> guildLock = new HashMap<>();
	private static synchronized Lock getGuildLock(Long guildId) {
		guildLock.computeIfAbsent(guildId, k -> new ReentrantLock(true));
//		if(!guildLock.containsKey(guildId))
//			guildLock.put(guildId, new ReentrantLock(true));
		return guildLock.get(guildId);
	}

	@Override
	public void doStuff(SlashCommandEvent event) {
		Banner banner = new Banner();
		banner.userCosts.userId = event.getUser().getIdLong();
		banner.gold = event.getOption("gold") != null && event.getOption("gold").getAsBoolean();
		banner.instant = event.getOption("instant") != null && event.getOption("instant").getAsBoolean() && event.getUser().getIdLong() == Constants.QUETZ_ID;
		int tickets = event.getOption("tickets") != null ? (int)event.getOption("tickets").getAsLong() : -1;
		try {
			banner.addUnits(event);
		} catch (BotException e) {
			MessageUtils.sendStatusMessageError(event.getHook(), e.getMessage());
			resetCD(event);
			return;
		}
		banner.rate = banner.getRates();
		if(banner.rate == null) {
			MessageUtils.sendStatusMessageWarn(event.getHook(), "Units chosen don't have a valid pull rate, please check below for the valid options. If you feel that an option is missing, let Quetz know!");
			resetCD(event);
			return;
		}
		Lock lock = getGuildLock(event.getGuild().getIdLong());
		try {
			CompletableFuture<Message> m = null;
			if(!lock.tryLock()) {
				m = MessageUtils.sendStatusMessageInfo(event.getHook(), "You have been placed in queue.");
				lock.lock();
			}
			List<Equipment> res = new LinkedList<>();
			if(tickets > 0) {
				for(int t = 0; t < tickets; t++)
					if(banner.gold)
						res.add(banner.getPlusPull());
					else
						res.add(banner.getTicketPull());
			}
			else if(banner.gold)
				for(int t = 0; t < 11; t++)
					res.add(banner.getPlusPull());
			else
				res.addAll(banner.getPulls());
			banner.updateResources();
			Layout l = null;
			if(tickets > 0)
				l = new Layout.Ticket();
			else
				l = new Layout.Multi();
			if(l != null)
				l.sendResult(event, m, banner, res);
		} catch (BotException e) {
			MessageUtils.sendStatusMessageError(event.getHook(), e.getMessage());
			resetCD(event);
		} finally {
			lock.unlock();
		}
	}
	
	private static void resetCD(SlashCommandEvent event) {
		boolean gold = event.getOption("gold") != null && event.getOption("gold").getAsBoolean();
		CooldownManager.clearCooldowns(event.getUser(), gold ? CooldownManager.Type.GOLDPULL : CooldownManager.Type.PULL);
	}
	
	@Override
	public CooldownManager.Type getCooldown(SlashCommandEvent event) {
		if(event.getOption("gold") != null && event.getOption("gold").getAsBoolean())
			return CooldownManager.Type.GOLDPULL; 
		return CooldownManager.Type.PULL; 
	}
	
	@Override
	public CommandData getCommandData() {
		return super.getCommandData()
				.addOptions(new OptionData(OptionType.STRING, field1, "Unit 1", true))
				.addOptions(new OptionData(OptionType.STRING, field2, "Unit 2", true))
				//.addOptions(new OptionData(OptionType.STRING, field3, "Unit 3 ('None' if not applicable)", true))
				.addOptions(new OptionData(OptionType.STRING, field3, "Unit 3", true))
				.addOptions(new OptionData(OptionType.STRING, "unitbt", "BT Unit", true))
				.addOptions(new OptionData(OptionType.STRING, "unitfr", "FR Unit"))
				.addOptions(new OptionData(OptionType.INTEGER, "tickets", "Number of Tickets (Max 30)"))
				.addOptions(new OptionData(OptionType.BOOLEAN, "gold", "100% Gold Pull (Cheater!!!)"))
				.addOptions(new OptionData(OptionType.BOOLEAN, "instant", "Instant Pull (Do not use)"));
	}
}