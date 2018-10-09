<h1> Santorini</h1> 
<h2> Directory </h2> 
<b> Common </b> : Holds all code both administrative and player components need to access <br />
<b> Admin </b> : Holds all administrator-specific code <br />
<b> Player </b> : Holds all player-specific code <br />
<b> Lib </b> : Holds all library-like code needed for our implementations <br />
<b> Design </b> : Holds all design specifications and plans <br />

<h3> Common/GamePieces/src </h3>

 * <b> Board.java </b> : Class representing a Game Board of a Santorini Game
 * <b> Cell.java </b> : Class representing a Cell on the Santorini Game Board
 * <b> Interpreter.java </b> : Class that converts JSON objects to actionable items onto a given Board Objects (used for xboard)
 * <b> JSONParse.java </b> : Class that takes in input and converts it to JSON objects
 * <b> RuleChecker.java </b> : Class that checks if player requested actions conform to the rules of the Santorini Game
 * <b> Worker.java </b> : Class representing a Worker in a Santorini Game
 * <b> XBoard.java </b> : Class containing main method used to run and test xboard
 * <b> XRules.java </b> : Class containing main method used to run and test xrules

<h3> Design </h3>

 * <b> board.java </b> : Interface specification for the Santorini Game Board and relevant classes, Worker and Square. <br /> 
 * <b> plan.pdf </b> : Project analysis of the Santorini Game.
 * <b> Player.java </b> : Interface specification for the player component of the Santorini Game.
 * <b> RuleChecker.java </b> : Interface specification for the rule checking component of the Santorini Game.
 * <b> IStrategy.java </b> : Interface specification for the Strategy components of the Santorini Game, describes interaction between Player and Strategy
 
<h3> Player </h3> 

 * <b> Player.java </b> : Class representing a Player in a Santorini Game
 
 <h2> Testing </h2> 
 
Each test suite is found within the folder corresponding to that week. For example, xboard tests are found in the 6 folder. For instructions on how to run the tests, see the TestMe file within each weeks folder.
