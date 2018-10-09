PVector leftShoulderPos = new PVector(220, 350);
PVector rightShoulderPos = new PVector(380, 350);
float humerusLenght = 80;

void setup(){
  size(600, 400);
}


void draw(){
  background(0);
  stroke(255);
  drawArms((float)mouseX / 50, (float)mouseY / 50);
  //drawArms(PI, PI);
}

void drawArms(float leftAngle, float rightAngle){
  PVector rightElbow = new PVector();
  PVector leftElbow = new PVector();
  PVector rightHand = new PVector();
  PVector leftHand = new PVector();

  rightElbow.x = (float)sin(rightAngle) * humerusLenght;
  rightElbow.y = (float)cos(rightAngle) * humerusLenght;
  leftElbow.x = (float)sin(leftAngle) * humerusLenght;
  leftElbow.y = (float)cos(leftAngle) * humerusLenght;
  
  rightElbow.add(rightShoulderPos);
  leftElbow.add(leftShoulderPos);
  
  line(leftShoulderPos.x, leftShoulderPos.y, leftElbow.x, leftElbow.y);
  line(rightShoulderPos.x, rightShoulderPos.y, rightElbow.x, rightElbow.y);
  
  float elbowToCenterDistance = leftElbow.dist(rightElbow) / 2;
  
  if (elbowToCenterDistance > humerusLenght){
    text("to far apart", 10, 30);
    return;
  }
  
  float angleBetweenElbowAndHand = acos(elbowToCenterDistance / humerusLenght);
  
  
  float RightHandAngle = -angleBetweenElbowAndHand + atan2((leftElbow.x - rightElbow.x), (leftElbow.y - rightElbow.y));
  float LeftHandAngle = angleBetweenElbowAndHand + atan2((rightElbow.x - leftElbow.x), (rightElbow.y - leftElbow.y));
  
  rightHand.x = (float)sin(RightHandAngle) * humerusLenght;
  rightHand.y = (float)cos(RightHandAngle) * humerusLenght;
  leftHand.x = (float)sin(LeftHandAngle) * humerusLenght;
  leftHand.y = (float)cos(LeftHandAngle) * humerusLenght;
  
  rightHand.add(rightElbow);
  leftHand.add(leftElbow);
  
  line(leftElbow.x, leftElbow.y, leftHand.x, leftHand.y);
  line(rightElbow.x, rightElbow.y, rightHand.x, rightHand.y);
}
