// instruction no. 0
@10       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 1
@1
D=M           // indirect access...
@0
D=D+A         // address is now set
@6
M=D           // store address in TEMP2 register, temporarily

@0
M=M-1
@0
A=M           // indirect access...
D=M           // store in D

@6
A=M           // indirect
M=D           // store to M[TEMP2], that is segment[index]

@0
M=M-1 // implementation detail, ignore this.. xD
@0
M=M+1

// instruction no. 2
@21       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 3
@22       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 4
@2
D=M           // indirect access...
@2
D=D+A         // address is now set
@6
M=D           // store address in TEMP2 register, temporarily

@0
M=M-1
@0
A=M           // indirect access...
D=M           // store in D

@6
A=M           // indirect
M=D           // store to M[TEMP2], that is segment[index]

@0
M=M-1 // implementation detail, ignore this.. xD
@0
M=M+1

// instruction no. 5
@2
D=M           // indirect access...
@1
D=D+A         // address is now set
@6
M=D           // store address in TEMP2 register, temporarily

@0
M=M-1
@0
A=M           // indirect access...
D=M           // store in D

@6
A=M           // indirect
M=D           // store to M[TEMP2], that is segment[index]

@0
M=M-1 // implementation detail, ignore this.. xD
@0
M=M+1

// instruction no. 6
@36       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 7
@3
D=M           // indirect access...
@6
D=D+A         // address is now set
@6
M=D           // store address in TEMP2 register, temporarily

@0
M=M-1
@0
A=M           // indirect access...
D=M           // store in D

@6
A=M           // indirect
M=D           // store to M[TEMP2], that is segment[index]

@0
M=M-1 // implementation detail, ignore this.. xD
@0
M=M+1

// instruction no. 8
@42       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 9
@45       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 10
@4
D=M           // indirect access...
@5
D=D+A         // address is now set
@6
M=D           // store address in TEMP2 register, temporarily

@0
M=M-1
@0
A=M           // indirect access...
D=M           // store in D

@6
A=M           // indirect
M=D           // store to M[TEMP2], that is segment[index]

@0
M=M-1 // implementation detail, ignore this.. xD
@0
M=M+1

// instruction no. 11
@4
D=M           // indirect access...
@2
D=D+A         // address is now set
@6
M=D           // store address in TEMP2 register, temporarily

@0
M=M-1
@0
A=M           // indirect access...
D=M           // store in D

@6
A=M           // indirect
M=D           // store to M[TEMP2], that is segment[index]

@0
M=M-1 // implementation detail, ignore this.. xD
@0
M=M+1

// instruction no. 12
@510       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 13
@5
D=A
@6
D=D+A         // address is now set
@6
M=D           // store address in TEMP2 register, temporarily

@0
M=M-1
@0
A=M           // indirect access...
D=M           // store in D

@6
A=M           // indirect
M=D           // store to M[TEMP2], that is segment[index]

@0
M=M-1 // implementation detail, ignore this.. xD
@0
M=M+1

// instruction no. 14
@1
D=M           // indirect access...

@0
A=D+A
D=M           // now in D is the value of segment[index]

@0      // get stack location
A=M           // indirect access!
M=D           // put segment[index] on stack
@0
M=M+1

// instruction no. 15
@4
D=M           // indirect access...

@5
A=D+A
D=M           // now in D is the value of segment[index]

@0      // get stack location
A=M           // indirect access!
M=D           // put segment[index] on stack
@0
M=M+1

// instruction no. 16
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

// instruction no. 17
@2
D=M           // indirect access...

@1
A=D+A
D=M           // now in D is the value of segment[index]

@0      // get stack location
A=M           // indirect access!
M=D           // put segment[index] on stack
@0
M=M+1

// instruction no. 18
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

// instruction no. 19
@3
D=M           // indirect access...

@6
A=D+A
D=M           // now in D is the value of segment[index]

@0      // get stack location
A=M           // indirect access!
M=D           // put segment[index] on stack
@0
M=M+1

// instruction no. 20
@3
D=M           // indirect access...

@6
A=D+A
D=M           // now in D is the value of segment[index]

@0      // get stack location
A=M           // indirect access!
M=D           // put segment[index] on stack
@0
M=M+1

// instruction no. 21
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

// instruction no. 22
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

// instruction no. 23
@5
D=A
@6
A=D+A         // here we DIRECTLY SUM base(offset) and index(offset from base address...)
D=M           // now in D is the value of segment[index]

@0      // get stack location
A=M           // indirect access!
M=D           // put segment[index] on stack
@0
M=M+1

// instruction no. 24
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

