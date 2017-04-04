/*
 * Colours.h
 *
 *  Created on: Feb 1, 2017
 *      Author: r3a9
 */

/*
 * Colours.h
 *
 *  Created on: Mar 14, 2015
 *      Author: Paul
 */

// see --http://www.rapidtables.com/web/color/RGB_Color.htm for example colours

// This file creates symbolic names for the colours that you can specify in your program
// the actual RGB values are set up in the FPGA Rom, so the name BLACK for example, refer to colour
// palette number 0 whose 24 bit RGB value is 0x000000, WHITE is palette number 1 = RGB value 0xFFFFFF etc
//
// See the ColourPalette.c source file

#ifndef COLOURS_H_
#define COLOURS_H_

// create a set of symbolic constants representing colour name e.g. BLACK, WHITE, RED = {0, 1, 2...} etc and a new data type called Colour (see bottom)
typedef enum  {
BLACK,
WHITE,
RED,
LIME,
BLUE,
YELLOW,
CYAN,
MAGENTA,
SILVER,
GRAY,
MAROON,
OLIVE,
GREEN,
PURPLE,
TEAL,
NAVY,
DARKRED ,
BROWN,
FIREBRICK,
CRIMSON,
TOMATO,
CORAL,
INDIAN_RED,
LIGHT_CORAL,
DARK_SALMON,
SALMON,
LIGHT_SALMON,
ORANGE_RED,
DARK_ORANGE,
ORANGE,
GOLD,
DARK_GOLDEN_ROD,
GOLDEN_ROD,
PALE_GOLDEN_ROD,
DARK_KHAKI,
KHAKI,
OLIVE_REPEAT,
YELLOW_REPEAT,
YELLOW_GREEN,
DARK_OLIVE_GREEN,
OLIVE_DRAB,
LAWN_GREEN,
CHART_REUSE,
GREEN_YELLOW,
DARK_GREEN,
GREEN_REPEAT,
FOREST_GREEN,
LIME_REPEAT,
LIME_GREEN,
LIGHT_GREEN,
PALE_GREEN,
DARK_SEA_GREEN,
MEDIUM_SPRING_GREEN,
SPRING_GREEN,
SEA_GREEN,
MEDIUM_AQUA_MARINE,
MEDIUM_SEA_GREEN,
LIGHT_SEA_GREEN,
DARK_SLATE_GRAY,
TEAL_REPEAT,
DARK_CYAN,
AQUA,
LIGHT_CYAN,
DARK_TURQUOISE,
TURQUOISE,
MEDIUM_TURQUOISE,
PALE_TURQUOISE,
AQUA_MARINE,
POWDER_BLUE,
CADET_BLUE,
STEEL_BLUE,
CORN_FLOWER_BLUE,
DEEP_SKY_BLUE,
DODGER_BLUE,
LIGHT_BLUE,
SKY_BLUE,
LIGHT_SKY_BLUE,
MIDNIGHT_BLUE,
NAVY_REPEAT,
DARK_BLUE,
MEDIUM_BLUE,
BLUE_REPEAT,
ROYAL_BLUE,
BLUE_VIOLET,
INDIGO,
DARK_SLATE_BLUE,
SLATE_BLUE,
MEDIUM_SLATE_BLUE,
MEDIUM_PURPLE,
DARK_MAGENTA,
DARK_VIOLET,
DARK_ORCHID,
MEDIUM_ORCHID,
PURPLE_REPEAT,
THISTLE,
PLUM,
VIOLET,
MAGENTA_REPEAT,
ORCHID,
MEDIUM_VIOLET_RED,
PALE_VIOLET_RED,
DEEP_PINK,
HOT_PINK,
LIGHT_PINK,
PINK,
ANTIQUE_WHITE,
BEIGE,
BISQUE,
BLANCHED_ALMOND,
WHEAT,
CORN_SILK,
LEMON_CHIFFON,
LIGHT_GOLDEN_ROD_YELLOW,
LIGHT_YELLOW,
SADDLE_BROWN,
SIENNA,
CHOCOLATE,
PERU,
SANDY_BROWN,
BURLY_WOOD,
TAN,
ROSY_BROWN,
MOCCASIN,
NAVAJO_WHITE,
PEACH_PUFF,
MISTY_ROSE,
LAVENDER_BLUSH,
LINEN,
OLD_LACE ,
PAPAYA_WHIP,
SEA_SHELL,
MINT_CREAM,
SLATE_GRAY,
LIGHT_SLATE_GRAY,
LIGHT_STEEL_BLUE,
LAVENDER,
FLORAL_WHITE,
ALICE_BLUE,
GHOST_WHITE,
HONEYDEW,
IVORY,
AZURE,
SNOW,
BLACK_REPEAT,
DIM_GRAY,
GRAY_REPEAT,
DARK_GRAY,
LIGHT_GRAY,
GAINSBORO,
WHITE_SMOKE,
WHITE_REPEAT,
} Colour;

#endif /* COLOURS_H_ */
