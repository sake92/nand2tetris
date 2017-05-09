////////// BOOTSTRAP CODE //////////
@256
D=A
@SP
M=D

/// call Sys.init 0 ///
@RETURN_0
D=A
@SP
A=M
M=D
@SP            // SP++
M=M+1



// pushing segment LCL on the stack !
@LCL
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment ARG on the stack !
@ARG
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment THIS on the stack !
@THIS
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment THAT on the stack !
@THAT
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1



// ARG = SP-n-5
@SP
D=M
@5
D=D-A
@0
D=D-A
@ARG
M=D

// LCL = SP
@SP
D=M
@LCL
M=D

// goto f
@Sys.init
0;JMP

(RETURN_0)

////////// BOOTSTRAP CODE END //////////

/// function Main.fibonacci 0 ///
(Main.fibonacci)

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

/// push constant 2 ///
@2
D=A

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// lt ///
@SP            // SP--
M=M-1

@SP
A=M
D=M
@SP            // SP--
M=M-1

@SP
A=M
D=M-D          // D is condition for jump

@TRUE1
D;JLT
@SP
A=M
M=0
@END1
0;JMP

(TRUE1)
@SP
A=M
M=-1
(END1)

@SP            // SP++
M=M+1

/// if-goto IF_TRUE ///
@SP            // SP--
M=M-1

@SP
A=M
D=M

@Main.fibonacci$IF_TRUE
D;JNE

/// goto IF_FALSE ///
@Main.fibonacci$IF_FALSE
0;JMP

/// label IF_TRUE ///
(Main.fibonacci$IF_TRUE)

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

/// label IF_FALSE ///
(Main.fibonacci$IF_FALSE)

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

/// push constant 2 ///
@2
D=A

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

/// call Main.fibonacci 1 ///
@RETURN_1
D=A
@SP
A=M
M=D
@SP            // SP++
M=M+1



// pushing segment LCL on the stack !
@LCL
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment ARG on the stack !
@ARG
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment THIS on the stack !
@THIS
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment THAT on the stack !
@THAT
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1



// ARG = SP-n-5
@SP
D=M
@5
D=D-A
@1
D=D-A
@ARG
M=D

// LCL = SP
@SP
D=M
@LCL
M=D

// goto f
@Main.fibonacci
0;JMP

(RETURN_1)
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

/// push constant 1 ///
@1
D=A

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

/// call Main.fibonacci 1 ///
@RETURN_2
D=A
@SP
A=M
M=D
@SP            // SP++
M=M+1



// pushing segment LCL on the stack !
@LCL
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment ARG on the stack !
@ARG
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment THIS on the stack !
@THIS
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment THAT on the stack !
@THAT
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1



// ARG = SP-n-5
@SP
D=M
@5
D=D-A
@1
D=D-A
@ARG
M=D

// LCL = SP
@SP
D=M
@LCL
M=D

// goto f
@Main.fibonacci
0;JMP

(RETURN_2)
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

/// function Sys.init 0 ///
(Sys.init)

/// push constant 4 ///
@4
D=A

@SP
A=M
M=D
@SP            // SP++
M=M+1

/// call Main.fibonacci 1 ///
@RETURN_3
D=A
@SP
A=M
M=D
@SP            // SP++
M=M+1



// pushing segment LCL on the stack !
@LCL
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment ARG on the stack !
@ARG
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment THIS on the stack !
@THIS
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1


// pushing segment THAT on the stack !
@THAT
D=M
@SP
A=M
M=D
@SP            // SP++
M=M+1



// ARG = SP-n-5
@SP
D=M
@5
D=D-A
@1
D=D-A
@ARG
M=D

// LCL = SP
@SP
D=M
@LCL
M=D

// goto f
@Main.fibonacci
0;JMP

(RETURN_3)
/// label WHILE ///
(Sys.init$WHILE)

/// goto WHILE ///
@Sys.init$WHILE
0;JMP

