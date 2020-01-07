package com.materiabot.GameElements.Datamining;
import java.util.LinkedList;
import java.util.List;

public class Ailment {	
	public static enum Target {
		ST(1, "ST"),
		Self(2, "Self?"),
		AoE(5, "AoE"),
		Party(6, "Party"),
		Party2(7, "Party"),
		Ally(13, "Ally only");
		
		private int id;
		private String desc;
		
		private Target(int id, String desc) {this.id = id; this.desc = desc; }

		public int getId() {
			return id;
		}
		public String getDesc() {
			return desc;
		}
	}
	public static class Effect{
		public static enum EffectType{
			E2(2, "Self"),
			E3(3, "Party");
			
			private int id;
			private String name;

			private EffectType(int id, String name) {
				this.id = id;
				this.name = name;
			}
			
			public int getId() {
				return id;
			}
			public String getName() {
				return name;
			}

			public static EffectType get(int id) {
				for(EffectType e : values())
					if(e.getId() == id)
						return e;
				return null;
			}
		}
		public static enum BuffType{
			E2(2, "Self"),
			E3(3, "Party");
			
			private int id;
			private String name;

			private BuffType(int id, String name) {
				this.id = id;
				this.name = name;
			}
			
			public int getId() {
				return id;
			}
			public String getName() {
				return name;
			}

			public static BuffType get(int id) {
				for(BuffType e : values())
					if(e.getId() == id)
						return e;
				return null;
			}
		}
		public static enum EffectValType{
			E2(2, "Self"),
			E3(3, "Party");
			
			private int id;
			private String name;

			private EffectValType(int id, String name) {
				this.id = id;
				this.name = name;
			}
			
			public int getId() {
				return id;
			}
			public String getName() {
				return name;
			}

			public static EffectValType get(int id) {
				for(EffectValType e : values())
					if(e.getId() == id)
						return e;
				return null;
			}
		}
		
		private EffectType effectType;
		private BuffType buffType;
		private EffectValType effectValType;
		private int argument;
		
		public EffectType getEffectType() {
			return effectType;
		}
		public void setEffectType(EffectType effectType) {
			this.effectType = effectType;
		}
		public BuffType getBuffType() {
			return buffType;
		}
		public void setBuffType(BuffType buffType) {
			this.buffType = buffType;
		}
		public EffectValType getEffectValType() {
			return effectValType;
		}
		public void setEffectValType(EffectValType effectValType) {
			this.effectValType = effectValType;
		}
		public int getArgument() {
			return argument;
		}
		public void setArgument(int argument) {
			this.argument = argument;
		}
	}
	
	private int id, castId;
	private String name, desc;
	private int rate, rank, duration;
	private int[] args;
	private Target target;
	private List<Effect> effects = new LinkedList<Effect>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCastId() {
		return castId;
	}
	public void setCastId(int castId) {
		this.castId = castId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int[] getArgs() {
		return args;
	}
	public void setArgs(int[] args) {
		this.args = args;
	}
	public Target getTarget() {
		return target;
	}
	public void setTarget(Target target) {
		this.target = target;
	}
	public List<Effect> getEffects() {
		return effects;
	}
}