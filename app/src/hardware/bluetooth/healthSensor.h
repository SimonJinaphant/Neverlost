
#ifndef HEALTHSENSOR_H_
#define HEALTHSENSOR_H_

void initRS232_HR(void);
char getCharRS232_HR(void);
void putCharRS232_HR(int c);
void waitChar(int i);
int getHR();
int getHRData();

#endif /* HEALTHSENSOR_H_ */
