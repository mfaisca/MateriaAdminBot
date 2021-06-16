package com.materiabot.GameElements;
import java.util.LinkedList;
import java.util.List;

public final class ChainAbility{
	private int id;
	private int originalId, secondaryId;
	private List<Integer> reqExtendPassives = new LinkedList<Integer>();
	private List<Integer> reqWeaponPassives = new LinkedList<Integer>();
	private List<MiscCondition> reqMiscConditions = new LinkedList<MiscCondition>();
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public int getOriginalId() { return originalId; }
	public void setOriginalId(int originalId) { this.originalId = originalId; }
	public int getSecondaryId() { return secondaryId; }
	public void setSecondaryId(int secondaryId) { this.secondaryId = secondaryId; }
	public List<Integer> getReqExtendPassives() { return reqExtendPassives; }
	public void setReqExtendPassives(List<Integer> reqExtendPassives) { this.reqExtendPassives = reqExtendPassives; }
	public List<Integer> getReqWeaponPassives() { return reqWeaponPassives; }
	public void setReqWeaponPassives(List<Integer> reqWeaponPassives) { this.reqWeaponPassives = reqWeaponPassives; }
	public List<MiscCondition> getReqMiscConditions() { return reqMiscConditions; }
	public void setReqMiscConditions(List<MiscCondition> reqMiscConditions) { this.reqMiscConditions = reqMiscConditions; }
}