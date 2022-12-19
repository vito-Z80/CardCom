	module CARD_SPRITE
map:
	dw MOTHER_LODE
	dw COPPING_THE_TECH
	dw WORK_OVERTIME
	dw FLOOD_WATER
	dw BARRACKS
	dw FOUNDATIONS
	dw THIEF

sprites:

MOTHER_LODE:
	db #03, #07, #AF, #00, #0F, #03, #15, #00
	db #17, #01, #0B, #00, #2F, #01, #57, #00
	db #17, #01, #2A, #00, #2B, #82, #07, #00
	db #17, #54, #2A, #00, #A8, #83, #01, #78
	db #E1, #81, #00, #30, #F3, #12, #70, #A3
	db #59, #99, #7C, #02, #2F, #18, #79, #18
	db #5E, #09, #66, #10, #20, #69, #2C, #06
	db #10, #78, #54, #04, #23, #18, #12, #00
	db #0A, #7D, #00, #8F, #17, #28, #00, #07
	db #0E, #42, #78, #03, #04, #7C, #36, #8B
	db #03, #00, #31, #87, #0B, #10, #78, #8B
	db #06, #01, #F1, #C7, #02, #03, #E2, #CB
	db #60, #02, #95, #38, #37, #75, #02, #18
	db #18, #5A, #D0, #18, #86, #CD, #E8, #3C
	db #40, #B9, #C1, #3C, #B0, #D0, #A0, #78
	db #CC, #E4, #44, #D8, #64, #4E, #10, #30
; attributes
	db #06, #70, #45, #68, #06, #46, #41, #07
	db #02, #50, #47, #70, #41, #47, #47, #46
COPPING_THE_TECH:
	db #00, #00, #00, #00, #00, #00, #00, #00
	db #03, #FA, #7E, #20, #0C, #0F, #89, #C0
	db #13, #F0, #FF, #80, #1F, #9F, #01, #40
	db #20, #71, #FF, #80, #3F, #8F, #C7, #00
	db #21, #00, #01, #55, #1F, #01, #01, #FE
	db #38, #03, #03, #B4, #07, #07, #03, #F8
	db #39, #0F, #87, #F0, #0F, #1F, #CF, #E0
	db #37, #3F, #DF, #C0, #39, #7F, #FF, #80
	db #0F, #00, #10, #2A, #3F, #01, #38, #00
	db #23, #01, #44, #1F, #1C, #07, #91, #A0
	db #3F, #7F, #3A, #7F, #19, #7F, #81, #A0
	db #07, #01, #44, #18, #1C, #03, #28, #83
	db #F0, #03, #BB, #40, #EC, #07, #3B, #08
	db #C0, #0E, #33, #38, #CE, #1C, #31, #22
	db #D0, #18, #61, #08, #E0, #00, #60, #3A
	db #CE, #00, #00, #00, #C0, #00, #00, #3A
; attributes
	db #46, #46, #46, #46, #46, #75, #48, #48
	db #46, #48, #07, #4F, #70, #48, #48, #4F
WORK_OVERTIME:
	db #80, #10, #70, #BB, #D6, #00, #B8, #5D
	db #6F, #00, #7A, #07, #A4, #04, #E0, #89
	db #A0, #00, #70, #02, #D4, #40, #00, #51
	db #BC, #00, #20, #E0, #D6, #00, #00, #48
	db #81, #00, #00, #EA, #49, #50, #05, #BF
	db #04, #05, #50, #D5, #C4, #A0, #0A, #6A
	db #22, #5A, #A5, #80, #00, #F5, #5F, #15
	db #08, #AF, #FA, #40, #40, #FA, #AF, #00
	db #A1, #7F, #00, #16, #12, #69, #68, #1E
	db #12, #21, #FD, #0E, #08, #01, #50, #03
	db #A0, #31, #00, #02, #10, #3D, #20, #11
	db #01, #1D, #65, #3F, #02, #0D, #7F, #B9
	db #00, #0D, #98, #46, #60, #0D, #98, #62
	db #52, #05, #48, #E4, #40, #01, #68, #60
	db #08, #00, #00, #20, #00, #00, #00, #00
	db #04, #00, #00, #00, #00, #00, #00, #00
; attributes
	db #08, #0D, #0F, #08, #01, #4A, #4A, #51
	db #01, #42, #50, #50, #01, #46, #46, #46
