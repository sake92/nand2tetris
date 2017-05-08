/// push constant 7 ///
@7
D=A
@SP
A=M
M=D
// SP++
@SP
M=M+1

/// push constant 8 ///
@8
D=A
@SP
A=M
M=D
// SP++
@SP
M=M+1

/// add ///
// SP--
@SP
M=M-1

@SP
A=M
D=M           

// SP--
@SP
M=M-1

@SP
A=M

M=M+D      

// SP++
@SP
M=M+1

