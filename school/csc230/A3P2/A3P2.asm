.include "m2560def.inc"
;
;
; *********************************************************
; * Student # 	V00869081
; * Name     	Corey Koelewyn
; *	date	  	10/10/17
; *	description	decrements a hex number from 1-f then outputs 
; * the binary equiv on the arduino's LED's
; *
; *
; *
; *
; *********************************************************



ldi ZH, high(numbers)
ldi ZL, low(numbers)

.def num = r16
.def mask = r18
.def output = r17
.def count = r19
.def temp = r20
;Setting up LED's on arduino
;.equ PORTL = 0x10B
;.equ DDRL = 0x10A
ldi r17, 0xFF		; set data direction 
sts DDRL, r17	
;End of led setup

; *************************
; * Code segment follows: *
; *************************

;************************	
; Your code starts here:
.cseg


;num = 	choose a number in (0x00, 0x0F] 
ldi num, 14					;the number used for led's on, cannot be greator than 15

;while (number > 0) {
decrementing:
	;dest[count++] = number; stores the data in register 17 in the data segment 
	st Z+, num
	; * Output number on LEDs uses masks to spread the 4 bits over 8 bits as per instructions
	ldi mask ,  0b00001000; start mask at 8
	ldi output, 0b00000000; start output at 0
	ldi temp, 	0b00000000; uses a temp
	and mask, num			;and mask to isolate the 4th bit
	lsl mask				;shift left result to proper location
	lsl mask				;shift left result to proper location
	lsl mask				;shift left result to proper location
	lsl mask				;shift left result to proper location
	add temp , mask			;use a temp to store the results from this masking
	ldi mask , 0b00000100	;change mask to isolate 3rd bit
	and mask , num			;and mask to isolate the first bit
	lsl mask 				;shift left result to proper location
	lsl mask				;shift left result to proper location
	lsl mask				;shift left result to proper location
	add temp , mask			;use a temp to store the results from this masking and previous
	ldi mask , 0b00000010	;change mask to isolate the 2nd bit
	and mask , num			;and mask to isolate the first bit
	lsl mask				;shift left result to proper location
	lsl mask				;shift left result to proper location
	add temp , mask			;use a temp to store the results from this masking and previous
	ldi mask , 0b00000001	;mask right most bit
	and mask , num			;and mask to isolate the first bit
	lsl mask				;shift left result to proper location
	add temp , mask
	add output , temp		;puts the result from masked bits into output
	sts portl , temp		;sets the LED's to turn on from results

	; * delay 0.5 second, as provided
		ldi r24, 0x2A 
		outer: ldi r23, 0xFF
		middle: ldi r22, 0xFF
		inner: dec r22
		brne inner
		dec r23
		brne middle
		dec r24
		brne outer

	;End of delay block, indented for my sanity


		
	dec num; number --; Decrements number

	cpi r16, 0			; conditional for while loop
	brne decrementing 	; conditional for while loop

; Your code finishes here.
;*************************

	ldi temp, 0x00 		;these two lines turn off all the LED's
	sts portl, temp


done: jmp done ;loop for fnished



; *************************
; * Data segment follows: *
; *************************
.dseg
.org 0x200
numbers: .byte 16