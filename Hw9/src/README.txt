//////////////////////////////////
Minesweeper Redone by Gabriel Lau
//////////////////////////////////

This program is a recreation of the The Windows 98 version of Minesweeper.
The only major differences are 
a) the lack of a top task bar, replaced by 3 buttons that allow the user to select a difficulty:
- Beginner(Represented by a green B) (a 9x9 grid with 10 mines)
- Intermediate(Represented by a Yellow I) (a 16x16 grid with 40 mines)
- Expert(Represented by a red E) (a 30x16 grid with 99 mines)

b) the lack of being able to drag your mouse when held over mines to reveal a blank space
(as in holding left click and dragging your mouse will result in 
the current space under your mouse looking revealed as if it was a space with 0 surrounding mines,
and then opening the space under your mouse)
This has been rectified by making it so that when you drag your mouse,
it reveals the space where you started to drag your mouse.

c) slight changes to the graphics, such as the smiley face being slighly different, 
due to to image library constraints

Extra Credit that I can think of:
- Improved graphics that display the flags(from -99 to 999) and timer(goes to 999),
 just as in the Windows 98 version.
- Improved graphics for a border
- Improved graphics for the mines
- Identifies the mine you hit, as well as any faulty flags you placed
- A reset button(The smiley face) that allows you to reset the game at any stage, 
  which changes the mine's position, except for the first click(read about why below)
  ~ The smiley face also changes based on game state, as well as when you hover over it.
- The first click of every game gives you the option to change the difficulty of the game
  (read above at point a in the "major differences" section for more details)
- First click is guaranteed not a mine, in fact it should trigger a collapse every time
  
///////////////////////////////////////////////
Please play my game i had so much fun making it
Also It was so painful to make
///////////////////////////////////////////////
