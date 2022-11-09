	module CARD_LOGIC
CARDS = 20	; max: 113 cards.
map:
	dw BRICK_SHORTAGE
	dw LUCKY_CACHE
	dw FRIENDLY_TERRAIN
	dw MINERS
	dw MOTHER_LODE
	dw DWARVEN_MINERS
	dw WORK_OVERTIME
	dw COPPING_THE_TECH
	dw BASIC_WALL
	dw STURDY_WALL
	dw INNOVATIONS
	dw FOUNDATIONS
	dw TREMORS
	dw SECRET_ROOM
	dw EARTHQUAKE
	dw BIG_WALL
	dw COLLAPSE!
	dw NEW_EQUIPMENT
	dw STRIP_MINE
	dw REINFORCED_WALL
probability:
	db #02
	db #01
	db #01
	db #02
	db #01
	db #01
	db #01
	db #01
	db #01
	db #02
	db #01
	db #01
	db #01
	db #01
	db #01
	db #01
	db #02
	db #01
	db #01
	db #03
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
MOTHER_LODE_DATA:
	db #FC	; cost: 4
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
DWARVEN_MINERS_DATA:
	db #F9	; cost: 7
	db #06	; currency: Bricks
	db #00,#00,#04	; Player, Wall, +4
	db #00,#04,#01	; Player, Quarry, +1
WORK_OVERTIME_DATA:
	db #FE	; cost: 2
	db #06	; currency: Bricks
	db #00,#00,#05	; Player, Wall, +5
	db #00,#0A,#FA	; Player, Gems, -6
COPPING_THE_TECH_DATA:
	db #FB	; cost: 5
	db #06	; currency: Bricks
	db #FF	; #FF by structure value else by value.
	db #00	; sign <
	db #00	; Player
	db #04	; Quarry
	db #01	; Enemy
	db #04	; Quarry
	db #FF,#04,#01	; value, Quarry,  Enemy [enemy Quarry value copy to player]
	db #FF	; no false content in condition.
BASIC_WALL_DATA:
	db #FE	; cost: 2
	db #06	; currency: Bricks
	db #00,#00,#03	; Player, Wall, +3
STURDY_WALL_DATA:
	db #FD	; cost: 3
	db #06	; currency: Bricks
	db #00,#00,#04	; Player, Wall, +4
INNOVATIONS_DATA:
	db #FE	; cost: 2
	db #06	; currency: Bricks
	db #00,#04,#01	; Player, Quarry, +1
	db #01,#04,#01	; Enemy, Quarry, +1
	db #00,#0A,#04	; Player, Gems, +4
FOUNDATIONS_DATA:
	db #FD	; cost: 3
	db #06	; currency: Bricks
	db #00	; #FF by structure value else by value.
	db #02	; sign =
	db #00	; Player
	db #00	; Wall
	db #00,#00,#06	; Player, Wall, +6
FOUNDATIONS_DATA_FALSE:
	db #00,#00,#03	; Player, Wall, +3
TREMORS_DATA:
	db #F9	; cost: 7
	db #06	; currency: Bricks
	db #00	; special | Play again
	db #02,#00,#FB	; All, Wall, -5
SECRET_ROOM_DATA:
	db #F8	; cost: 8
	db #06	; currency: Bricks
	db #00	; special | Play again
	db #00,#08,#01	; Player, Magic, +1
EARTHQUAKE_DATA:
	db #00	; cost: 0
	; The currency is irrelevant when the price is zero.
	db #02,#04,#FF	; All, Quarry, -1
BIG_WALL_DATA:
	db #FB	; cost: 5
	db #06	; currency: Bricks
	db #00,#00,#06	; Player, Wall, +6
COLLAPSE!_DATA:
	db #FC	; cost: 4
	db #06	; currency: Bricks
	db #01,#04,#FF	; Enemy, Quarry, -1
NEW_EQUIPMENT_DATA:
	db #FA	; cost: 6
	db #06	; currency: Bricks
	db #00,#04,#02	; Player, Quarry, +2
STRIP_MINE_DATA:
	db #00	; cost: 0
	; The currency is irrelevant when the price is zero.
	db #00,#04,#FF	; Player, Quarry, -1
	db #00,#00,#0A	; Player, Wall, +10
	db #00,#0A,#05	; Player, Gems, +5
REINFORCED_WALL_DATA:
	db #F8	; cost: 8
	db #06	; currency: Bricks
	db #00,#00,#08	; Player, Wall, +8
BRICK_SHORTAGE:
	dw BRICK_SHORTAGE_DATA
	call LOGIC.resource_calc
	ret
LUCKY_CACHE:
	dw LUCKY_CACHE_DATA
	call LOGIC.exe_specials
	call LOGIC.resource_calc
	call LOGIC.resource_calc
	ret
FRIENDLY_TERRAIN:
	dw FRIENDLY_TERRAIN_DATA
	call LOGIC.exe_specials
	call LOGIC.resource_calc
	ret
MINERS:
	dw MINERS_DATA
	call LOGIC.resource_calc
	ret
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
DWARVEN_MINERS:
	dw DWARVEN_MINERS_DATA
	call LOGIC.resource_calc
	call LOGIC.resource_calc
	ret
WORK_OVERTIME:
	dw WORK_OVERTIME_DATA
	call LOGIC.resource_calc
	call LOGIC.resource_calc
	ret
COPPING_THE_TECH:
	dw COPPING_THE_TECH_DATA
	call LOGIC.exe_condition
	ret nz
	call LOGIC.assign_calc
	ret
BASIC_WALL:
	dw BASIC_WALL_DATA
	call LOGIC.resource_calc
	ret
STURDY_WALL:
	dw STURDY_WALL_DATA
	call LOGIC.resource_calc
	ret
INNOVATIONS:
	dw INNOVATIONS_DATA
	call LOGIC.resource_calc
	call LOGIC.resource_calc
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
TREMORS:
	dw TREMORS_DATA
	call LOGIC.exe_specials
	call LOGIC.resource_calc
	ret
SECRET_ROOM:
	dw SECRET_ROOM_DATA
	call LOGIC.exe_specials
	call LOGIC.resource_calc
	ret
EARTHQUAKE:
	dw EARTHQUAKE_DATA
	call LOGIC.resource_calc
	ret
BIG_WALL:
	dw BIG_WALL_DATA
	call LOGIC.resource_calc
	ret
COLLAPSE!:
	dw COLLAPSE!_DATA
	call LOGIC.resource_calc
	ret
NEW_EQUIPMENT:
	dw NEW_EQUIPMENT_DATA
	call LOGIC.resource_calc
	ret
STRIP_MINE:
	dw STRIP_MINE_DATA
	call LOGIC.resource_calc
	call LOGIC.resource_calc
	call LOGIC.resource_calc
	ret
REINFORCED_WALL:
	dw REINFORCED_WALL_DATA
	call LOGIC.resource_calc
	ret
	endmodule