package com.assignment.input;

import java.util.List;

public class WinCombination {
	
	private double reward_multiplier;
	private String when;
	private String label;
	private double extra;
	private String group;
	private List<List<String>> covered_areas;
	private int count;
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public List<List<String>> getCovered_areas() {
		return covered_areas;
	}
	public void setCovered_areas(List<List<String>> covered_areas) {
		this.covered_areas = covered_areas;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getReward_multiplier() {
		return reward_multiplier;
	}
	public void setReward_multiplier(double reward_multiplier) {
		this.reward_multiplier = reward_multiplier;
	}
	public String getWhen() {
		return when;
	}
	public void setWhen(String when) {
		this.when = when;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public double getExtra() {
		return extra;
	}
	public void setExtra(double extra) {
		this.extra = extra;
	}
	

}
