package iterativeDeepening;

import java.util.List;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


public class iddfs {
    public static void main(String args[]){
        // data structure to construct graph and vertex
        HashMap<Character, List<Character>> graph = new HashMap<>();
        HashMap<Character, Integer> vertices = new HashMap<>();
        // store command
        String[] commandInfo = new String[]{"", ""};
        String flag = null;
        int targetValue = 0;

        try{
            // EXTRACT INPUT
            readInput(graph, vertices, commandInfo);
            flag = commandInfo[0];
            targetValue = Integer.parseInt(commandInfo[1]);
        
            System.out.println(flag);
            System.out.println(targetValue);
            System.out.println(graph);
            System.out.println(vertices);

        } catch (FileNotFoundException e){
            System.err.println("Here1");
            System.exit(0);
        }

        // Verbose Option
        if (flag.equals("V")){
            verbose(graph, vertices, targetValue);
        }
        // Compact Option
        else if(flag.equals("C")){
            compact(graph, vertices, targetValue);
        }
    }
    
    public static void readInput(HashMap<Character, List<Character>> graph, 
    HashMap<Character, Integer> vertices, String[] commandInfo) throws FileNotFoundException{
        // construct default empty graph 
        List<Character> allVertices = new ArrayList<>();
        File graphFile = new File("iterativeDeepening/input.txt");
        try{
            // 1) Extract command print style and targetValue
            Scanner inFile = new Scanner(graphFile);
            String firstLine = inFile.nextLine().trim();
            String typeOfResult = firstLine.split(" ", -1)[1];
            String targetString = firstLine.split(" ", -1)[0];
            commandInfo[0] = typeOfResult;
            commandInfo[1] = targetString;
            // 2) Add vertices to vertices list and graph
            while (inFile.hasNextLine()){
                String currentLine = inFile.nextLine().trim();
                if (currentLine.equals("")){
                    break;
                }
                Character label = currentLine.split(" ", -1)[0].charAt(0);
                List<Character> adjList = new ArrayList<>();
                String valueInString  = currentLine.split(" ", -1)[1];
                int value = Integer.parseInt(valueInString);
                // add vertex to list
                vertices.put(label, value);
                // add vertex to graph
                graph.put(label, adjList);
                allVertices.add(label);
            }
            // 3) Add all vertices to each vertex adj list
            for (Character c : graph.keySet()){
                for (Character a : allVertices){
                    if (!a.equals(c)){
                        graph.get(c).add(a);
                    }
                }
            }
            // 4) Read edges and remove ones with edges from adj list
            while (inFile.hasNextLine()){
                String currentLine = inFile.nextLine().trim();
                if (currentLine.equals("")){
                    break;
                }
                Character vertex1 = currentLine.split(" ", -1)[0].charAt(0);
                Character vertex2 = currentLine.split(" ", -1)[1].charAt(0);
                graph.get(vertex1).remove(vertex2);
                graph.get(vertex2).remove(vertex1);
            }
            inFile.close();
        } catch (FileNotFoundException e) {
            System.err.println("Here3");
            System.exit(0);
        }
        return;
    }


    // ACTUAL ALGO
    public static void verbose(HashMap<Character, List<Character>> graph, 
    HashMap<Character, Integer> vertices, int goal){   
        // depth is bounded by n (for n vertices)
        int maximalDepth = vertices.size();
        // loop through each depth
        for (int currentDepth = 0; currentDepth <= maximalDepth; currentDepth++){
            // print depth level
            System.out.println("\nDepth=" + currentDepth + ".");
            String answer = "";
            answer = dfs(graph, vertices, goal, currentDepth);
            // IF SOLUTION FOUND
            if(calcValue(vertices, answer)>=goal){
                System.out.println("\nFound solution " + answer);
                System.out.println(calcValue(vertices, answer));
                break;
            }
            
        }
        return;   
    }

    public static String dfs(HashMap<Character, List<Character>> graph, 
    HashMap<Character, Integer> vertices, int goal, int maxDepth){
        String state = "";
        return dfs1(state, graph, vertices, goal, maxDepth);
    }
    public static String dfs1(String state, HashMap<Character, List<Character>> graph, 
    HashMap<Character, Integer> vertices, int goal, int maxDepth){
        //System.out.println("state: " + state);
        // BC1 - check for goal state
        if(calcValue(vertices, state)>= goal){
            return state;
        }

        // find successors to state exactly once
        for (int i = 0; i < maxDepth; i++){
            List<String> successors = new ArrayList<>();
            successors = getSuccessors(state, graph);
            // BC2 - if no successors
            if (successors.size() == 0){
                System.out.println(state);
                return state;
            }
            for (String successor : successors){
                System.out.println(successor);
                //dfs1(successor)
                String ans = dfs1(successor, graph, vertices, goal, maxDepth-1);
                // if ans meets goal
                if (calcValue(vertices, ans) >= goal){
                    return ans;
                }
            }
        }
        return state;
    }

    public static List<String> getSuccessors(String state, HashMap<Character, List<Character>> graph){
        List<String> successors = new ArrayList<>();
        // if starting state
        if (state.length() == 0){
            // for each vertex/state
            for (Character c : graph.keySet()){
                String singleV = c.toString();
                successors.add(singleV);
            }
        }
        // if not starting state
        else{
            // for each vertex adjacent to current state's last vertex 
            for(Character c : graph.get(state.charAt(state.length()-1))){
                // if vertex isnt already in chain
                if(!state.contains(c.toString().trim())){
                    // add to successors
                    String successor = state + c.toString().trim();
                    successors.add(successor);
                }
            }
        }
        
        return successors;
    }

    public static int calcValue(HashMap<Character, Integer> vertices, String state){
        if(state.equals(null)){
            return 0;
        }
        if(state.length() == 0 || state == ""){
            return 0;
        }
        else{
            int total = 0;
            // iterate over each vertex in state
            for (int i = 0; i < state.length(); i++){
                total += vertices.get(state.charAt(i));
            }
            return total;
        }
    }

    public static String formatStateForPrint(String state){
        String formatState = "";
        for (int i = 0; i < state.length(); i++){
            if(i == state.length()-1){
                formatState += state.charAt(i);
            }
            else{
                formatState += state.charAt(i) + " ";
            }
        }
        return formatState;
    }

    public static void compact(HashMap<Character, List<Character>> graph, HashMap<Character, Integer> vertices, int goal){

    }
}
