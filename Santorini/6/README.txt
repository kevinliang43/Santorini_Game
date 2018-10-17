This folder contains a program for running tests on the Santorini implementation.

xboard is a bash script that will call a jar file which runs the main program. It will wait for input on STDIN, so files can be redirected to efficiently test known cases. Note that xboard and test_runner use the `realpath` to make sure the path is correct regardless of where it is running from. This command may not be installed on Macs by default but is installed on the CCIS Linux machines.

board-tests directory contains JSON files with input for xboard and expected output. Tests can be run individually through `./xboard < board-tests/1-in.json | diff - 1-out.json`. This will run the first test case. The expected result is no output from the diff command. Additionally, a `test_runner` script can be called that will run test cases 1 through 5 in the board-tests directory. Simply `./test_runner` from the board-tests directory.

Aux contains the source files and generated Jar to exercise the Sanrotini (just Board so far) implementation. lib is external libraries, including the Santorini library. out is generated Jar artifact and related files. src is the source code.