FLOOD_WATER:
	db #70, #DE, #5B, #C7, #E0, #F5, #31, #C3
	db #40, #78, #86, #83, #10, #2C, #CF, #A1
	db #08, #D0, #D4, #80, #0C, #80, #82, #0E
	db #20, #C8, #57, #9E, #00, #80, #82, #16
	db #05, #23, #A8, #BC, #23, #39, #ED, #9B
	db #11, #58, #DC, #06, #0B, #32, #0A, #10
	db #4D, #57, #67, #B8, #E7, #22, #F2, #11
	db #47, #09, #44, #04, #03, #00, #80, #00
	db #C4, #7D, #EB, #6C, #62, #B6, #F5, #D3
	db #88, #5D, #D7, #87, #F4, #34, #EA, #0E
	db #20, #C5, #D1, #5B, #9C, #A0, #80, #41
	db #02, #0E, #A1, #03, #20, #87, #00, #80
	db #10, #12, #C6, #CE, #52, #4C, #08, #AB
	db #B2, #17, #E1, #5A, #B9, #88, #06, #76
	db #E8, #55, #79, #AC, #B9, #0C, #00, #70
	db #74, #D7, #25, #48, #AB, #2F, #CB, #14
; attributes
	db #28, #05, #46, #68, #29, #06, #46, #68
	db #0D, #08, #48, #4D, #0F, #0F, #4F, #79
BARRACKS:
	db #17, #DA, #82, #B8, #0A, #A0, #82, #BC
	db #5C, #80, #54, #E8, #FC, #81, #E0, #D0
	db #AC, #FF, #FF, #00, #04, #96, #A8, #A4
	db #98, #80, #8A, #92, #E0, #C0, #0D, #28
	db #10, #80, #00, #00, #A8, #ED, #2D, #F4
	db #C0, #90, #7D, #B6, #E0, #80, #6D, #B5
	db #74, #A0, #6F, #BE, #98, #E2, #00, #00
	db #FC, #FD, #24, #92, #80, #AA, #00, #00
	db #54, #7E, #6F, #3C, #00, #5C, #7E, #D3
	db #00, #5F, #3F, #24, #54, #02, #00, #38
	db #50, #35, #24, #81, #00, #1E, #00, #C3
	db #00, #37, #4E, #DD, #50, #02, #2B, #66
	db #7E, #FF, #00, #00, #00, #01, #81, #3C
	db #00, #88, #48, #56, #D0, #14, #D8, #EC
	db #E2, #08, #FA, #22, #80, #00, #FC, #DA
	db #D0, #D3, #BA, #D0, #EA, #05, #11, #00
; attributes
	db #03, #70, #70, #06, #04, #70, #42, #02
	db #42, #46, #42, #04, #70, #70, #46, #02
FOUNDATIONS:
	db #00, #00, #0D, #00, #A5, #00, #17, #00
	db #FA, #F4, #0F, #00, #81, #40, #1B, #00
	db #00, #00, #0F, #01, #A0, #00, #17, #01
	db #CA, #00, #1F, #03, #3F, #D1, #17, #07
	db #9D, #01, #F0, #07, #CA, #01, #C0, #0E
	db #A0, #01, #D0, #0E, #00, #01, #E0, #1D
	db #80, #03, #80, #3C, #44, #07, #60, #1A
	db #E9, #0F, #21, #DC, #FE, #1C, #47, #F8
	db #00, #FF, #00, #F0, #71, #14, #01, #F0
	db #3B, #09, #03, #E0, #5A, #00, #0F, #60
	db #30, #47, #1C, #C0, #2A, #C1, #0B, #00
	db #03, #50, #00, #00, #01, #72, #00, #00
	db #00, #FF, #FF, #FF, #BA, #89, #0A, #4A
	db #7E, #09, #02, #00, #3E, #11, #80, #80
	db #2F, #13, #42, #10, #1D, #81, #80, #00
	db #16, #D3, #40, #10, #0B, #81, #A9, #28
; attributes
	db #4F, #4F, #71, #4E, #4F, #4E, #4E, #4E
	db #07, #38, #46, #4E, #07, #38, #38, #78
THIEF:
	db #00, #02, #00, #00, #00, #02, #00, #00
	db #00, #07, #80, #00, #55, #06, #34, #00
	db #5A, #04, #80, #00, #FA, #87, #00, #00
	db #00, #42, #ED, #00, #CB, #23, #80, #00
	db #04, #00, #00, #00, #1E, #40, #10, #60
	db #40, #01, #2D, #00, #F5, #41, #1E, #74
	db #02, #00, #01, #00, #FD, #00, #83, #80
	db #20, #40, #1F, #A0, #DE, #00, #1F, #98
	db #04, #40, #8B, #08, #BB, #00, #02, #00
	db #00, #40, #4F, #80, #05, #40, #08, #00
	db #E0, #00, #02, #00, #C6, #00, #24, #00
	db #60, #40, #27, #40, #84, #40, #42, #00
	db #61, #01, #E3, #40, #24, #02, #60, #80
	db #80, #04, #90, #00, #00, #19, #2C, #08
	db #00, #30, #01, #30, #00, #48, #40, #00
	db #00, #10, #00, #00, #00, #80, #00, #00
; attributes
	db #01, #41, #01, #00, #05, #05, #05, #41
	db #05, #41, #05, #01, #01, #05, #41, #01
	endmodule