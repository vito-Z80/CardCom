	module CARD_LOGIC
CARDS = 6	; max: 113 cards.
map:
	dw MOTHER_LODE
	dw COPPING_THE_TECH
	dw WORK_OVERTIME
	dw FLOOD_WATER
	dw BARRACKS
	dw FOUNDATIONS
probability:
	db #03
	db #02
	db #03
	db #03
	db #02
	db #03
MOTHER_LODE_DATA:
	db #04	; cost: 4,  | No specials
	db #06	; currency: Bricks
	db #FF	; #FF by structure value else by value.
	db #00	; sign <
	db #00	; Player
	db #04	; Quarry
	db #01	; Enemy
	db #04	; Quarry
	db #00,#04,#02	; Player, Quarry, +2
MOTHER_LODE_DATA_FALSE:
	db #00,#04,#01	; Player, Quarry, +1
COPPING_THE_TECH_DATA:
	db #05	; cost: 5,  | No specials
	db #06	; currency: Bricks
	db #FF	; #FF by structure value else by value.
	db #00	; sign <
	db #00	; Player
	db #04	; Quarry
	db #01	; Enemy
	db #04	; Quarry
	db #FF,#04,#00	; value, Quarry,  Player [enemy Quarry value copy to player]
	db #FF	; no false content in condition.
WORK_OVERTIME_DATA:
	db #02	; cost: 2,  | No specials
	db #06	; currency: Bricks
	db #00,#00,#05	; Player, Wall, +5
	db #00,#0A,#FA	; Player, Gems, -6
FLOOD_WATER_DATA:
	db #06	; cost: 6,  | No specials
	db #06	; currency: Bricks
	db #FF	; #FF by structure value else by value.
	db #20	; sign <!=
	db #00	; Player
	db #00	; Wall
	db #01	; Enemy
	db #00	; Wall
	db #00,#0C,#FF	; Player, Dungeon, -1
	db #00,#02,#FE	; Player, Tower, -2
FLOOD_WATER_DATA_FALSE:
	db #01,#0C,#FF	; Enemy, Dungeon, -1
	db #01,#02,#FE	; Enemy, Tower, -2
BARRACKS_DATA:
	db #0A	; cost: 10,  | No specials
	db #06	; currency: Bricks
	db #00,#0E,#06	; Player, Recruits, +6
	db #00,#00,#06	; Player, Wall, +6
	db #FF	; #FF by structure value else by value.
	db #00	; sign <
	db #00	; Player
	db #0C	; Dungeon
	db #01	; Enemy
	db #0C	; Dungeon
	db #00,#0C,#01	; Player, Dungeon, +1
	db #FF	; no false content in condition.
FOUNDATIONS_DATA:
	db #03	; cost: 3,  | No specials
	db #06	; currency: Bricks
	db #00	; #FF by structure value else by value.
	db #02	; sign =
	db #00	; Player
	db #00	; Wall
	db #00,#00,#06	; Player, Wall, +6
FOUNDATIONS_DATA_FALSE:
	db #00,#00,#03	; Player, Wall, +3
MOTHER_LODE:
	dw MOTHER_LODE_DATA
	call LOGIC.exe_condition
	jr nz,.falseContent
	call LOGIC.resource_calc
	ret
.falseContent:
	ld hl,MOTHER_LODE_DATA_FALSE
	call LOGIC.resource_calc
	ret
COPPING_THE_TECH:
	dw COPPING_THE_TECH_DATA
	call LOGIC.exe_condition
	ret nz
	call LOGIC.assign_calc
	ret
WORK_OVERTIME:
	dw WORK_OVERTIME_DATA
	call LOGIC.resource_calc_2
	ret
FLOOD_WATER:
	dw FLOOD_WATER_DATA
	call LOGIC.exe_condition
	jr nz,.falseContent
	call LOGIC.resource_calc_2
	ret
.falseContent:
	ld hl,FLOOD_WATER_DATA_FALSE
	call LOGIC.resource_calc_2
	ret
BARRACKS:
	dw BARRACKS_DATA
	call LOGIC.resource_calc_2
	call LOGIC.exe_condition
	ret nz
	call LOGIC.resource_calc
	ret
FOUNDATIONS:
	dw FOUNDATIONS_DATA
	call LOGIC.exe_condition
	jr nz,.falseContent
	call LOGIC.resource_calc
	ret
.falseContent:
	ld hl,FOUNDATIONS_DATA_FALSE
	call LOGIC.resource_calc
	ret
	endmodule