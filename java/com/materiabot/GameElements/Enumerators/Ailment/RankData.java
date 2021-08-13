package com.materiabot.GameElements.Enumerators.Ailment;

public class RankData {
	private int id;
	private Integer[][] values;
	
	public RankData(int id) { this.id = id; }

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public Integer[][] getValues() { return values; }
	public void setValues(Integer[][] values) { this.values = values; }
	
	public boolean equals(Object other) {
		if(other == null || !other.getClass().equals(RankData.class))
			return false;
		return ((RankData)other).getId() == this.getId();
	}
}