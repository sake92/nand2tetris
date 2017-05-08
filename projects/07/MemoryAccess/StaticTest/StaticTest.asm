/// push constant 111 ///
@111
D=A
@SP
A=M
M=D
// SP++
@SP
M=M+1

/// push constant 333 ///
@333
D=A
@SP
A=M
M=D
// SP++
@SP
M=M+1

/// push constant 888 ///
@888
D=A
@SP
A=M
M=D
// SP++
@SP
M=M+1

/// pop static 8 ///
@test.8
D=M
@8
D=D+A          
@R13
M=D            

// SP--
@SP
M=M-1

@SP
A=M
D=M           

@R13
A=M
M=D           
/// pop static 3 ///
@test.3
D=M
@3
D=D+A          
@R13
M=D            

// SP--
@SP
M=M-1

@SP
A=M
D=M           

@R13
A=M
M=D           
/// pop static 1 ///
@test.1
D=M
@1
D=D+A          
@R13
M=D            

// SP--
@SP
M=M-1

@SP
A=M
D=M           

@R13
A=M
M=D           
/// push static 3 ///
@test.3
D=M
@3
A=D+A
D=M

@SP
A=M 
M=D       
// SP++
@SP
M=M+1

/// push static 1 ///
@test.1
D=M
@1
A=D+A
D=M

@SP
A=M 
M=D       
// SP++
@SP
M=M+1

/// sub ///
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

M=M-D      

// SP++
@SP
M=M+1

/// push static 8 ///
@test.8
D=M
@8
A=D+A
D=M

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

