package iterativeDeepening;

import java.util.List;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.PrintWriter;

import iterativeDeepening.Vertex;

public class Graph{
    public static void main(String args[]){
        // initialize default list of vertices (empty)
        HashMap<Vertex, List<Vertex>> graph = new HashMap<>();
        // to store command info: 1) typeOfResult; 2) targetValue
        String[] commandInfo = null;
        // Read input.txt
        try{
            commandInfo = parseCommand();

            System.out.println("Command: " + commandInfo[0]);
            System.out.println("Target: " + commandInfo[1]);

            graph = readGraph();
        } catch (FileNotFoundException e){
            System.err.println("Here1");
            
            System.exit(0);
        }

        // OPTION 1 - if output type = Verbose
        //if (commandInfo[0].equals("V")){

        //}
        
        System.out.println("Keys: " + graph.keySet());

        for (Vertex key : graph.keySet()){
            String label = key.getLabel();
            int value = key.getValue();
            System.out.println("label: " + label + "; value: " + value);
        }
        System.out.println("Values: " + graph.values());
    }

    public static String[] parseCommand() throws FileNotFoundException{
        String[] commandInfo = {null, null};
        File graphFile = new File("iterativeDeepening/input.txt");
        try{
            // PARSER
            Scanner inFile = new Scanner(graphFile);
            // STEP ONE - Read first line
            String firstLine = inFile.nextLine().trim();
            // read type of result
            String typeOfResult = firstLine.split(" ", -1)[1];
            // read target value
            String targetString = firstLine.split(" ", -1)[0];
            // Add to List 
            commandInfo[0] = typeOfResult;
            commandInfo[1] = targetString;

        } catch (FileNotFoundException e) {
            System.err.println("Here2");
            System.exit(0);
            // HANDLE ERROR IF FILE NOT FOUND
        }
        return commandInfo;
    }


    // readGraph will read input.txt, construct a hashmap of vertices and edges out of it, and then return it
    public static HashMap<Vertex, List<Vertex>> readGraph() throws FileNotFoundException{
        // construct default empty graph 
        HashMap<Vertex, List<Vertex>> graph = new HashMap<>();
        File graphFile = new File("iterativeDeepening/input.txt");
        try{
            // PARSER
            Scanner inFile = new Scanner(graphFile);
            // Skip first line
            inFile.nextLine();

            // STEP TWO - CONSTRUCT VERTICES
            while (inFile.hasNextLine()){
                
                String currentLine = inFile.nextLine().trim();
                if (currentLine.equals("")){
                    break;
                }
                System.out.println(currentLine);
                String label = currentLine.split(" ", -1)[0];
                String valueInString  = currentLine.split(" ", -1)[1];
                int value = Integer.parseInt(valueInString);

                // create new vertex for every line
                Vertex newVertex = new Vertex();
                newVertex.setLabel(label);
                newVertex.setValue(value);
                // add vertex to list
                graph.put(newVertex, null);
            }
            

        } catch (FileNotFoundException e) {
            System.err.println("Here3");
            System.exit(0);
        }
        
        


        // return graph
        return graph;
    }
}