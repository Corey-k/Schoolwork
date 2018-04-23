;
; CSc 230 Assignment 5 Part 2 Programming
;   Factorial
;  
;	Name	   : Corey Koelewyn
;	Stu number : V00869081
;	Date	   : November 17, 2017
; 	Description: As per the handout on coursespaces, this assignment is in 4 parts
;				 Part 1 defines a macro satisfying the following psuedo-instruction
;				 		Addw ah, al, bh, bl
;				 Part 2 multiplies 2 bytes together, passing the arguments on the stack
;						using the addition marco from part 1
;				 Part 3 Uses Part 2 to create a recursive function to solve factorials
;				 Part 4 Uses part 3, with a variable called init declared in memory.
;						the output shall be shown on the 4 LEDS on port L
;
;
;	NOTES FOR MARKER:	I could not quite get the recursion part to work, I feel confidant in my logic,
;						but for some reason it does not seem to quite work. I feel a discussion will be had
;						at the demo.
;
;
.include "m2560def.inc"
; This program calculates n! where n is found in the variable Init 
; where the lower nibble of the result will be shown on the 4 LEDS attached to port L
;
.cseg

;  Obtain the constant from location init
	ldi zH, High(init<<1)
	ldi zL, low(init<<1)
	lpm r16, Z

;***
; YOUR CODE GOES HERE:
;

push r16
call factorial
jmp done
;this is the macro for synthetic addition to satisfy the operation ah:al <- ah:al + bh:bl
;it satisfies the program description from above for part 1
.MACRO ADDW 
	add @1, @3
	adc @0, @2
.ENDMACRO

;initializing stack pointer
	ldi r21, low(RAMEND)
	out SPL, r21
	ldi r21, high(RAMEND)
	out SPH, r21

	
	
; This function satisfies part 2 of the program description above
; Psuedo code for Part 2
; word multiply (byte factor, byte multiplier) {
;     word answer = 0;
;     while (factor-- > 0) answer += multiplier;
;     return answer;
; }\


;stack representation for multiply
;	|   | < -- Z and SP
;	|r02|  1
;	|r01|  2
;	|r00|  3
;	|r31|  4
;	|r30|  5
;	|ret|  6
;	|ret|  7
;	|ret|  8
;	|par2| 9
;	|par1| 10
multiply:
	push r30				;protecting the Z pointer
	push r31				;protecting the Z pointer
	push r0					;saving registers we are going to use
	push r1					;saving registers we are going to use
	push r2					;saving registers we are going to use
;set Z pointer
	in ZH, SPH
	in ZL, SPL
;load parameters
	ldd r1, Z+10			;factor
	ldd r2, Z+9				;multiplier
							;high byte for returned word
							;low byte for returned word
	clr r0					;used as Bh for ADDW marco -> assignment spec allows this
multiply_loop:
	addw r25, r24, r0, r2 	;answer += multiplier
	dec r1					;factor --
	breq end_multiply
	jmp multiply_loop
end_multiply:
	pop r2
	pop r1
	pop r0
	pop r31
	pop r30
	ret

	
	;sudocode
	;def factorial(int n):
	;	if n = 1:
	;		return n
	;	return n * factorial(n-1)
	;Stack representation for factorial
	; |
	; |
	; |
	; |r17|	6
	; |r16| 5
	; |ret| 4
	; |ret| 3
	; |ret| 2
	; | n | 1
Factorial:
	push r17			;protecting registers
	push r16			;protecting registers
	in ZH, SPH			;loading ZH from SPH
	in ZL, SPL			;loading ZL from SPL
	ldd r16, Z+6			;Loading parameter into r16
	ldi r17, $01		;loading 1 into r17
	cp r16, r17			;if n = 1
	breq end_factorial	;if n = 1 return
	push r16			;loading for multiplying
	dec r16				;n-1 for recursive multiplying
	push r16			;loading for multiplying
	call Factorial		;recursive call
	call multiply		;multiplying
end_factorial:
	pop r16
	sts result, r16
	pop r16
	pop r17
	ret







; YOUR CODE FINISHES HERE
;****

done:	jmp done

; The constant, named init, holds the starting number.  
init:	.db 0x03

; This is in the data segment (ie. SRAM)
; The first real memory location in SRAM starts at location 0x200 on
; the ATMega 2560 processor.  
;
.dseg
.org 0x200

result:	.byte 2
