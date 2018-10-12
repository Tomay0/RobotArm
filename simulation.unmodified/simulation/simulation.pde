float humerusLength = 150; // humerusLenght = R
PVector leftShoulderPos = new PVector(220, 350);
PVector rightShoulderPos = new PVector(leftShoulderPos.x + humerusLength / 2 , 350);

void setup(){
  size(600, 400);
}


void draw(){
  background(0);
  stroke(250, 0, 0);
  noFill();
  ellipse(leftShoulderPos.x, leftShoulderPos.y, humerusLength * 2, humerusLength * 2);
  ellipse(rightShoulderPos.x, rightShoulderPos.y, humerusLength * 2, humerusLength * 2);
  ellipse(mouseX, mouseY, humerusLength * 2, humerusLength * 2);
  
  stroke(255);
  
  float[] armAngles = calculateAngle(new PVector(mouseX, mouseY));
  //drawArms((float)mouseX / 50, (float)mouseY / 50);
  drawArms(armAngles[0], armAngles[1]);
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

float[] calculateAngle(PVector pos){
  float[] angles = new float[2];  
  
  if (pos.dist(leftShoulderPos) > 2 * humerusLength || pos.dist(rightShoulderPos) > 2 * humerusLength){
    text("not in range", 10, 40);
    return angles;
  }
  // finding mid points (aka 'A')
  PVector midLeft = new PVector((pos.x + leftShoulderPos.x) / 2, (pos.y + leftShoulderPos.y) / 2);
  PVector midRight = new PVector((pos.x + rightShoulderPos.x) / 2, (pos.y + rightShoulderPos.y) / 2);
  //line(midLeft.x, midLeft.y, midRight.x, midRight.y); // debugging
  
  // finding distance betwen midpoint and joint (aka 'h')
  float midToJointLeft = sqrt(sq(humerusLength) - sq(pos.dist(leftShoulderPos) / 2));
  float midToJointRight = sqrt(sq(humerusLength) - sq(pos.dist(rightShoulderPos) / 2));
  
  // finding angle between hand and joint (aka 'a')
  float angleFromMidToJointLeft = acos((leftShoulderPos.x - pos.x) / pos.dist(leftShoulderPos));
  float angleFromMidToJointRight = acos((rightShoulderPos.x - pos.x) / pos.dist(rightShoulderPos));
  
  // finding joint position (aka 'x3', 'y3', 'x4', 'y4')
  PVector leftJointPos = new PVector(midLeft.x - midToJointLeft * sin(angleFromMidToJointLeft), midLeft.y + midToJointLeft * cos(angleFromMidToJointLeft));
  PVector rightJointPos = new PVector(midRight.x + midToJointRight * sin(angleFromMidToJointRight), midRight.y - midToJointRight * cos(angleFromMidToJointRight));
  
  //line(leftJointPos.x, leftJointPos.y, rightJointPos.x, rightJointPos.y); // debugging
  
  // finding angle of sholder
  float LeftShoulderAngle = atan2((leftJointPos.x - leftShoulderPos.x), (leftJointPos.y - leftShoulderPos.y));
  float RightShoulderAngle = atan2((rightJointPos.x - rightShoulderPos.x), (rightJointPos.y - rightShoulderPos.y));
  
  angles[0] = LeftShoulderAngle;
  angles[1] = RightShoulderAngle;
  
  return angles;
}
