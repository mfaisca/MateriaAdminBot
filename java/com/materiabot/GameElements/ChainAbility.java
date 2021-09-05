package com.materiabot.GameElements;
import java.util.LinkedList;
import java.util.List;

public final class ChainAbility{
	private Unit unit;
	private int id;
	private int originalId, secondaryId;
	private boolean triggered = false, upgraded = false;
	private List<Integer> reqExtendPassives = new LinkedList<>();
	private List<Integer> reqWeaponPassives = new LinkedList<>();
	private List<MiscCondition> reqMiscConditions = new LinkedList<>();
	
	public Unit getUnit() { return unit; }
	public void setUnit(Unit unit) { this.unit = unit; }
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public int getOriginalId() { return originalId; }
	public void setOriginalId(int originalId) { this.originalId = originalId; }
	public int getSecondaryId() { return secondaryId; }
	public void setSecondaryId(int secondaryId) { this.secondaryId = secondaryId; }
	public boolean isTriggered() { return triggered; }
	public void setTriggered(boolean triggered) { this.triggered = triggered; }
	public boolean isUpgraded() { return upgraded; }
	public void setUpgraded(boolean upgraded) { this.upgraded = upgraded; }
	public List<Integer> getReqExtendPassives() { return reqExtendPassives; }
	public void setReqExtendPassives(List<Integer> reqExtendPassives) { this.reqExtendPassives = reqExtendPassives; }
	public List<Integer> getReqWeaponPassives() { return reqWeaponPassives; }
	public void setReqWeaponPassives(List<Integer> reqWeaponPassives) { this.reqWeaponPassives = reqWeaponPassives; }
	public List<MiscCondition> getReqMiscConditions() { return reqMiscConditions; }
	public void setReqMiscConditions(List<MiscCondition> reqMiscConditions) { this.reqMiscConditions = reqMiscConditions; }
}