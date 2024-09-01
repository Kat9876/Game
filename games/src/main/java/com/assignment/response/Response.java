package com.assignment.response;

import java.util.List;
import java.util.Map;

public class Response {
	
	public String[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(String[][] matrix) {
		this.matrix = matrix;
	}

	public double getReward() {
		return reward;
	}

	public void setReward(double reward) {
		this.reward = reward;
	}

	public Map<String, List<String>> getApplied_winning_combinations() {
		return applied_winning_combinations;
	}

	public void setApplied_winning_combinations(Map<String, List<String>> applied_winning_combinations) {
		this.applied_winning_combinations = applied_winning_combinations;
	}

	public String getApplied_bonus_symbol() {
		return applied_bonus_symbol;
	}

	public void setApplied_bonus_symbol(String applied_bonus_symbol) {
		this.applied_bonus_symbol = applied_bonus_symbol;
	}

	private String[][] matrix;
	
	private double reward;
	
	private Map<String,List<String>> applied_winning_combinations;
	
	private String applied_bonus_symbol;

}
