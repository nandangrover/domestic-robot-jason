// plan to go to the door and brings beer back to the fridge
+!order_beers[source(Ag)] : true
  <- .send(supermarket, achieve, order(beer, 5));
     !at(robotConcierge,door). 
  
// when the supermarket makes a delivery, send signal to robot that fridge is restocked
+delivered(beer,_Qtd,_OrderId)[source(supermarket)]
  :  true
  <- .print("The beer has been collected from the door");
     !at(robotConcierge,fridge);
     .print("The conceirge robot is back at the fridge");
     .send(robot, achieve, restocked(Product,Qtd,OrderId));
     !at(robotConcierge,init).

// Add an action to for the robot to go back to the fridge and put the beer in it and let the main robot know
+!at(robotConcierge,P) : at(robotConcierge,P) <- true.
+!at(robotConcierge,P) : not at(robotConcierge,P)
    <- move(robotConcierge, P);
       !at(robotConcierge,P).
