/// function SimpleFunction.test 2 ///
(SimpleFunction.test)
@0
D=A

@SP
A=M
M=D
@SP            // SP++
M=M+1

@0
D=A

@SP
A=M
M=D
@SP            // SP++
M=M+1


/// push local 0 ///
@LCL
D=M       // indirect, pointer
@0
A=D+A
D=M

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// push local 1 ///
@LCL
D=M       // indirect, pointer
@1
A=D+A
D=M

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// add ///
@SP            // SP--
M=M-1

@SP
A=M
D=M
@SP            // SP--
M=M-1

@SP
A=M
M=M+D
@SP            // SP++
M=M+1

/// not ///
@SP            // SP--
M=M-1

@SP
A=M
M=!M
@SP            // SP++
M=M+1

/// push argument 0 ///
@ARG
D=M       // indirect, pointer
@0
A=D+A
D=M

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// add ///
@SP            // SP--
M=M-1

@SP
A=M
D=M
@SP            // SP--
M=M-1

@SP
A=M
M=M+D
@SP            // SP++
M=M+1

/// push argument 1 ///
@ARG
D=M       // indirect, pointer
@1
A=D+A
D=M

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// sub ///
@SP            // SP--
M=M-1

@SP
A=M
D=M
@SP            // SP--
M=M-1

@SP
A=M
M=M-D
@SP            // SP++
M=M+1

/// return ///
// FRAME = LCL       // FRAME  is a temporary variable R7
@LCL
D=M
@R7
M=D

// RET = *(FRAME-5)  // store RETURN-address in a temp var R14
@5
A=D-A
D=M
@R14
M=D

// *ARG = pop()      // Reposition the return value for the caller
@ARG
D=M
@0
D=D+A

@R13
M=D

@SP            // SP--
M=M-1

@SP
A=M
D=M

@R13
A=M
M=D



// SP = ARG+1        // Restore SP of the caller
@ARG
D=M
@SP
M=D+1

// THAT = *(FRAME-1) // Restore THAT of the caller
@R7
D=M
@1
A=D-A
D=M

@THAT
M=D

// THIS = *(FRAME-2) // Restore THIS of the caller
@R7
D=M
@2
A=D-A
D=M

@THIS
M=D

// ARG = *(FRAME-3)  // Restore ARG of the caller
@R7
D=M
@3
A=D-A
D=M

@ARG
M=D

// LCL = *(FRAME-4)  // Restore LCL of the caller
@R7
D=M
@4
A=D-A
D=M

@LCL
M=D


// goto RET // Goto return-address (in the caller’s code)
@R14
A=M
0;JMP

