	module CARD_LOGIC
map:
	dw BRICK_SHORTAGE
	dw LUCKY_CACHE
BRICK_SHORTAGE_DATA:
	db #07	; currency: Recruits
	db #03	; cost: 3
	db #00,#02,#04,#F8	; effect: GENERAL, All, Bricks, -8
LUCKY_CACHE_DATA:
	db #03	; currency: Bricks
	db #04	; cost: 4
	db #00	; special | Play again
	db #00,#00,#04,#02	; effect: GENERAL, Player, Bricks, +2
	db #00,#00,#10,#02	; effect: GENERAL, Player, Gems, +2
BRICK_SHORTAGE:
	ld hl,BRICK_SHORTAGE_DATA
	
	call LOGIC.check_cost_currency
	ret c
	call LOGIC.exe_effect
	ret
LUCKY_CACHE:
	ld hl,LUCKY_CACHE_DATA
	
	call LOGIC.check_cost_currency
	ret c
	call LOGIC.exe_specials
	call LOGIC.exe_effect_2
	ret
	endmodule