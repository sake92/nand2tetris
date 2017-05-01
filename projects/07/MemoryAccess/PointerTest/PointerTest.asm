// instruction no. 0
@3030       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 1
@3
D=A
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
@3040       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 3
@3
D=A
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

// instruction no. 4
@32       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 5
@3
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

// instruction no. 6
@46       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 7
@4
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
@3
D=A
@0
A=D+A         // here we DIRECTLY SUM base(offset) and index(offset from base address...)
D=M           // now in D is the value of segment[index]

@0      // get stack location
A=M           // indirect access!
M=D           // put segment[index] on stack
@0
M=M+1

// instruction no. 9
@3
D=A
@1
A=D+A         // here we DIRECTLY SUM base(offset) and index(offset from base address...)
D=M           // now in D is the value of segment[index]

@0      // get stack location
A=M           // indirect access!
M=D           // put segment[index] on stack
@0
M=M+1

// instruction no. 10
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

// instruction no. 11
@3
D=M           // indirect access...

@2
A=D+A
D=M           // now in D is the value of segment[index]

@0      // get stack location
A=M           // indirect access!
M=D           // put segment[index] on stack
@0
M=M+1

// instruction no. 12
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

// instruction no. 13
@4
D=M           // indirect access...

@6
A=D+A
D=M           // now in D is the value of segment[index]

@0      // get stack location
A=M           // indirect access!
M=D           // put segment[index] on stack
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
M=M+D        // do the operation and store in second arg
@0
M=M+1

