	module CARD_LOGIC
map:
	dw BRICK_SHORTAGE
	dw LUCKY_CACHE
	dw FRIENDLY_TERRAIN
	dw MINERS
BRICK_SHORTAGE_DATA:
	db #00	; cost: 0
	; The currency is irrelevant when the price is zero.
	db #02,#06,#F8	; All, Bricks, -8
LUCKY_CACHE_DATA:
	db #00	; cost: 0
	; The currency is irrelevant when the price is zero.
	db #00	; special | Play again
	db #00,#06,#02	; Player, Bricks, +2
	db #00,#0A,#02	; Player, Gems, +2
FRIENDLY_TERRAIN_DATA:
	db #FF	; cost: 1
	db #06	; currency: Bricks
	db #00	; special | Play again
	db #00,#00,#01	; Player, Wall, +1
MINERS_DATA:
	db #FD	; cost: 3
	db #06	; currency: Bricks
	db #00,#04,#01	; Player, Quarry, +1
BRICK_SHORTAGE:
	ld hl,BRICK_SHORTAGE_DATA
	call LOGIC.check_cost_currency
	ret nz
	call LOGIC.resource_calc
	ret
LUCKY_CACHE:
	ld hl,LUCKY_CACHE_DATA
	call LOGIC.check_cost_currency
	ret nz
	call LOGIC.exe_specials
	call LOGIC.resource_calc_2
	ret
FRIENDLY_TERRAIN:
	ld hl,FRIENDLY_TERRAIN_DATA
	call LOGIC.check_cost_currency
	ret nz
	call LOGIC.exe_specials
	call LOGIC.resource_calc
	ret
MINERS:
	ld hl,MINERS_DATA
	call LOGIC.check_cost_currency
	ret nz
	call LOGIC.resource_calc
	ret
	endmodule