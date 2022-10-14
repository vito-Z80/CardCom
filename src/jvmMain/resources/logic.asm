	module CARD_LOGIC
map:
	dw BRICK_SHORTAGE
	dw LUCKY_CACHE
	dw FRIENDLY_TERRAIN
	dw MINERS
	dw MOTHER_LODE
BRICK_SHORTAGE_DATA:
	db #03	; currency: Bricks
	db #00	; cost: 0
	db #00,#02,#04,#F8	; effect: GENERAL, All, Bricks, -8
LUCKY_CACHE_DATA:
	db #03	; currency: Bricks
	db #00	; cost: 0
	db #00	; special | Play again
	db #00,#00,#04,#02	; effect: GENERAL, Player, Bricks, +2
	db #00,#00,#10,#02	; effect: GENERAL, Player, Gems, +2
FRIENDLY_TERRAIN_DATA:
	db #03	; currency: Bricks
	db #01	; cost: 1
	db #00	; special | Play again
	db #00,#00,#00,#01	; effect: GENERAL, Player, Wall, +1
MINERS_DATA:
	db #03	; currency: Bricks
	db #03	; cost: 3
	db #00,#00,#02,#01	; effect: GENERAL, Player, Quarry, +1
MOTHER_LODE_DATA:
	db #03	; currency: Bricks
	db #04	; cost: 4
	db #FF	; #FF by structure value else by value.
	db #00	; sign <
	db #00	; Player
	db #02	; Quarry
	db #01	; Enemy
	db #02	; Quarry
	db #00,#00,#02,#02	; effect: GENERAL, Player, Quarry, +2
	db #00,#00,#02,#01	; effect: GENERAL, Player, Quarry, +1
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
FRIENDLY_TERRAIN:
	ld hl,FRIENDLY_TERRAIN_DATA
	
	call LOGIC.check_cost_currency
	ret c
	call LOGIC.exe_specials
	call LOGIC.exe_effect
	ret
MINERS:
	ld hl,MINERS_DATA
	
	call LOGIC.check_cost_currency
	ret c
	call LOGIC.exe_effect
	ret
MOTHER_LODE:
	ld hl,MOTHER_LODE_DATA
	
	call LOGIC.check_cost_currency
	ret c
	call LOGIC.exe_condition
	jr z,.falseContent
	call LOGIC.exe_effect
	ret
.falseContent:
	call LOGIC.exe_effect
	ret
	ret
	endmodule