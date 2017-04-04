#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include "graphic.h"
#include "Colours.h"

#define RS232_Control_BT (*(volatile unsigned char *)(0x84000220))
#define RS232_Status_BT (*(volatile unsigned char *)(0x84000220))
#define RS232_TxData_BT (*(volatile unsigned char *)(0x84000222))
#define RS232_RxData_BT (*(volatile unsigned char *)(0x84000222))
#define RS232_Baud_BT (*(volatile unsigned char *)(0x84000224))
#define RS232_Control_HR (*(volatile unsigned char *)(0x84000250))
#define RS232_Status_HR (*(volatile unsigned char *)(0x84000250))
#define RS232_TxData_HR (*(volatile unsigned char *)(0x84000252))
#define RS232_RxData_HR (*(volatile unsigned char *)(0x84000252))
#define RS232_Baud_HR (*(volatile unsigned char *)(0x84000254))
#define RS232_Control_GPS (*(volatile unsigned char *)(0x84000210))
#define RS232_Status_GPS (*(volatile unsigned char *)(0x84000210))
#define RS232_TxData_GPS (*(volatile unsigned char *)(0x84000212))
#define RS232_RxData_GPS (*(volatile unsigned char *)(0x84000212))
#define RS232_Baud_GPS (*(volatile unsigned char *)(0x84000214))

typedef struct { char* lat; char* lng; } Coord ;

//initialize
void initRS232_GPS(void) {
	//8 data bits, 1 stop bit
	RS232_Control_GPS = 0x15;

	//9600 baud
	RS232_Baud_GPS = 0x05;
}

//read from rx
char getCharRS232_GPS(void) {
	while (!(RS232_Status_GPS & 0x01)) {
	};
	return RS232_RxData_GPS;
}

//write to tx
void putCharRS232_GPS(int c) {
	while (!(0x02 & RS232_Status_GPS)) {
	};
	RS232_TxData_GPS = c;
}

Coord getGPSdata() {
    int size = 500;
    Coord c;
    while (1) {
        char data[size];
        char character;
        int index = 0;
        const char s[2] = ",";
        //get data string
        do {
            character = getCharRS232_GPS();
            printf("%c", character);
            data[index] = character;
            index++;
        } while (character != '\n');

        char *token = strtok(data, s);

        if (!strcmp(token, "$GPGGA")) {
            printf("message_id: %s \n", token);

            token = strtok(NULL, s);
            double UTC_time = atof(token);
            printf("UTC_time: %f \n", UTC_time);

            token = strtok(NULL, s);
            char* latitude = token;
            printf("latitude: %s \n", latitude);

            token = strtok(NULL, s);
            char* NS_indicator = token;

            token = strtok(NULL, s);
            char* longitude = token;
            printf("longitude: %s \n", longitude);

            token = strtok(NULL, s);
            char* EW_indicator = token;

            double lat_double = atof(latitude);
            double long_double = atof(longitude);

            if (strcmp(NS_indicator, "S") == 0){
            	lat_double = lat_double*-1;
            }

            if (strcmp(EW_indicator, "W") == 0){
            	long_double = long_double*-1;
            }

            int lat_degree = (int) (lat_double/100);
            double lat_min = (lat_double-(lat_degree*100))/60;
            double lat_converted = lat_degree+lat_min;
            int long_degree = (int) (long_double/100);
            double long_min = (long_double-(long_degree*100))/60;
            double long_converted = long_degree+long_min;

            char *lat_char = malloc(100*sizeof(char));
            char *long_char = malloc(100*sizeof(char));
            snprintf(lat_char, 100, "%f", lat_converted);
            snprintf(long_char, 100, "%f", long_converted);

            c.lat = lat_char;
            c.lng = long_char;

            return c;
        }
    }
    return c;
}


char* concat(const char *s1, const char *s2, const char *s3, const char *s4){
    char *result = malloc(strlen(s1)+strlen(s2)+strlen(s3)+strlen(s4)+1);
    strcpy(result, s1);
    strcat(result, s2);
    strcat(result, s3);
    strcat(result, s4);
    return result;
}

char* concat2(const char *s1, const char *s2){
    char *result = malloc(strlen(s1)+strlen(s2)+1);
    strcpy(result, s1);
    strcat(result, s2);
    return result;
}

void initRS232_BT(void){
	//8 data bits, 1 stop bit
	RS232_Control_BT = 0x15;

	RS232_Baud_BT = 0x01;
}

//read from rx
char getCharRS232_BT(void) {
	while (!(RS232_Status_BT & 0x01)) {
	};
	return RS232_RxData_BT;
}


//read from rx
char getCharRS232_BT_TIMED(void) {
	int count = 0;
	while (!(RS232_Status_BT & 0x01)) {
		count++;
		if (count == 50)
			return NULL;
	};
	return RS232_RxData_BT;
}

//write to tx
void putCharRS232_BT(int c) {
	while (!(0x02 & RS232_Status_BT)) {
	};
	RS232_TxData_BT = c;
}

