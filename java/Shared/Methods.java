package Shared;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Methods {
	private Methods() {}
	public static final Random RNG = new Random();

	public static final String urlizeDB(String s) {
		if(s.equals("leon")) s = "leonhart";
		return s.substring(0, s.contains("(") ? s.indexOf("(") : s.length()).replace("'", "").trim().replace(" ", "_");
	}
	public static final String urlizeTT(String s) {
		s = s.toLowerCase();
		if(s.equalsIgnoreCase("cecil")) s = "cecil-dark-knight";
		if(s.equalsIgnoreCase("leonhart")) s = "leon";
		if(s.equalsIgnoreCase("lann&reynn")) s = "lann-reynn";
		return s.substring(0, s.contains("(") ? s.indexOf("(") : s.length()).replace("'", "").trim().replace(" ", "-");
	}
//	public static final String urlizeDC(String s) {
//		s = s.toLowerCase();
//		if(s.equalsIgnoreCase("raines")) s = "cidraines";
//		if(s.equalsIgnoreCase("raines")) s = "cidhighwind";
//		if(s.equalsIgnoreCase("lann&reynn")) s = "lannreynn";
//		return s.substring(0, s.contains("(") ? s.indexOf("(") : s.length()).replace("'", "").trim().replace(" ", "");
//	}

	public static final String enframe(String s) { return "「**" + s + "**」"; }
	public static final String enframe(Integer i) { return enframe(""+i); }

	//	public static final LinkedList<String> splitString(String s, int size) {
	//		LinkedList<String> split = new LinkedList<String>();
	//		int spaceIndex = s.indexOf(" ");
	//		if(spaceIndex != -1)
	//			while(spaceIndex < s.length()) {
	//				int nextLineIndex = s.indexOf("\n", 0);
	//				if(nextLineIndex != -1 && nextLineIndex < size) {
	//					split.add(s.substring(0, nextLineIndex));
	//					split.addAll(splitString(s.substring(nextLineIndex + 1), size));
	//					return split;
	//				}
	//				int nextSpaceIndex = s.indexOf(" ", spaceIndex + 1);
	//				if(nextSpaceIndex == -1){
	//					if(s.length() < size)
	//						split.addFirst(s);
	//					else {
	//						split.add(s.substring(0, spaceIndex));
	//						split.add(s.substring(spaceIndex+1));
	//					}
	//					return split;
	//				}
	//				if(nextSpaceIndex < size)
	//					spaceIndex = nextSpaceIndex;
	//				else {
	//					split.add(s.substring(0, spaceIndex));
	//					split.addAll(splitString(s.substring(spaceIndex + 1), size));
	//					return split;
	//				}
	//			}
	//		split.add(s);
	//		return split;
	//	}
	//
	//	public static final String replaceLast(String string, String toReplace, String replacement) {
	//		int pos = string.lastIndexOf(toReplace);
	//		if (pos > -1)
	//			return string.substring(0, pos)
	//					+ replacement
	//					+ string.substring(pos + toReplace.length(), string.length());
	//		else
	//			return string;
	//	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>(); 
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null; 
	}

	public static final Integer[][] splitRankData(String[] rdd) {
		int[][] ret = new int[10][3];
		int idx = 2; int mult = 1;
		for(int i2 = 0; i2 < rdd.length; i2++) {
			String rd = rdd[i2];
			for(int i = rd.length()-1; i >= 0; i--) {
				char c = rd.charAt(i);
				if(c == '-') {
					ret[i2][idx] = -ret[i2][idx];
					idx--; mult = 1;
					continue;
				}
				if(mult == 1000) {
					idx--; mult = 1;
				}
				ret[i2][idx] += ((c - '0') * mult);
				mult *= 10;
			}
		}
		Integer[][] rett = new Integer[10][3];
		for(int i = 0; i < rett.length; i++) {
			rett[i] = new Integer[3];
			for(int i2 = 0; i2 < rett[i].length; i2++)
				rett[i][i2] = ret[i][i2];
		}
		return rett;
	}
	public static String timeDifference(Timestamp from, Timestamp to) {
		long s = (from.getTime() - to.getTime())/1000;
		long m = s / 60; s -= m * 60;
		long h = m / 60; m -= h * 60;
		long d = h / 24; h -= d * 24;
		String ret = "";
		if((m + h + d) == 0)
			return ret = ret.length() == 0 ? "less than a minute" : ret;
		if(d > 0)
			ret += d + " Day" + (d >= 2 ? "s, " : ", ");
		if(d == 0 && h == 0)
			ret += String.format("%02dm", m);
		else
			ret += String.format("%dh:%02dm", h, m);
		return ret;
	}
	public static final Integer[][] splitRankData(Integer[] rdd) {
		int[][] ret = new int[10][3];
		for(int i2 = 0; i2 < rdd.length; i2++) {
			String rd = ""+rdd[i2];
			int idx = 2; int mult = 1;
			for(int i = rd.length()-1; i >= 0; i--) {
				char c = rd.charAt(i);
				if(c == '-') {
					ret[i2][idx] = -ret[i2][idx];
					idx--; mult = 1;
					continue;
				}
				if(mult == 1000) {
					idx--; mult = 1;
				}
				ret[i2][idx] += ((c - '0') * mult);
				mult *= 10;
			}
		}
		Integer[][] rett = new Integer[10][3];
		for(int i = 0; i < rett.length; i++) {
			rett[i] = new Integer[3];
			for(int i2 = 0; i2 < rett[i].length; i2++)
				rett[i][i2] = ret[i][i2];
		}
		return rett;
	}
	
	public static <T> Collector<T, ?, List<T>> maxAll(Comparator<? super T> comparator) {
	    return maxAll(comparator, Collectors.toList());
	}
	private static <T, A, D> Collector<T, ?, D> maxAll(Comparator<? super T> comparator, Collector<? super T, A, D> downstream) {
		Supplier<A> downstreamSupplier = downstream.supplier();
		BiConsumer<A, ? super T> downstreamAccumulator = downstream.accumulator();
		BinaryOperator<A> downstreamCombiner = downstream.combiner();
		class Container {
			A acc;
			T obj;
			boolean hasAny;

			Container(A acc) {
				this.acc = acc;
			}
		}
		Supplier<Container> supplier = () -> new Container(downstreamSupplier.get());
		BiConsumer<Container, T> accumulator = (acc, t) -> {
			if(!acc.hasAny) {
				downstreamAccumulator.accept(acc.acc, t);
				acc.obj = t;
				acc.hasAny = true;
			} else {
				int cmp = comparator.compare(t, acc.obj);
				if (cmp > 0) {
					acc.acc = downstreamSupplier.get();
					acc.obj = t;
				}
				if (cmp >= 0)
					downstreamAccumulator.accept(acc.acc, t);
			}
		};
		BinaryOperator<Container> combiner = (acc1, acc2) -> {
			if (!acc2.hasAny) {
				return acc1;
			}
			if (!acc1.hasAny) {
				return acc2;
			}
			int cmp = comparator.compare(acc1.obj, acc2.obj);
			if (cmp > 0) {
				return acc1;
			}
			if (cmp < 0) {
				return acc2;
			}
			acc1.acc = downstreamCombiner.apply(acc1.acc, acc2.acc);
			return acc1;
		};
		Function<Container, D> finisher = acc -> downstream.finisher().apply(acc.acc);
		return Collector.of(supplier, accumulator, combiner, finisher);
	}
}