<h1> Santorini</h1> 
<h2> Directory </h2> 
<b> Common </b> : Holds all code both administrative and player components need to access <br />
<b> Admin </b> : Holds all administrator-specific code <br />
<b> Player </b> : Holds all player-specific code <br />
<b> Lib </b> : Holds all library-like code needed for our implementations <br />
<b> Design </b> : Holds all design specifications and plans <br />
<b> Observer </b> : Holds all observer-specific code <br /> 
<b> Remote </b> : Holds all remote components 

<h3> Common/GamePieces/src </h3>

 * <b> Action.java </b> : Class representing a single turn action, can be one of: PlaceWorker, Move, or Build
 * <b> ActionRunnable.java </b> : Runnable Action that enables us to request an Action on a seperate thread
 * <b> Board.java </b> : Class representing a Game Board of a Santorini Game
 * <b> BoardStatus.java </b> : Class representing a Board with limited functionality combined with the current game Status
 * <b> ConfigReader.java </b> : Class that parses a JSON Configuration file and creates Players and Observers from the file specifications
 * <b> GameOverStatus.java </b> : Enumeration representing the possible Game Over states/possibilities
 * <b> GameResult.java </b> : Class that represents the end results of a game run by a Referee
 * <b> IAction.java </b> : IAction interface to encapsulate Actions and MoveBuilds
 * <b> MoveBuild.java </b> : Class representing an IAction that contains both a move and build request
 * <b> Square.java </b> : Class representing a cell on the board in a game of Santorini
 * <b> Status.java </b> : Enumeration representing the status of the game and what kind of action the game needs
 * <b> Turn.java </b> : Enumeration that represents whose turn in the game it is
 * <b> Worker.java </b> : Class representing a Worker in a game of Santorini
 
<h3> Admin </h3> 

 * <b> Referee.java </b> : Class representing the administrative component of a game of Santorini, runs the game and manages interaction between Players and Board
 * <b> RuleChecker.java </b> : Class representing the rules the Santorini Game abides by, used to check validity of moves
 * <b> TournamentManager.java </b> : Class representing an administrative component that supervises and facilitates a Round Robin Santorini Tournament
 
 
<h3> Player </h3> 

 * <b> BreakerPlayer.java </b> : Class representing a Player that is intended to break a rule
 * <b> BreakerStrategy.java </b> : Class representing a strategy that is intended to provide illegal moves
 * <b> InfinitePlacePlayer.java </b> : Class representing a Player that is intended to enter an infinite loop when placing workers
 * <b> InfinitePlaceStrategy.java </b> : Class representing a strategy that is intended to enter an infinite loop when determining where to place workers
 * <b> InfinitePlayer.java </b> : Class representing a Player that is intended to enter an infinite loop when performing a MoveBuild action
 * <b> InfiniteStrategy.java </b> : Class representing a strategy that is intended to enter an infinite loop when performing a MoveBuild action
 * <b> Player.java </b> : Class representing a Player in a Santorini Game that takes in a Strategy
 * <b> StayAlivePlayer.java </b> : Class representing a Player that uses the Stay Alive Strategy
 * <b> StayAliveStrategy.java </b> : Class representing a strategy that picks moves on the criteria that a player can stay alive for n rounds after the move is made
 * <b> Strategy.java </b> : Interface representing a Strategy in a game of Santorini that a Player can play by
 
 
<h3> Design </h3>

 * <b> IBoard.java </b> : Interface specification for the Santorini Game Board and relevant classes, Worker and Square
 * <b> IObserver.java </b> : Plan for plugging in an Observer into the Referee
 * <b> plan.pdf </b> : Project analysis of the Santorini Game
 * <b> IStrategy.java </b> : Interface specification for the Strategy components of the Santorini Game, describes interaction between Player and Strategy
 * <b> IPlayer.java </b> : Interface specification for the player component of the Santorini Game
 * <b> IReferee.java </b> : Interface for a Referee component in a Santorini Game
 * <b> IRuleChecker.java </b> : Interface specification for the rule checking component of the Santorini Game
 * <b> ITournamentManager.java </b> : Interface specification for a tournament manager for a tournament of Santorini Games
 * <b> TournamentProtocol.pdf </b> : Remote Protocol for Tournament Manager itneraction with Clients
 
 
 <h3> Observer </h3>

 * <b> Observer.java </b> : Class representing and observer object of a game of Santorini
 
 
 <h3> Remote </h3> 
 
 * <b> ClientSideProxy.java </b> : Class acting as a remote client
 * <b> MessageType.java </b> : Enumeration representing the different types of messages a remote client should expect
 * <b> ProxyPlayer.java </b> : Class representing a remote player object
 * <b> RemoteStrategy.java </b> : Class representing a strategy that chooses moves from a remote client
 * <b> Server.java </b> : Class representing the Server of a Santorini Game Server
 
 
 <h2> Testing </h2> 
 
 To Run TESTME testing suite:
 1. Go into the Santorini Directory.
 2. Run the following commmand:
  ./TESTME
 
 Unit tests can be found at Santorini/tests. </br>
 Unit Tests Created for Week 9: </br>
  * <b> RefereeTest.java </b> : tests the functions within the Referee class, includes commented-out tests for private methods
  * <b> Player.java </b> : tests the functions within the Player class
 
 Unit Tests Created for Week 10: </br> 
  * <b> ObserverTest.java </b> : tests the functions within the Observer class
  * <b> RefereeTest.java </b> : tests added observer message sending, commented-out tests using private methods

Unit Tests Created for Week 11: </br> 
  * <b> ConfigReaderTest.java </b> : tests the functions within the ConfigReader class
  * <b> BreakerStrategyTest.java </b> : tests the functions within the BreakerStrategy class
  * <b> InfiniteStrategyTest.java </b> : tests the functions within the InfiniteStrategy class
  * <b> TournamentManagerTests.java </b> : tests the functions within the TournamentManager class

Unit Tests Created for Week 13: </br> 
  * <b> ProxyPlayerTest.java </b> : tests the functions within the ProxyPlayer class
  * <b> RemoteStrategyTest.java </b> : tests the functions within the RemoteStrategy class using a mock Socket
 
 
Each test suite is found within the folder corresponding to that week. For example, xboard tests are found in the 6 folder. For instructions on how to run the tests, see the TestMe file within each weeks folder.

10- Implementing an Observer: xObserve can be found within the 10 folder in the maka-keli directory and run using ./xObserve


11- Implementing the Tournament Manager: xrun and santorini.rc can be found within the 11 folder. santorini.rc holds the system-level tests

13- Implementing Remote Proxies: xserver and xclient can be found within the 13 folder
