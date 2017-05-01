// instruction no. 0
@7       // load constant
D=A           // store const in D
@0      // get stack location
A=M           // indirect access!
M=D           // put const in stack
@0
M=M+1

// instruction no. 1
@8       // load constant
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
M=M+D        // do the operation and store in second arg
@0
M=M+1