//initialize
void initRS232_HR(void) {
	//8 data bits, 1 stop bit
	RS232_Control_HR = 0x15;

	//9600 baud
	RS232_Baud_HR = 0x05;
}

//read from rx
char getCharRS232_HR(void) {
	while (!(RS232_Status_HR & 0x01)) {
	};
	return RS232_RxData_HR;
}

//write to tx
void putCharRS232_HR(int c) {
	while (!(0x02 & RS232_Status_HR)) {
	};
	RS232_TxData_HR = c;
}

void waitChar(int i){
	int j;
	for (j = 0; j < i; j++)
		getCharRS232_HR();
}

int getHR(){
	char HR_MSB;
	char HR_LSB;
	while (1){
		char s = getCharRS232_HR();
		if ((!(s&0xFE))&(s&0x01)){
			if (getCharRS232_HR()&0x01){
				waitChar(1);
				HR_MSB = getCharRS232_HR();
				waitChar(4);
				HR_LSB = getCharRS232_HR();

				int HR = (0x7F&HR_LSB)+((0x03&HR_MSB)<<7);

				return HR;
			}
		}
	}
	return 0;
}

int getHRData(){
	int i;
	int j;
	do{
		i = getHR();
		j = getHR();
	}
	while(i != j);
	printf("heart rate data: %d\n", i);
	return i;
}

void sendString(char* string){
	int i = 0;
	while (string[i] != NULL){
		putCharRS232_BT(string[i]);
		i++;
	}
	putCharRS232_BT('\r');
	putCharRS232_BT('\n');
}

char* readString(){
	char *s = malloc(30);

	int i = 0;
	char c = getCharRS232_BT_TIMED();
	if (c == NULL){
		s[0] = ' ';
		s[1] = '\0';
		return s;
	}
	while(c != '\r'){
		s[i] = c;
		i++;
		printf("%c", c);
		c = getCharRS232_BT();
	}
	getCharRS232_BT();
	s[i] = '\0';
	return s;
}

int main() {
	printf("starting initialization");
	initRS232_BT();
	initRS232_HR();
	initRS232_GPS();
	time_t start_t, end_t;
	time(&start_t);
	clearScreen(BLACK);
	setData();

	while(1){
		char *key = readString();
		if (!strcmp(key, "HeartRate")){
			char* buffer = malloc(30);
			snprintf(buffer, 30, "%d", getHRData());
			sendString(buffer);
			free(buffer);
		}
		else if (!strcmp(key, "Latitude")){
			Coord c = getGPSdata();
			sendString(c.lat);
			free(c.lat);
			free(c.lng);
		}
		else if (!strcmp(key, "Longitude")){
			Coord c = getGPSdata();
			sendString(c.lng);
			free(c.lat);
			free(c.lng);
		}
		free(key);
		time(&end_t);
		double diff_t = difftime(end_t, start_t);
		if (diff_t > 2){
			time(&start_t);
			updateData();
		}
	}
}

int old_hr;
char *old_lat;
char *old_lng;

void setData(){
	write(365, 75, "NEVERLOST", 12, WHITE, BLACK);

	Coord c = getGPSdata();
	int hr = getHRData();

	char* lat = concat2("Latitude: ", c.lat);
	write(100, 275, lat, 12, WHITE, BLACK);
	free(lat);

	char* lng = concat2("Longitude: ", c.lng);
	write(500, 275, lng, 12, WHITE, BLACK);
	free(lng);

	char* buffer = malloc(100);
	snprintf(buffer, 100, "Heart Rate: %d", hr);
	if (hr == 511)
		write(100, 175, "Heart Rate: Monitor not connected", 12, WHITE, BLACK);
	else
		write(100, 175, buffer, 12, WHITE, BLACK);
	free(buffer);

	old_hr = hr;
	old_lat = c.lat;
	old_lng = c.lng;
}

void updateData(){

	Coord c = getGPSdata();
	int hr = getHRData();

	if (strcmp(c.lat, old_lat) != 0){
		write(100, 275, "Latitude:            ", 12, WHITE, BLACK);
		char* lat = concat2("Latitude: ", c.lat);
		write(100, 275, lat, 12, WHITE, BLACK);
		free(lat);
		free(old_lat);
		old_lat = c.lat;
	}
	if (strcmp(c.lng, old_lng) != 0){
		old_lng = c.lng;
		write(500, 275, "Longitude:           ", 12, WHITE, BLACK);
		char* lng = concat2("Longitude: ", c.lng);
		write(500, 275, lng, 12, WHITE, BLACK);
		free(lng);
		free(old_lng);
		old_lng = c.lng;
	}
	if (old_hr != hr){
		old_hr = hr;
		write(100, 175, "Heart Rate:                             ", 12, WHITE, BLACK);
		char* buffer = malloc(100);
		snprintf(buffer, 100, "Heart Rate: %d", hr);
		if (hr == 511)
			write(100, 175, "Heart Rate: Monitor not connected", 12, WHITE, BLACK);
		else
			write(100, 175, buffer, 12, WHITE, BLACK);
		free(buffer);
	}
}
