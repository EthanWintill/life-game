# life-game
A simple Jframe game of life animation that demonstrates the speedup due to threading. 

## In general
I designed this so that most of the functionality can be controlled from main. When running, make sure to input the number of threads and number of generations from the console, otherwise you can simply change the input variables in the 'BoardSim' constructer called in main. For optimal speed-up, put the number of cores in your machine as the number of threads. 

If you don't care about threads, and just want to see the simulation, set the variable 'threadDemo' in the 'BoardSim' constructer to false. This will cause the game to speed up to human-relevant levels. The threading only occurs when the next generation is computed, specifically the board is dividied up among all the different threads, so if there's two threads each takes half the board, if there's three then a third each, etc. In order for the threading to actually have a noticable effect, I delayed the 'countLiveNeighbors()' method used to update the board by .00025 seconds. Turning off threadDemo turns this off.

Any 2D boolean can be used, I have a blank board initilized as well as one from file input in main, but the file input is used currently.
