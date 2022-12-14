	module CARD_TEXT
SPACE_2 = 255
SPACE_4 = 254
NEW_LINE = 251
END = 0
map:
	dw MOTHER_LODE
	dw COPPING_THE_TECH
	dw WORK_OVERTIME
	dw FLOOD_WATER
	dw BARRACKS
	dw FOUNDATIONS
	dw THIEF

data:

MOTHER_LODE:
	db "MOTHER LODE",NEW_LINE
	db "IF",NEW_LINE,"QUARRY < ENEMY",NEW_LINE,"QUARRY,",NEW_LINE,"+2 QUARRY.",NEW_LINE,"ELSE, +1 QUARRY",END
COPPING_THE_TECH:
	db "COPPING THE TECH",NEW_LINE
	db "",SPACE_4,SPACE_2," IF",NEW_LINE,SPACE_4," QUARRY",NEW_LINE,SPACE_4,SPACE_2," <",NEW_LINE,SPACE_2,"ENEMY QUARRY,",NEW_LINE,SPACE_4," QUARRY",NEW_LINE,SPACE_4,SPACE_4,"=",NEW_LINE,SPACE_2,"ENEMY QUARRY",END
WORK_OVERTIME:
	db "WORK OVERTIME",NEW_LINE
	db "",SPACE_4,NEW_LINE,NEW_LINE,SPACE_4,"+5 WALL.",NEW_LINE," YOU LOSE 6 GEMS",END
FLOOD_WATER:
	db "FLOOD WATER",NEW_LINE
	db "PLAYER(S)",NEW_LINE," W/ LOWEST WALL",NEW_LINE," ARE -1 DUNGEON",NEW_LINE,"AND 2",SPACE_2,"DAMAGE TO",NEW_LINE,SPACE_4," TOWER",END
BARRACKS:
	db "BARRACKS",NEW_LINE
	db "+6 RECRUITS,",NEW_LINE,"+6 WALL.",NEW_LINE,"IF",NEW_LINE,"DUNGEON",NEW_LINE,"<",NEW_LINE,"ENEMY DUNGEON,",NEW_LINE,SPACE_2," +1 DUNGEON",END
FOUNDATIONS:
	db "FOUNDATIONS",NEW_LINE
	db "IF",NEW_LINE,"WALL = 0,",NEW_LINE,"+6 WALL,",NEW_LINE,"ELSE",NEW_LINE,"+3 WALL",END
THIEF:
	db "THIEF",NEW_LINE
	db "",SPACE_2,NEW_LINE,SPACE_2,"ENEMY LOSES",NEW_LINE,SPACE_4,"10 GEMS,",NEW_LINE,SPACE_2," 5 BRICKS,",NEW_LINE,SPACE_4,"YOU GAIN",NEW_LINE,SPACE_4,"1/2 AMT.",NEW_LINE,SPACE_4,"ROUND UP",END
	endmodule