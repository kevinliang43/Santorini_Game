<h1> Santorini - Design </h1> 
<h2> Directory </h2> 

<b> board.java </b> : Interface specification for the Santorini Game Board and relevant classes, Worker and Square.
  * board(int row, int column) : Interface representing the Board of a Santorini Game, contains a 2D Array of Squares representing the grid of size row x col.
    * void addWorker(Worker worker, Square targetSquare) : Adds the given Worker to the given Square.
    * void moveWorker(Worker worker, Square targetSquare) : Moves the given Worker to the given Square.
    * void addFloor(Worker worker, Square targetSquare) : Uses the given Worker to the given Square.
  * Square(int row, int column) : Interface representing a Square, a Square represents a cell in the 2D Array grid. A Square is defined to be a building if it has a height greater than 0 and NOT to have a building if it has a height of 0. A Square is initialized with a height field set to 0 and a Worker field set to null.
    * void addFloor() : Adds 1 to the height of this Square.
    * boolean adjacentSquare(Square targetSquare): Determines if the given Square is next to this Square.
  * Worker(Square targetSquare) : Interface representing a Worker game piece object. A Worker is initialized on the given Square and with a boolean <i>moved</i> representing whether or not this Worker has moved.
    * move(Square targetSquare) : Moves this Worker to the given Square. 
    * build(Square targetSquare) : Builds a floor onto the given Square.
    
<b> plan.pdf </b> : Project analysis of the Santorini Game

