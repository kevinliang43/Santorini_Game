<h1> Santorini</h1> 
<h2> Directory </h2> 
<b> Common </b> : Holds all code both administrative and player components need to access <br />
<b> Admin </b> : Holds all administrator-specific code <br />
<b> Player </b> : Holds all player-specific code <br />
<b> Lib </b> : Holds all library-like code needed for our implementations <br />
<b> Design </b> : Holds all design specifications and plans <br />

<h3> Common/GamePieces/src </h3>

 * <b> Action.java </b> : Class representing a single turn action, can be one of: PlaceWorker, Move, or Build
 * <b> ActionRunnable.java </b> : Runnable Action that enables us to request an Action on a seperate thread
 * <b> Board.java </b> : Class representing a Game Board of a Santorini Game
 * <b> BoardStatus.java </b> : Class representing a Board with limited functionality combined with the current game Status
 * <b> GameOverStatus.java </b> : Enumeration representing the possible Game Over states/possibilities
 * <b> IAction.java </b> : IAction interface to encapsulate Actions and MoveBuilds
 * <b> MoveBuild.java </b> : Class representing an IAction that contains both a move and build request
 * <b> RuleChecker.java </b> : Class representing the rules the Santorini Game abides by, used to check validity of moves
 * <b> Square.java </b> : Class representing a cell on the board in a game of Santorini
 * <b> Status.java </b> : Enumeration representing the status of the game and what kind of action the game needs
 * <b> StayAliveStrategy.java </b> : Class representing a strategy that picks moves on the criteria that a player can stay alive for n rounds after the move is made
 * <b> Strategy.java </b> : Interface representing a Strategy in a game of Santorini that a Player can play by
 * <b> Turn.java </b> : Enumeration that represents whose turn in the game it is
 * <b> Worker.java </b> : Class representing a Worker in a game of Santorini
 
<h3> Admin </h3> 

 * <b> Referee.java </b> : Class representing the administrative component of a game of Santorini, runs the game and manages interaction between Players and Board
 
 
<h3> Player </h3> 

 * <b> Player.java </b> : Class representing a Player in a Santorini Game
 
 
<h3> Design </h3>

 * <b> board.java </b> : Interface specification for the Santorini Game Board and relevant classes, Worker and Square
 * <b> IObserver.java </b> : Plan for plugging in an Observer into the Referee
 * <b> plan.pdf </b> : Project analysis of the Santorini Game
 * <b> IStrategy.java </b> : Interface specification for the Strategy components of the Santorini Game, describes interaction between Player and Strategy
 * <b> Player.java </b> : Interface specification for the player component of the Santorini Game
 * <b> RuleChecker.java </b> : Interface specification for the rule checking component of the Santorini Game
 
 
 
 <h2> Testing </h2> 
 
 To Run TESTME testing suite:
 1. Go into the Santorini Directory.
 2. Run the following commmand:
  ./TESTME
 
 Unit tests can be found at Santorini/tests. </br>
 Unit Tests Created for Week 9: </br>
  * <b> RefereeTest.java </b> : tests the functions within the Referee class, includes commented-out tests for private methods
  * <b> Player.java </b> : tests the functions within the Player class
  
 
 
Each test suite is found within the folder corresponding to that week. For example, xboard tests are found in the 6 folder. For instructions on how to run the tests, see the TestMe file within each weeks folder.
