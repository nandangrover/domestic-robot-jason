/* Initial beliefs and rules */

// initially, I believe that there is some dirt in the environment
!has(dirt).

!clean(dirt).   // initial goal: clean dirt

+!clean(dirt): has(dirt)
    <- .wait(400);
       !at(robotCleaner,dirt);
       .print("Dirt Cleaned");
       !at(robotCleaner,init);
       !clean(dirt).

+!clean(dirt): not has(dirt)
    <- .wait(2);
       !clean(dirt).

+!at(robotCleaner,P) : at(robotCleaner,P) <- true.
+!at(robotCleaner,P) : not at(robotCleaner,P)
    <- move(robotCleaner, P);
       !at(robotCleaner,P).

-!at(_,_)
   :  true
   <- true.

-!has(dirt)
   :  true
   <- .random(X); .wait(X*5000+2000);
      !has(dirt).
