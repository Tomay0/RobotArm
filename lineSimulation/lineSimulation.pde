import java.util.*;

ArrayList<ArrayList<PVector>> lines1 = new ArrayList<ArrayList<PVector>>();

PVector[][] lines2 = new PVector[1][2];

PImage pencil;

int t = 0;

void setup() {
  size(1000,800);
  //fullScreen();
  frameRate(144);
  pencil = loadImage("pencil.png");
  try {
    //scan to find all lines
    Scanner scan = new Scanner(new File(dataPath("test.txt")));
    int nLines = 0;
    while(scan.hasNextLine()) {
      String lineString = scan.nextLine();
      Scanner lineScan = new Scanner(lineString);
      ArrayList<PVector> line = new ArrayList<PVector>();
      
      //get all points on the line
      while(lineScan.hasNext()) {
        String[] pointString = lineScan.next().split(",");
        line.add(new PVector(float(pointString[0]),float(pointString[1])));
      }
      
      
      //add the line to list of lines
      lines1.add(line);
      lineScan.close();
      
      //add to line count
      if(line.size()>1) nLines+=line.size()-1;
    }
    scan.close();
    
    //build line array
    lines2 = new PVector[nLines][2];
    
    int lineN = 0;
    for(ArrayList<PVector> line : lines1) {
      for(int i = 0;i<line.size()-1;i++) {
        PVector p1 = line.get(i);
        PVector p2 = line.get(i+1);
        
        lines2[lineN][0] = p1;
        lines2[lineN][1] = p2;
        lineN++;
      }
    }
    
    
  }catch(Exception e) {
    e.printStackTrace();
  }
}

void draw() {
  translate(100,300);
  scale(200, 200);
  background(0);
  strokeWeight(0.005);
  stroke(255);
  
  for(int i = 0;i<t && i<lines2.length;i++) {
    //stroke(i%255,255,150);
    line(lines2[i][0].x,lines2[i][0].y,lines2[i][1].x,lines2[i][1].y);
    if(i==t-1) {
      //image(pencil,lines2[i][1].x,lines2[i][1].y);
    }
  }
  t += 20;
}
