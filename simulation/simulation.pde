PVector leftShoulderPos = new PVector(220, 350);
PVector rightShoulderPos = new PVector(380, 350);
float humerusLength = 80; // humerusLenght = R

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

  rightElbowPos.x = (float)sin(rightAngle) * humerusLength;
  rightElbowPos.y = (float)cos(rightAngle) * humerusLength;
  leftElbowPos.x = (float)sin(leftAngle) * humerusLength;
  leftElbowPos.y = (float)cos(leftAngle) * humerusLength;
  
  rightElbowPos.add(rightShoulderPos);
  leftElbowPos.add(leftShoulderPos);
  
  line(leftShoulderPos, leftElbowPos);
  line(rightShoulderPos, rightElbowPos);
  
  float elbowToCenterDistance = leftElbowPos.dist(rightElbowPos) / 2;
  
  if (elbowToCenterDistance > humerusLength){
    text("to far apart", 10, 30);
    return;
  }
  
  float angleBetweenElbowAndHand = acos(elbowToCenterDistance / humerusLength);
  
  
  float RightHandAngle = -angleBetweenElbowAndHand + atan2((leftElbowPos.x - rightElbowPos.x), (leftElbowPos.y - rightElbowPos.y));
  float LeftHandAngle = angleBetweenElbowAndHand + atan2((rightElbowPos.x - leftElbowPos.x), (rightElbowPos.y - leftElbowPos.y));
  
  rightHandPos.x = (float)sin(RightHandAngle) * humerusLength;
  rightHandPos.y = (float)cos(RightHandAngle) * humerusLength;
  leftHandPos.x = (float)sin(LeftHandAngle) * humerusLength;
  leftHandPos.y = (float)cos(LeftHandAngle) * humerusLength;
  
  rightHandPos.add(rightElbowPos);
  leftHandPos.add(leftElbowPos);
  
  line(leftElbowPos, leftHandPos);
  line(rightElbowPos, rightHandPos);
}

void line(PVector a, PVector b){
  line(a.x, a.y, b.x, b.y);
}
