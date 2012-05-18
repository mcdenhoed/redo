#REDO Game

This game is written in Python and uses the [pygame] engine. Thus, both Python and pygame need to be installed to run this game.

More information about the game's development is at the [project page][gh-pages] for the game.

[pygame]: http://www.pygame.org/news.html
[gh-pages]: http://mcdenhoed.github.com/redo/


##Controls
wasd to move.
Up to jump.
Space to start/stop recording.

##Licensing
All code is BSD licensed.

##What's next for the project?
Lots (once I get the time)! Right now my main concerns are...

### Crushing Bugs
There's some killer bugs in there. One of the most mysterious is slight variations in the motion of the recordings.  I think it might be due to some sort of weird rounding error involving camera motion.  I might define everything in terms of a universal coordinate system instead of the relative one I'm using now. Not sure if it will help. This is the main bug I see. There is also some jittering which I think can be fixed by reordering some of the stuff that I did collision detection with.

### Polish
I might refine the controls more to make it more playable. I'm also thinking of adding some visual flaire, like some fake bloom filters (using a pre-made image instead of a black rectangle, for instance. Also, I might add in the paralax background that the java version had. What I think might be fun is if the next levels can be seen in the background as the user plays through a given level. Just a thought. I also want to add little flourishes like maybe a small cloud of tiny rectangles from the player upon a jump. We shall see.

### Integration
I want menus. I think I also want to integrate the level editor with the actual game. Maybe I can see if I can get rid of some of that filler code and use REAL platforms and players and stuff instead of those dumb placeholder classes. Just a thought.