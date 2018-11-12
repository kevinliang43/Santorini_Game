<h2> Commit 6cfd4ce </h2>
<h3> Project Restructuring </h3>

 * Removed immutable square, mutable square, abstract worker (classes that were never used)
 * Moved Observer.java to the Observer Directory
 * Change Board.java to implement the IBoard.java interface
 * Change Observer.java to implement IObserver.java interface
 * Change Player.java to implement IPlayer.java interface, code base given had a lot of rule checking functionality in the player interface so we changed the player interface to reflect the needs of a Santorini Player
 * Deleted TournamentState enumeration
 * Moved Referee and TournamentManager into Admin and have them implement their respective interfaces, changed the abstract Referee class to be a Referee Interface
 * Moved RuleChecker to Admin, IReferee is just a specification
 * Completed/Deleted TODO list that came with the code base (mostly adding tests for certain functions)

<h2> Commit a2e46b8 </h2> 
<h3> Cleaning up Unused Code and Switch to Creating JSON with JSON Package </h3> 

 * Removed Turn.DRAW from the Turn enumeration because it was never used
 * Removed GameOverStatus.KICKEDPLAYER because it was never used
 * Refactored our JSON string converters to use a JSON library in GameResult.java, Translator.java 
 * Changed fields in Action and MoveBuild to be private and added getter methods
 * Changed our n-out.json to match the newly created JSON obejcts using JSON package within xrun
 * Changed fields in GameResult to be private and added getter methods
 * Updated unit tests with new JSON format
