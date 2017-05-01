// instruction no. 0
@17       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 1
@17       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 2
@0
M=M-1
@0
A=M             // indirect access...
D=M             // store first argument in D

@0
M=M-1
@0
A=M
D=M-D           // subtract second argument and store in D

@TRUE0
D;JEQ // if D is true, jump to true
@0
A=M
M=0                // else it is false and goto end
@END0
0;JMP

(TRUE0)
@0
A=M
M=-1
(END0)
@0
M=M+1

// instruction no. 3
@17       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 4
@16       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 5
@0
M=M-1
@0
A=M             // indirect access...
D=M             // store first argument in D

@0
M=M-1
@0
A=M
D=M-D           // subtract second argument and store in D

@TRUE1
D;JEQ // if D is true, jump to true
@0
A=M
M=0                // else it is false and goto end
@END1
0;JMP

(TRUE1)
@0
A=M
M=-1
(END1)
@0
M=M+1

// instruction no. 6
@16       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 7
@17       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 8
@0
M=M-1
@0
A=M             // indirect access...
D=M             // store first argument in D

@0
M=M-1
@0
A=M
D=M-D           // subtract second argument and store in D

@TRUE2
D;JEQ // if D is true, jump to true
@0
A=M
M=0                // else it is false and goto end
@END2
0;JMP

(TRUE2)
@0
A=M
M=-1
(END2)
@0
M=M+1

// instruction no. 9
@892       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 10
@891       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 11
@0
M=M-1
@0
A=M             // indirect access...
D=M             // store first argument in D

@0
M=M-1
@0
A=M
D=M-D           // subtract second argument and store in D

@TRUE3
D;JLT // if D is true, jump to true
@0
A=M
M=0                // else it is false and goto end
@END3
0;JMP

(TRUE3)
@0
A=M
M=-1
(END3)
@0
M=M+1

// instruction no. 12
@891       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 13
@892       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 14
@0
M=M-1
@0
A=M             // indirect access...
D=M             // store first argument in D

@0
M=M-1
@0
A=M
D=M-D           // subtract second argument and store in D

@TRUE4
D;JLT // if D is true, jump to true
@0
A=M
M=0                // else it is false and goto end
@END4
0;JMP

(TRUE4)
@0
A=M
M=-1
(END4)
@0
M=M+1

// instruction no. 15
@891       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 16
@891       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 17
@0
M=M-1
@0
A=M             // indirect access...
D=M             // store first argument in D

@0
M=M-1
@0
A=M
D=M-D           // subtract second argument and store in D

@TRUE5
D;JLT // if D is true, jump to true
@0
A=M
M=0                // else it is false and goto end
@END5
0;JMP

(TRUE5)
@0
A=M
M=-1
(END5)
@0
M=M+1

// instruction no. 18
@32767       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 19
@32766       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 20
@0
M=M-1
@0
A=M             // indirect access...
D=M             // store first argument in D

@0
M=M-1
@0
A=M
D=M-D           // subtract second argument and store in D

@TRUE6
D;JGT // if D is true, jump to true
@0
A=M
M=0                // else it is false and goto end
@END6
0;JMP

(TRUE6)
@0
A=M
M=-1
(END6)
@0
M=M+1

// instruction no. 21
@32766       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 22
@32767       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 23
@0
M=M-1
@0
A=M             // indirect access...
D=M             // store first argument in D

@0
M=M-1
@0
A=M
D=M-D           // subtract second argument and store in D

@TRUE7
D;JGT // if D is true, jump to true
@0
A=M
M=0                // else it is false and goto end
@END7
0;JMP

(TRUE7)
@0
A=M
M=-1
(END7)
@0
M=M+1

// instruction no. 24
@32766       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 25
@32766       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 26
@0
M=M-1
@0
A=M             // indirect access...
D=M             // store first argument in D

@0
M=M-1
@0
A=M
D=M-D           // subtract second argument and store in D

@TRUE8
D;JGT // if D is true, jump to true
@0
A=M
M=0                // else it is false and goto end
@END8
0;JMP

(TRUE8)
@0
A=M
M=-1
(END8)
@0
M=M+1

// instruction no. 27
@57       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 28
@31       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 29
@53       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 30
@0
M=M-1
@0
A=M             // indirect access...
D=M             // store first argument in D

@0
M=M-1
@0
A=M
M=M+D        // do the operation and store in second arg
@0
M=M+1

// instruction no. 31
@112       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 32
@0
M=M-1
@0
A=M             // indirect access...
D=M             // store first argument in D

@0
M=M-1
@0
A=M
M=M-D        // do the operation and store in second arg
@0
M=M+1

// instruction no. 33
@0
M=M-1
@0
A=M
D=0
M=D-M
@0
M=M+1

// instruction no. 34
@0
M=M-1
@0
A=M             // indirect access...
D=M             // store first argument in D

@0
M=M-1
@0
A=M
M=M&D        // do the operation and store in second arg
@0
M=M+1

// instruction no. 35
@82       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 36
@0
M=M-1
@0
A=M             // indirect access...
D=M             // store first argument in D

@0
M=M-1
@0
A=M
M=M|D        // do the operation and store in second arg
@0
M=M+1

// instruction no. 37
@0
M=M-1
@0
A=M             // indirect access...
M=!M
@0
M=M+1

