PVector leftShoulderPos = new PVector(220, 350);
PVector rightShoulderPos = new PVector(380, 350);
float humerusLenght = 80; // humerusLenght = R

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
  PVector rightElbowPos = new PVector();
  PVector leftElbowPos = new PVector();
  PVector rightHandPos = new PVector();
  PVector leftHandPos = new PVector();

  rightElbowPos.x = (float)sin(rightAngle) * humerusLenght;
  rightElbowPos.y = (float)cos(rightAngle) * humerusLenght;
  leftElbowPos.x = (float)sin(leftAngle) * humerusLenght;
  leftElbowPos.y = (float)cos(leftAngle) * humerusLenght;
  
  rightElbowPos.add(rightShoulderPos);
  leftElbowPos.add(leftShoulderPos);
  
  line(leftShoulderPos.x, leftShoulderPos.y, leftElbowPos.x, leftElbowPos.y);
  line(rightShoulderPos.x, rightShoulderPos.y, rightElbowPos.x, rightElbowPos.y);
  
  float elbowToCenterDistance = leftElbowPos.dist(rightElbowPos) / 2;
  
  if (elbowToCenterDistance > humerusLenght){
    text("to far apart", 10, 30);
    return;
  }
  
  float angleBetweenElbowAndHand = acos(elbowToCenterDistance / humerusLenght);
  
  
  float RightHandAngle = -angleBetweenElbowAndHand + atan2((leftElbowPos.x - rightElbowPos.x), (leftElbowPos.y - rightElbowPos.y));
  float LeftHandAngle = angleBetweenElbowAndHand + atan2((rightElbowPos.x - leftElbowPos.x), (rightElbowPos.y - leftElbowPos.y));
  
  rightHandPos.x = (float)sin(RightHandAngle) * humerusLenght;
  rightHandPos.y = (float)cos(RightHandAngle) * humerusLenght;
  leftHandPos.x = (float)sin(LeftHandAngle) * humerusLenght;
  leftHandPos.y = (float)cos(LeftHandAngle) * humerusLenght;
  
  rightHand.add(rightElbow);
  leftHand.add(leftElbow);
  
  line(leftElbow.x, leftElbow.y, leftHand.x, leftHand.y);
  line(rightElbow.x, rightElbow.y, rightHand.x, rightHand.y);
}
