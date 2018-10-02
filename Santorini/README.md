<h1> Santorini</h1> 
<h2> Directory </h2> 
<b> Common </b> : Holds all code both administrative and player components need to access <br />
<b> Admin </b> : Holds all administrator-specific code <br />
<b> Player </b> : Holds all player-specific code <br />
<b> Lib </b> : Holds all library-like code needed for our implementations <br />
<b> Design </b> : Holds all design specifications and plans <br />

<h3> Common </h3>

 * <b> Cell.java </b> : Class representing a Cell on the Santorini Game Board
 * <b> Board.java </b> : Class representing a Game Board of a Santorini Game
 * <b> Worker.java </b> : Class representing a Worker in a Santorini Game
 * <b> Interpreter.java </b> : Class that converts JSON objects to actionable items onto a given Board Objects (used for xboard)
 * <b> JSONParse.java </b> : Class that takes in input and converts it to JSON objects
 * <b> Main.java </b> : Main class used for xboard

<h3> Design </h3>

 * <b> board.java </b> : Interface specification for the Santorini Game Board and relevant classes, Worker and Square. <br /> 
 * <b> plan.pdf </b> : Project analysis of the Santorini Game.
 * <b> RuleChecker.java </b> : Interface specification for the rule checking component of the Santorini Game.
 * <b> Player.java </b> : Interface specification for the player componenrt of the Santorini Game.
 
 <h2> Testing </h2> 
 to test using our xboard object, run the following command within the 6 directory:<br /> ./xboard <n-in.json> | diff - <n-out.json>
 
 Example: Testing 1-in.json
 
 ./xboard 1-in.json | diff - board-tests/1-out.json
 
 To run all 3 tests, within the 6 directory you can also use the TESTME file. <br />
 To do this run this command:   ./TESTME <br /> 
The TESTME file will not produce any output if all tests pass.
If there are discrepencies between the output of the xboard executable and the n-out.json, then an output will be created by the 'diff' command that will be displayed on the console.
