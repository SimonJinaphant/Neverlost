/*
 * graphic.h
 *
 *  Created on: Feb 25, 2017
 *      Author: r3a9
 */

#ifndef GRAPHIC_H_
#define GRAPHIC_H_

void WriteAPixel (int x, int y, int Colour);
int ReadAPixel (int x, int y);
void ProgramPalette(int PaletteNumber, int RGB);
void DrawHline(int x1, int y1, int x2, int y2, int color);
void DrawHlineWeighted(int x1, int y1, int x2, int y2, int color);
void DrawVlineWeighted(int x1, int y1, int x2, int y2, int color);
void DrawVline(int x1, int y1, int x2, int y2, int color);
void Drawline(int x1, int y1, int x2, int y2, int color);
void DrawSolidRectangle(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int color);
void DrawRectangle(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int color);
void clearScreen(int colour);
void write(int x, int y, char *string, int spacing, int color, int backgroundcolor);
void writeSmall(int x, int y, char *string, int spacing, int color, int backgroundcolor);
void drawcircle(int x0, int y0, int radius, int colour);
void drawSolidcircle(int x0, int y0, int radius, int colour);
void waitFor (unsigned int secs);

#endif /* GRAPHIC_H_ */
