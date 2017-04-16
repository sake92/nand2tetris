// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

@color    // declare color variable
M=0      // by default is white

(LOOP)

  @SCREEN
  D=A
  @pixels
  M=D         // pixel address, goes from 16384 to 16384 + 8192 == 24576

  @KBD    // keyboard address
  D=M
  @BLACK
  D;JGT     // if(keyboard > 0) goto BLACK
  
  @color
  M=0       // set to white
  @COLOR_SCREEN
  0;JMP     // jump to subroutine that colors the screen
  
  (BLACK)
    @color
    M=-1    // set to black (2's complement 111111111...)

  (COLOR_SCREEN)
    @color
    D=M
    @pixels
    A=M         // VERY IMPORTANT! indirect address
    M=D         // color M[pixels] with @color
    
    @pixels
    M=M+1
    D=M
        
    @24576
    D=D-A
    @COLOR_SCREEN
    D;JLT

@LOOP
0;JMP // infinite loop
