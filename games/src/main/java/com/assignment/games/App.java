package com.assignment.games;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

import com.assignment.input.Input;
import com.assignment.input.StandardSymbol;
import com.assignment.input.Symbol;
import com.assignment.input.WinCombination;
import com.assignment.response.Response;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {

	public static void main(String[] args) throws IOException {
	//	String file = "C:/Users/katma/Downloads/take-home-assignment/config.json";
		String file = args[0];
		String json = readFileAsString(file);
		double betAmount = Double.parseDouble(args[1]);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Input input = objectMapper.readValue(json, Input.class);
		Map<String, Symbol> standardSymbols =
			    input.getSymbols().entrySet()
			         .stream()
			         .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
			         .filter(e -> "standard".equals(e.getValue().getType()))
			         .collect(Collectors.toMap(
			             Map.Entry::getKey,
			             Map.Entry::getValue
			         ));
		
		Map<String, Symbol> bonusSymbols =
			    input.getSymbols().entrySet()
			         .stream()
			         .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()))
			         .filter(e -> "bonus".equals(e.getValue().getType()))
			         .collect(Collectors.toMap(
			             Map.Entry::getKey,
			             Map.Entry::getValue
			         ));
		Response response = new Response();
		Map<String, Integer> probabilities = new HashMap<>();
		 List<StandardSymbol> standardSymbolsList = input.getProbabilities().getStandard_symbols();
	        for (StandardSymbol standardSymbol : standardSymbolsList) {
	        	probabilities.putAll(standardSymbol.getSymbols());
	        }
	        probabilities.putAll(input.getProbabilities().getBonus_symbols().getSymbols());
	       
	        String[][] matrix =  generateMatrix(3,3,probabilities, input.getSymbols());
	        Map<String, List<WinCombination>> rewardMultiplier =checkWinCombinations(matrix,input.getWin_combinations(),standardSymbolsList,bonusSymbols,response);
	        double reward = calculateTotalReward(rewardMultiplier,betAmount,standardSymbols,bonusSymbols);
	        
	       
	        response.setMatrix(matrix);
	        response.setReward(reward);
	        Map<String, List<String>> appliedWinningCombinations = new HashMap<>();
	        for(Map.Entry<String, List<WinCombination>> x : rewardMultiplier.entrySet()) {
	        	List<String> list = new ArrayList<>();
	        	x.getValue().forEach(y->{
	        		if(y.getLabel()!=null) {
	        			list.add(y.getLabel());
	        		}
	        	});
	        	if(!"BONUS".equals(x.getKey())) {
	        		appliedWinningCombinations.put(x.getKey(), list);
	        	}
	        	
			 }
			response.setApplied_winning_combinations(appliedWinningCombinations);
	        String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
	        System.out.println(prettyJson);
	}
	
	public static String readFileAsString(String file)
    {
        try {
			return new String(Files.readAllBytes(Paths.get(file)));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
	
	private static String[][] generateMatrix(int rows, int columns, Map<String, Integer> probabilities, Map<String, Symbol> symbols) {
		String[][] matrix = new String[rows][columns];
		//Getting total probability
		int totalProbability = probabilities.values().stream().mapToInt(Integer::intValue).sum();

		Random rand = new Random();
		
		for(int i=0;i<rows;i++) {
			for(int j=0; j<columns; j++) {
				//Getting a random value
				 double randomValue = rand.nextDouble(); // Generates a number between 0.0 and 1.0
			        double addedProbability = 0.0;
					for (Map.Entry<String, Integer> k : probabilities.entrySet()) {
						addedProbability += (double) k.getValue() / totalProbability;
						if (randomValue < addedProbability) {
							matrix[i][j] = k.getKey();
							break;
						}
					}
			}
		}
		return matrix;
	}
	
	private static double calculateTotalReward(Map<String, List<WinCombination>> rewardMultiplier, double betAmount, Map<String, Symbol> standardSymbols, Map<String, Symbol> bonusSymbols) {
		double total = 0;
		double extra = 0;
		double bonusMultiplier = 1;
		 for(Map.Entry<String, List<WinCombination>> x : rewardMultiplier.entrySet()) {
			 if("BONUS".equals(x.getKey())) {
				 
				 for(WinCombination bonus : x.getValue()) {
					 if(bonus.getExtra()!= 0) {
						 extra = extra + bonus.getExtra();
					 }
					 if(bonus.getReward_multiplier()!= 0) {
						 bonusMultiplier = bonusMultiplier * bonus.getReward_multiplier();
					 }
					
				 }
			 }else {
				 double multiplier = standardSymbols.get(x.getKey()).getReward_multiplier();
				 for(WinCombination winCombination:x.getValue()) {
					 multiplier *= winCombination.getReward_multiplier();
				 }
				 total += betAmount*multiplier;
			 }
		 }
		 total = total +extra;
		 total = total *bonusMultiplier;
		return total;
	}

	private static Map<String, List<WinCombination>> checkWinCombinations(String[][] matrix, Map<String, WinCombination> win_combinations, List<StandardSymbol> standardSymbolsList, Map<String, Symbol> bonusSymbols, Response response) {

		//double rewardMultiplier = 0;
		Map<String, List<WinCombination>> rewardMultiplier = new HashMap<>();
		Map<String, Integer> symbolCount = new HashMap<>();
		Map<String, List<String>> mappings = new HashMap<>();
        
        // Count occurrences of each symbol
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String symbol = String.valueOf(matrix[i][j]);
                symbolCount.put(symbol, symbolCount.getOrDefault(symbol, 0) + 1);
                List<String> list = new ArrayList<>();
                if(mappings.containsKey(symbol)) {
                	list = mappings.get(symbol);
                }
                list.add(i+":"+j);
                mappings.put(symbol, list);
            }
        }
        
        
        for(Map.Entry<String, Integer> x : symbolCount.entrySet()) {
        	List<WinCombination> winCombination = new ArrayList<>();
        	for (Map.Entry<String, WinCombination> entry : win_combinations.entrySet()) {
    			if("same_symbols".equalsIgnoreCase(entry.getValue().getGroup())&& entry.getValue().getCount()== x.getValue()) {
    				entry.getValue().setLabel(entry.getKey());
    				winCombination.add(entry.getValue());
    			}
    			if("linear_symbols".equalsIgnoreCase(entry.getValue().getWhen())) {
    				List<List<String>> coveredAreas = entry.getValue().getCovered_areas();
    				List<String> mapping = mappings.getOrDefault(x.getKey(), null);
    				if(mapping != null) {
    					
    					coveredAreas.forEach(y->{
    						if(mapping.containsAll(y)) {
    							entry.getValue().setLabel(entry.getKey());
    							winCombination.add(entry.getValue());
    						}
    					});
    				}
    			}
    		}
        	if(!winCombination.isEmpty()) {
        		rewardMultiplier.put(x.getKey(), winCombination);
        	}
        }
        List<String> bonusList = new ArrayList<>();
       
        if(!rewardMultiplier.isEmpty()) {
        	 List<WinCombination> bonus = new ArrayList<>();
             for(Entry<String, Symbol> bonusSymbol : bonusSymbols.entrySet()) {
             	if( symbolCount.containsKey(bonusSymbol.getKey())) {
             		WinCombination bonusWinCombination = new WinCombination();
             		if("multiply_reward".equalsIgnoreCase(bonusSymbol.getValue().getImpact())) {
             			bonusWinCombination.setReward_multiplier(bonusSymbol.getValue().getReward_multiplier());
                 		bonus.add(bonusWinCombination);
                 		bonusList.add(bonusSymbol.getKey());
             		}else if("extra_bonus".equalsIgnoreCase(bonusSymbol.getValue().getImpact())) {
             			bonusWinCombination.setExtra(bonusSymbol.getValue().getExtra());
                 		bonus.add(bonusWinCombination);
                 		bonusList.add(bonusSymbol.getKey());
             		}
             		
             	}
             }
             if(!bonus.isEmpty()) {
             	rewardMultiplier.put("BONUS", bonus);
             	response.setApplied_bonus_symbol(bonusList.toString());
             }
        }
       
        
       ;
        return rewardMultiplier;
	}

}
