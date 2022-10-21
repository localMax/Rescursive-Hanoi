import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

public class hanoi{
    private ArrayList<Deque<Integer>> pegs;
    private int numDisks;
    private int fastest;
    private ArrayList<String> fastestSol;

    public hanoi(int numDisks, int numPegs){
        fastest = Integer.MAX_VALUE;
        this.numDisks = numDisks;
        pegs = new ArrayList<>();
        for(int i = 0; i < numPegs; i++){
            pegs.add(new LinkedList<Integer>());
        }
        for(int j = 0; j < numDisks; j++){
            pegs.get(0).push(numDisks - j);
        }
        ArrayList<String> images = new ArrayList<>();
        images.add(this.toString());
        recursiveHanoi(0, images, -1, 0);
        System.out.println("MovesF: " + fastest + "\n" + fastestSol);
    }

    private void move(int start, int end){
        pegs.get(end).push(pegs.get(start).pop());
    }

    private boolean canMove(int start, int end){
        if(pegs.get(start).size() == 0){
            return false;
        }
        if(pegs.get(end).size() == 0){
            return true;
        }
        return pegs.get(end).peek() > pegs.get(start).peek();
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < pegs.size(); i++){
            sb.append(pegs.get(i)).append('\n');
        }
        return sb.toString();
    }

    private boolean complete(){
        for(int i = 0; i < pegs.size(); i++){
            if(pegs.get(i).size() == numDisks){
                return true;
            }
        }
        return false;
    }

    private boolean valid(ArrayList<String> images){
        for(int i = 0; i < images.size(); i++){
            if(images.get(i).split("\n")[0].equals("[]")){
                return true;
            }
        }
        return false;
    }

    private void recursiveHanoi(int moves, ArrayList<String>images, int lastMovedRing, int validIndex){
        //terminates if not the fastest solution
        if(moves > fastest){
            return;
        }
        if(validIndex == 0 && valid(images)){
            validIndex = moves;
        }
        //base case - solved
        if(validIndex != 0 && complete()){
            fastest = moves;
            ArrayList<String> cloneList = new ArrayList<>();
            for(String str: images){
                cloneList.add(str);
            }
            fastestSol = cloneList;
            System.out.println("Solution With: " + moves + " moves");
            return;
        }
        //infinite loop - move made that is exactly redundant
        if(redundant(images, validIndex)){
            return;
        }
        //try every move
        for(int i = 0; i < pegs.size(); i++){
            //empty
            if(pegs.get(i).size() == 0){
                continue;
            }
            // same ring
            int ring = pegs.get(i).peek();
            if(ring == lastMovedRing){
                continue;
            }
            for(int j = 0; j < pegs.size(); j++){
                //wont move it to its current location
                if(j == i){
                    continue;
                }
                if(canMove(i, j)){
                    move(i, j);
                    images.add(this.toString());
                    recursiveHanoi(moves+1, images, ring, validIndex);
                    //undo move
                    images.remove(images.size()-1);
                    move(j, i);
                }
            }
        }
    }

    private boolean redundant(ArrayList<String> images, int starting){
        if(images.size() == 0){
            return false;
        }
        String lastImage = images.get(images.size()-1);
        for(int i = starting; i < images.size() - 1; i++){
            if(symmetric(lastImage,images.get(i))){
                return true;
            }
        }
        return false;
    }

    private boolean symmetric(String image1, String image2){
        Set<String> imgSet1 = new HashSet<String>(Arrays.asList(image1.split("\n")));
        Set<String> imgSet2 = new HashSet<String>(Arrays.asList(image2.split("\n")));
        return imgSet1.equals(imgSet2);
        //System.out.println();
    }
    public static void main(String[] args){
        hanoi h = new hanoi(4,3);
        //System.out.println();
    }
}