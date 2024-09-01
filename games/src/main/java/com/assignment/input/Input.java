package com.assignment.input;

import java.util.Map;


public class Input {
	
	private int columns;
	private int rows;
	private Map<String, Symbol> symbols;
	private Probabilities probabilities;
	private Map<String, WinCombination> win_combinations;
	public int getColumns() {
		return columns;
	}
	public void setColumns(int columns) {
		this.columns = columns;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public Map<String, Symbol> getSymbols() {
		return symbols;
	}
	public void setSymbols(Map<String, Symbol> symbols) {
		this.symbols = symbols;
	}
	public Probabilities getProbabilities() {
		return probabilities;
	}
	public void setProbabilities(Probabilities probabilities) {
		this.probabilities = probabilities;
	}
	public Map<String, WinCombination> getWin_combinations() {
		return win_combinations;
	}
	public void setWin_combinations(Map<String, WinCombination> win_combinations) {
		this.win_combinations = win_combinations;
	}

}
