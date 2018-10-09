import java.util.*;

ArrayList<ArrayList<PVector>> lines = new ArrayList<ArrayList<PVector>>();

void setup() {
  size(800,600);
  try {
    Scanner scan = new Scanner(new File(dataPath("lines.txt")));
    while(scan.hasNextLine()) {
      String lineString = scan.nextLine();
      Scanner lineScan = new Scanner(lineString);
      ArrayList<PVector> line = new ArrayList<PVector>();
      while(lineScan.hasNext()) {
        String[] pointString = lineScan.next().split(",");
        line.add(new PVector(float(pointString[0]),float(pointString[1])));
      }
      lines.add(line);
      lineScan.close();
    }
    
    scan.close();
  }catch(Exception e) {
    e.printStackTrace();
  }
  
}

void draw() {
  for(ArrayList<PVector> line : lines) {
    for(int i = 0;i<line.size()-1;i++) {
      PVector p1 = line.get(i);
      PVector p2 = line.get(i+1);
      
      line(p1.x,p1.y,p2.x,p2.y);
    }
  }
}
