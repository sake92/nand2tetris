/// push constant 3030 ///
@3030
D=A
@SP
A=M
M=D
// SP++
@SP
M=M+1

/// pop pointer 0 ///
@THIS
D=A
@0
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
/// push constant 3040 ///
@3040
D=A
@SP
A=M
M=D
// SP++
@SP
M=M+1

/// pop pointer 1 ///
@THIS
D=A
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
/// push constant 32 ///
@32
D=A
@SP
A=M
M=D
// SP++
@SP
M=M+1

/// pop this 2 ///
@THIS
D=M
@2
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
/// push constant 46 ///
@46
D=A
@SP
A=M
M=D
// SP++
@SP
M=M+1

/// pop that 6 ///
@THAT
D=M
@6
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
/// push pointer 0 ///
@THIS
D=A
@0
A=D+A
D=M

@SP
A=M 
M=D       
// SP++
@SP
M=M+1

/// push pointer 1 ///
@THIS
D=A
@1
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

/// push this 2 ///
@THIS
D=M
@2
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

/// push that 6 ///
@THAT
D=M
@6
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

